package kr.api.lenders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.type.PostCategoryType;
import kr.api.lenders.domain.type.PostStatusType;
import kr.api.lenders.service.value.PostUpdateRequest;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore // prevent infinite loop
    private User user;

    @NotNull
    @Column(length = 50)
    private String title;

    @NotNull
    @Column(length = 1000)
    private String description;

    private double price;

    @NotNull
    @Column(length = 5)
    private String currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostCategoryType category = PostCategoryType.OTHER;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostStatusType status = PostStatusType.AVAILABLE;

    @Nullable // nullable since there may not be any trader for a post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trader_id")
    @JsonIgnore
    private User trader;

    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime tradedAt;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="postId", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    /**
     * [TODO] add a field (or a table) for post's trading history
     */

    @Builder
    public Post(User user, String title, String description, double price, String currency, PostCategoryType category,
                Location location) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.category = category;
        this.location = location;
    }

    public void updateInfo(String title, String description, double price, String currency, PostCategoryType category,
                           Location location) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.category = category;
        this.location = location;
    }

    public void updateTrader(User trader) {
        if (trader == null) {
            this.trader = null;
            this.status = PostStatusType.AVAILABLE;
        } else {
            this.trader = trader;
            this.status = PostStatusType.RESERVING;
        }
    }

    public void remove() {
        this.status = PostStatusType.REMOVED;
    }

    public void addImage(PostImage postImage) {
        this.images.add(postImage);
    }

    public void removeAllImages() {
        this.images.clear();
    }
}
