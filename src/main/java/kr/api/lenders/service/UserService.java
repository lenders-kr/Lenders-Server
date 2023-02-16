package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.UserRepository;
import kr.api.lenders.domain.type.StatusType;
import kr.api.lenders.error.DuplicationException;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.error.ParameterValidationException;
import kr.api.lenders.service.value.UserCreateRequest;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static String EMAIL_PATTERN = "\\A[\\w+\\-.]+@[a-z\\d\\-.]+\\.[a-z]+\\z";
    @NotNull
    private final transient UserRepository userRepository;

    public User find(final long id) {
        /**
         * [TODO]
         *   add status check
         */
        return userRepository.findByIdAndStatus(id, StatusType.LIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserResponse findOne(final long id) {
        return UserResponse.of(find(id));
    }

    public UserResponse save(final UserCreateRequest userCreateRequest) {
        if (!userCreateRequest.getEmail().matches(EMAIL_PATTERN)) {
            throw new ParameterValidationException("Invalid email format");
        }

        userRepository
                .findByEmailAndStatus(userCreateRequest.getEmail(), StatusType.LIVE)
                .ifPresent(user -> {throw new DuplicationException("User exists");});

        User user = User.builder()
                .email(userCreateRequest.getEmail())
                .name(userCreateRequest.getName())
                .nickname(userCreateRequest.getNickname())
                .build();
        user = userRepository.save(user);

        return UserResponse.of(user);
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

    /**
     * [TODO]
     *   implement auth
     */
    public UserResponse delete(final long id) {
        User user = find(id);
        user.delete();
        user = userRepository.save(user);

        return UserResponse.of(user);
    }
}
