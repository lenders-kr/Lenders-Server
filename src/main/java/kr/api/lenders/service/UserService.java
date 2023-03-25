package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.UserRepository;
import kr.api.lenders.domain.type.UserRoleType;
import kr.api.lenders.error.DuplicationException;
import kr.api.lenders.error.ForbiddenException;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.error.ParameterValidationException;
import kr.api.lenders.service.value.UserLoginReqeust;
import kr.api.lenders.service.value.UserRegisterRequest;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    @NotNull
    private final transient UserRepository userRepository;

    @NotNull
    private final transient UserSsoDetailService userSsoDetailService;

    private static final String EMAIL_PATTERN = "\\A[\\w+\\-.]+@[a-z\\d\\-.]+\\.[a-z]+\\z";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[`~!@#$%^&*(){}\\[\\]:\";'<>?,.\\/\\-_=+])[A-Za-z\\d`~!@#$%^&*(){}\\[\\]:\";'<>?,.\\/\\-_=+]{8,}$";

    public User find(final long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserResponse findOne(final long id) {
        return UserResponse.of(find(id));
    }

    public UserResponse update(
            final long id,
            final User currentUser,
            final UserUpdateRequest userUpdateRequest
    ) {
        User user = find(id);
        if (currentUser.getId() != user.getId()) {
            throw new ForbiddenException("You are not allowed to update other user's info");
        }

        user.updateInfo(userUpdateRequest);
        user = userRepository.save(user);

        return UserResponse.of(user);
    }

    public User register(UserRegisterRequest userRegisterRequest) {
        if (!userRegisterRequest.getEmail().matches(EMAIL_PATTERN)) {
            throw new ParameterValidationException("Invalid email format");
        }
        if (!userRegisterRequest.getPassword().matches(PASSWORD_PATTERN)) {
            throw new ParameterValidationException("Invalid password format");
        }

        userRepository.findByEmail(userRegisterRequest.getEmail())
                .ifPresent(user -> {throw new DuplicationException("Email already exists");});

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(userRegisterRequest.getPassword());

        User user = User.builder()
                .email(userRegisterRequest.getEmail())
                .password(encryptedPassword)
                .name(userRegisterRequest.getName())
                .role(UserRoleType.ROLE_USER)
                .build();
        return userRepository.save(user);
    }

    public User login(UserLoginReqeust userLoginReqeust) {
        User user = userRepository.findByEmail(userLoginReqeust.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(userLoginReqeust.getPassword(), user.getPassword())) {
            throw new ParameterValidationException("Invalid password");
        }

        return user;
    }
}
