package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.*;
import kr.api.lenders.domain.type.PostStatusType;
import kr.api.lenders.error.ConflictException;
import kr.api.lenders.error.ForbiddenException;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.service.value.OkResponse;
import kr.api.lenders.service.value.UserBookmarkPostCreateRequest;
import kr.api.lenders.service.value.UserBookmarkPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBookmarkPostService {
    @NotNull
    private final transient UserBookmarkPostRepository userBookmarkPostRepository;

    @NotNull
    private final transient PostRepository postRepository;

    public UserBookmarkPostResponse save(
            final User currentUser,
            final UserBookmarkPostCreateRequest userBookmarkPostCreateRequest
            ) {
        Post post = postRepository.findById(userBookmarkPostCreateRequest.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getStatus().equals(PostStatusType.REMOVED)) {
            throw new NotFoundException("Post not found");
        }
        if (post.getUser().getId() == currentUser.getId()) {
            throw new ForbiddenException("Cannot bookmark own post");
        }
        if (userBookmarkPostRepository.existsByUserIdAndPostId(currentUser.getId(), post.getId())) {
            throw new ConflictException("User already bookmarked this post");
        }

        UserBookmarkPost userBookmarkPost = UserBookmarkPost.builder()
                .user(currentUser)
                .post(post)
                .build();
        userBookmarkPost = userBookmarkPostRepository.save(userBookmarkPost);

        return UserBookmarkPostResponse.of(userBookmarkPost);
    }

    public OkResponse remove(final User currentUser, final long id) {
        UserBookmarkPost userBookmarkPost = userBookmarkPostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User bookmark post not found"));
        if (userBookmarkPost.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("Cannot remove other user's bookmark post");
        }
        userBookmarkPostRepository.delete(userBookmarkPost);

        return new OkResponse();
    }

    public Page<UserBookmarkPostResponse> findAllByUser(final User currentUser, final Pageable pageable) {
        Page<UserBookmarkPost> userBookmarkPosts =
                userBookmarkPostRepository.findAllByUserId(currentUser.getId(), pageable);

        return new PageImpl<>(
                userBookmarkPosts.getContent().stream().map(UserBookmarkPostResponse::of).collect(Collectors.toList()),
                userBookmarkPosts.getPageable(),
                userBookmarkPosts.getNumberOfElements()
        );
    }
}
