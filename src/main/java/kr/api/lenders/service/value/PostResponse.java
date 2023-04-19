package kr.api.lenders.service.value;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.api.lenders.domain.Post;
import kr.api.lenders.domain.PostImage;
import kr.api.lenders.domain.type.PostCategoryType;
import kr.api.lenders.domain.type.PostStatusType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class PostResponse {
    long id;

    UserResponse user;

    UserResponse trader;

    String title;

    String description;

    double price;

    String currency;

    PostCategoryType category;

    PostStatusType status;

    List<PostImage> images;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime tradedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updatedAt;

    LocationResponse location;

    public static PostResponse of(final Post post) {
        PostResponseBuilder postResponse =  PostResponse.builder()
                .id(post.getId())
                .user(UserResponse.of(post.getUser()))
                .title(post.getTitle())
                .description(post.getDescription())
                .price(post.getPrice())
                .currency(post.getCurrency())
                .category(post.getCategory())
                .status(post.getStatus())
                .images(post.getImages())
                .tradedAt(post.getTradedAt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .location(LocationResponse.of(post.getLocation()));

        if (post.getTrader() != null) {
            postResponse.trader(UserResponse.of(post.getTrader()));
        }

        return postResponse.build();
    }
}
