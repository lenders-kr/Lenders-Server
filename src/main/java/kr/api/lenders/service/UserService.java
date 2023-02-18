package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.UserRepository;
import kr.api.lenders.domain.UserSsoDetail;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserSocialLoginRequest;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    @NotNull
    private final transient UserRepository userRepository;

    @NotNull
    private final transient UserSsoDetailService userSsoDetailService;

    public User find(final long id) {
        /**
         * [TODO]
         *   add status check
         */
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserResponse findOne(final long id) {
        return UserResponse.of(find(id));
    }

    /**
     * [TODO]
     *   implement auth
     */
    public UserResponse update(final UserUpdateRequest userUpdateRequest) {
        User user = find(userUpdateRequest.getId());
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .nickname(userUpdateRequest.getNickname())
                .image(userUpdateRequest.getImage())
                .build();
        user.updateInfo(updateRequest);
        user = userRepository.save(user);

        return UserResponse.of(user);
    }

    public UserResponse socialLogin(UserSocialLoginRequest userSocialLoginRequest) {
        UserSsoDetail userSsoDetail = userSsoDetailService
                .findOrCreate(userSocialLoginRequest.getProviderType(), userSocialLoginRequest.getIdentifier());
        return UserResponse.of(userSsoDetail.getUser());
    }
}
