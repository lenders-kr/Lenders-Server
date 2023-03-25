package kr.api.lenders.service.value;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.api.lenders.domain.Review;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ReviewResponse {
    long id;

    long postId;

    String postTitle;

    UserResponse user;

    String content;

    byte rating;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updatedAt;

    public static ReviewResponse of(final Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .user(UserResponse.of(review.getUser()))
                .postId(review.getPost().getId())
                .postTitle(review.getPost().getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
