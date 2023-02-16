package kr.api.lenders.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import kr.api.lenders.domain.type.StatusType;
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

    private String name;

    @Column(length = 20)
    private String nickname;

    private String image;

    private StatusType status;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String name, String nickname) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.status = StatusType.LIVE;
    }

    public void updateInfo(UserUpdateRequest userUpdateRequest) {
        this.nickname = userUpdateRequest.getNickname();
        this.image = userUpdateRequest.getImage();
    }

    public void delete() {
        this.status = StatusType.REMOVED;
        this.email = this.email + "|" + this.id;
    }
}
