package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.*;
import kr.api.lenders.domain.type.PostCategoryType;
import kr.api.lenders.domain.type.PostStatusType;
import kr.api.lenders.error.BadRequestException;
import kr.api.lenders.error.ForbiddenException;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.service.value.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    @NotNull
    private final transient PostRepository postRepository;

    @NotNull
    private final transient UserService userService;

    @NotNull
    private final transient UserBookmarkPostRepository userBookmarkPostRepository;

    @NotNull
    private final transient LocationService locationService;

    public Post find(final long id) {
        return postRepository.findByIdAndStatusNot(id, PostStatusType.REMOVED)
                .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    public PostResponse findOne(final long id) {
        return PostResponse.of(find(id));
    }

    public PostResponse save(final PostCreateRequest postCreateRequest, final User currentUser) {
        Location location = locationService.findOne(postCreateRequest.getLocationId());
        Post post = Post.builder()
                .user(currentUser)
                .title(postCreateRequest.getTitle())
                .description(postCreateRequest.getDescription())
                .price(postCreateRequest.getPrice())
                .currency(postCreateRequest.getCurrency())
                .category(PostCategoryType.valueOf(postCreateRequest.getCategory()))
                .location(location)
                .build();
        post = postRepository.save(post);

        List<PostImage> images = newPostImages(post.getId(), postCreateRequest.getImages());
        for (PostImage image : images) {
            post.addImage(image);
        }
        post = postRepository.save(post);

        return PostResponse.of(post);
    }

    public PostResponse update(final long id, final PostUpdateRequest postUpdateRequest, final User currentUser) {
        Post post = find(id);
        if (post.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("Cannot update other user's post");
        }

        if (post.getStatus() != PostStatusType.AVAILABLE &&
                post.getStatus() != PostStatusType.RESERVING) {
            throw new BadRequestException("Cannot update post with status: " + post.getStatus());
        }

        Location location = locationService.findOne(postUpdateRequest.getLocationId());
        post.updateInfo(postUpdateRequest.getTitle(), postUpdateRequest.getDescription(),
                postUpdateRequest.getPrice(), postUpdateRequest.getCurrency(),
                PostCategoryType.valueOf(postUpdateRequest.getCategory()), location);

        // remove all existing images first
        post.removeAllImages();

        // add new images
        List<PostImage> images = newPostImages(post.getId(), postUpdateRequest.getImages());
        for (PostImage image : images) {
            post.addImage(image);
        }
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

        if (post.getStatus() != PostStatusType.AVAILABLE &&
                post.getStatus() != PostStatusType.RESERVING) {
            throw new BadRequestException("Cannot update post with status: " + post.getStatus());
        }

        User trader = postUpdateTraderRequest.getTraderId() != null ?
                userService.find(postUpdateTraderRequest.getTraderId()) : null;
        post.updateTrader(trader);
        post = postRepository.save(post);

        return PostResponse.of(post);
    }

    @Transactional
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

        // remove all related UserBookmarkPost
        userBookmarkPostRepository.deleteAllByPostId(post.getId());

        return PostResponse.of(post);
    }

    public Page<PostResponse> findAllByLocationDistrict(String district, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByLocationDistrict(district, pageable);
        return new PageImpl<>(
                posts.getContent().stream().map(PostResponse::of).collect(Collectors.toList()),
                posts.getPageable(),
                posts.getNumberOfElements()
        );
    }

    /**
     * Private methods
     */

    private List<PostImage> newPostImages(long postId, List<String> images) {
        if (images.size() > 10) {
            throw new BadRequestException("Cannot upload more than 10 images");
        }

        List<PostImage> newPostImages = new ArrayList<>();
        byte seq = 0;
        for (String image : images) {
            PostImage postImage = PostImage.builder()
                    .postId(postId)
                    .image(image)
                    .seq(seq++)
                    .build();
            newPostImages.add(postImage);
        }
        return newPostImages;
    }
}
