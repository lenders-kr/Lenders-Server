package kr.api.lenders.service.value;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.api.lenders.domain.Post;
import kr.api.lenders.domain.UserBookmarkPost;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UserBookmarkPostResponse {
    long id;

    Post post;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updatedAt;

    public static UserBookmarkPostResponse of(final UserBookmarkPost userBookmarkPost) {
        return UserBookmarkPostResponse.builder()
                .id(userBookmarkPost.getId())
                .post(userBookmarkPost.getPost())
                .createdAt(userBookmarkPost.getCreatedAt())
                .updatedAt(userBookmarkPost.getUpdatedAt())
                .build();
    }
}
