package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.Post;
import kr.api.lenders.domain.PostRepository;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.type.PostCategoryType;
import kr.api.lenders.domain.type.PostStatusType;
import kr.api.lenders.error.BadRequestException;
import kr.api.lenders.error.ForbiddenException;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.service.value.PostCreateOrUpdateRequest;
import kr.api.lenders.service.value.PostResponse;
import kr.api.lenders.service.value.PostUpdateTraderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    @NotNull
    private final transient PostRepository postRepository;

    @NotNull
    private final transient UserService userService;

    public Post find(final long id) {
        return postRepository.findByIdAndStatusNot(id, PostStatusType.REMOVED)
                .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    public PostResponse findOne(final long id) {
        return PostResponse.of(find(id));
    }

    public PostResponse save(final PostCreateOrUpdateRequest postCreateOrUpdateRequest, final User currentUser) {
        Post post = Post.builder()
                .user(currentUser)
                .title(postCreateOrUpdateRequest.getTitle())
                .description(postCreateOrUpdateRequest.getDescription())
                .price(postCreateOrUpdateRequest.getPrice())
                .currency(postCreateOrUpdateRequest.getCurrency())
                .category(PostCategoryType.valueOf(postCreateOrUpdateRequest.getCategory()))
                .build();
        post = postRepository.save(post);

        return PostResponse.of(post);
    }

    public PostResponse update(final long id, final PostCreateOrUpdateRequest postCreateOrUpdateRequest, final User currentUser) {
        Post post = find(id);
        if (post.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("Cannot update other user's post");
        }

        if (!post.getStatus().equals(PostStatusType.AVAILABLE)) {
            throw new BadRequestException("Cannot update post that is not available");
        }

        post.updateInfo(postCreateOrUpdateRequest);
        post = postRepository.save(post);

        return PostResponse.of(post);
    }

    public PostResponse updateTrader(long id, User currentUser, PostUpdateTraderRequest postUpdateTraderRequest) {
        Post post = find(id);
        if (post.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("Cannot update other user's post");
        }

        if (postUpdateTraderRequest.getTraderId() != null &&
                post.getUser().getId() == postUpdateTraderRequest.getTraderId()) {
            throw new ForbiddenException("Post's owner cannot trade one's own post");
        }

        User trader = postUpdateTraderRequest.getTraderId() != null ?
                userService.find(postUpdateTraderRequest.getTraderId()) : null;
        post.updateTrader(trader);
        post = postRepository.save(post);

        return PostResponse.of(post);
    }

    public PostResponse remove(long id, User currentUser) {
        Post post = find(id);
        if (post.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("Cannot delete other user's post");
        }

        if (!post.getStatus().equals(PostStatusType.AVAILABLE)) {
            throw new BadRequestException("Cannot delete post that is not available");
        }

        post.remove();
        post = postRepository.save(post);

        return PostResponse.of(post);
    }
}
