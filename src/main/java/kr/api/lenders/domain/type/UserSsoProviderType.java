package kr.api.lenders.domain.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserSsoProviderType {
    GOOGLE("GOOGLE");

    @Getter
    private final String code;
}
