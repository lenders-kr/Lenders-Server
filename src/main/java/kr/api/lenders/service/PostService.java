package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.Post;
import kr.api.lenders.domain.PostRepository;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.type.PostCategoryType;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.service.value.PostCreateRequest;
import kr.api.lenders.service.value.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    @NotNull
    private final transient PostRepository postRepository;

    public Post find(final long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    public PostResponse findOne(final long id) {
        return PostResponse.of(find(id));
    }

    public PostResponse save(final PostCreateRequest postCreateRequest, User currentUser) {
        Post post = Post.builder()
                .user(currentUser)
                .title(postCreateRequest.getTitle())
                .description(postCreateRequest.getDescription())
                .price(postCreateRequest.getPrice())
                .currency(postCreateRequest.getCurrency())
                .category(PostCategoryType.valueOf(postCreateRequest.getCategory()))
                .build();
        post = postRepository.save(post);

        return PostResponse.of(post);
    }
}
