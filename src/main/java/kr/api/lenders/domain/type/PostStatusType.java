package kr.api.lenders.domain.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostStatusType {
    AVAILABLE("AVAILABLE"),
    TRADING("TRADING"),
    SOLD("SOLD"),
    REMOVED("REMOVED");

    @Getter
    private final String code;
}
