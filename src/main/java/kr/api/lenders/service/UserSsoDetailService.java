package kr.api.lenders.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.UserRepository;
import kr.api.lenders.domain.UserSsoDetail;
import kr.api.lenders.domain.UserSsoDetailRepository;
import kr.api.lenders.domain.type.UserRoleType;
import kr.api.lenders.domain.type.UserSsoProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSsoDetailService {
    @NotNull
    private final transient UserSsoDetailRepository userSsoDetailRepository;

    @NotNull
    private final transient UserRepository userRepository;

    @NotNull
    private final transient GoogleApiClientService googleApiClientService;

    private Optional<UserSsoDetail> find(final UserSsoProviderType provider, final String identifier) {
        return userSsoDetailRepository.findByProviderAndIdentifier(provider, identifier);
    }

    @Transactional
    public UserSsoDetail findOrCreate(final UserSsoProviderType provider, final String token) {
        Payload payload = googleApiClientService.verifyToken(token);
        Optional<UserSsoDetail> userSsoDetail = find(provider, payload.getSubject());
        // return existing one if userSsoDetail is present
        if (userSsoDetail.isPresent()) {
            return userSsoDetail.get();
        }

        // create or find user
        String email = payload.getEmail();
        String name = ObjectUtils.isEmpty(payload.get("name")) ? "Unknown" : payload.get("name").toString();
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .role(UserRoleType.ROLE_USER)
                            .build();
                    return userRepository.save(newUser);
        });

        UserSsoDetail newUserSsoDetail = UserSsoDetail.builder()
                .user(user)
                .provider(provider)
                .identifier(payload.getSubject())
                .build();

        return userSsoDetailRepository.save(newUserSsoDetail);
    }
}
