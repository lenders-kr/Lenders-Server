package kr.api.lenders.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.type.UserRoleType;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@ToString
public class User implements UserDetails {
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

    @NotNull
    private UserRoleType role;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String name, UserRoleType role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = name; // default nickname is name
        this.role = role;
    }

    public void updateInfo(UserUpdateRequest userUpdateRequest) {
        this.nickname = userUpdateRequest.getNickname();
        this.image = userUpdateRequest.getImage();
    }

    /**
     * UserDetail implementation methods start
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * UserDetail implementation methods end
     */
}
