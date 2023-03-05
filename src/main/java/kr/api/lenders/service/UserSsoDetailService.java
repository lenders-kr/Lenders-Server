package kr.api.lenders.service;

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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSsoDetailService {
    @NotNull
    private final transient UserSsoDetailRepository userSsoDetailRepository;

    @NotNull
    private final transient UserRepository userRepository;

    private Optional<UserSsoDetail> find(final UserSsoProviderType provider, final String identifier) {
        return userSsoDetailRepository.findByProviderAndIdentifier(provider, identifier);
    }

    /**
     * [TODO]
     *   add identifier validation logic
     */
    @Transactional
    public UserSsoDetail findOrCreate(final UserSsoProviderType provider, final String identifier) {
        Optional<UserSsoDetail> userSsoDetail = find(provider, identifier);
        // return existing one if userSsoDetail is present
        if (userSsoDetail.isPresent()) {
            return userSsoDetail.get();
        }

        // create new user
        User user = User.builder()
                .email("test@1.com") // [TODO] add email from identifier toke
                .name("test") // [TODO] add name from identifier token
                .role(UserRoleType.ROLE_USER)
                .build();
        user = userRepository.save(user);

        UserSsoDetail newUserSsoDetail = UserSsoDetail.builder()
                .user(user)
                .provider(provider)
                .identifier(identifier)
                .build();
        newUserSsoDetail = userSsoDetailRepository.save(newUserSsoDetail);

        return newUserSsoDetail;
    }
}
