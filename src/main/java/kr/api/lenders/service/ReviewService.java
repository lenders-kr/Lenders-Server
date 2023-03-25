package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.*;
import kr.api.lenders.domain.type.PostStatusType;
import kr.api.lenders.error.ConflictException;
import kr.api.lenders.error.ForbiddenException;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.service.value.ReviewCreateRequest;
import kr.api.lenders.service.value.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @NotNull
    private final ReviewRepository reviewRepository;

    @NotNull
    private final PostRepository postRepository;

    public ReviewResponse save(User currentUser, ReviewCreateRequest reviewCreateRequest) {
        Post post = postRepository.findById(reviewCreateRequest.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found"));

        User owner = post.getUser();
        User trader = post.getTrader();
        if (trader == null || !post.getStatus().equals(PostStatusType.DONE)) {
            throw new ForbiddenException("Review can only be created when post is done and has a trader");
        }

        if (currentUser.getId() != owner.getId() && currentUser.getId() != trader.getId()) {
            throw new ForbiddenException("Review can only be created by owner or trader of the post");
        }

        if (reviewRepository.existsByPostAndUser(post, currentUser)) {
            throw new ConflictException("Review already exists for this post");
        }

        Review review = Review.builder()
                .post(post)
                .user(currentUser)
                .content(reviewCreateRequest.getContent())
                .rating(reviewCreateRequest.getRating())
                .build();
        review = reviewRepository.save(review);

        return ReviewResponse.of(review);
    }

    public ReviewResponse findOne(final long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        return ReviewResponse.of(review);
    }

    public Page<ReviewResponse> findAllByUserId(final long userId, final Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByUserId(userId, pageable);
        return new PageImpl<>(
                reviews.getContent().stream().map(ReviewResponse::of).collect(Collectors.toList()),
                reviews.getPageable(),
                reviews.getNumberOfElements()
        );
    }

    public Page<ReviewResponse> findAllByPostId(final long postId, final Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByPostId(postId, pageable);
        return new PageImpl<>(
                reviews.getContent().stream().map(ReviewResponse::of).collect(Collectors.toList()),
                reviews.getPageable(),
                reviews.getNumberOfElements()
        );
    }
}
