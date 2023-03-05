package kr.api.lenders.domain.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserRoleType {
    ROLE_USER(0), ROLE_ADMIN(1);

    @Getter
    private final int code;
}
