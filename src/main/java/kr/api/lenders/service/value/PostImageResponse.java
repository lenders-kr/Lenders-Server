package kr.api.lenders.service.value;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.api.lenders.domain.PostImage;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class PostImageResponse {
    long id;

    String image;

    byte seq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updatedAt;

    public static PostImageResponse of(final PostImage postImage) {
        return PostImageResponse.builder()
                .id(postImage.getId())
                .image(postImage.getImage())
                .seq(postImage.getSeq())
                .createdAt(postImage.getCreatedAt())
                .updatedAt(postImage.getUpdatedAt())
                .build();
    }
}
