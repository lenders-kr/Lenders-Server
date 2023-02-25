package kr.api.lenders.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @Nullable
    private String password; // null on social login create user

    private String name;

    @Column(length = 20)
    private String nickname;

    private String image;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = name; // default nickname is name
    }

    public void updateInfo(UserUpdateRequest userUpdateRequest) {
        this.nickname = userUpdateRequest.getNickname();
        this.image = userUpdateRequest.getImage();
    }
}
