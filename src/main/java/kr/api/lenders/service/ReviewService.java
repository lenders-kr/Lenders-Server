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
import org.springframework.stereotype.Service;

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
        if (trader == null || !post.getStatus().equals(PostStatusType.SOLD)) {
            throw new ForbiddenException("Review can only be created for sold posts with a trader");
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
}
