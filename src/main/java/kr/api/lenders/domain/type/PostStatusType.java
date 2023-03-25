package kr.api.lenders.domain.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostStatusType {
    AVAILABLE("AVAILABLE"),
    RESERVING("RESERVING"),
    LENDING("LENDING"),
    LENT("LENT"),
    RETURNED("RETURNED"),
    DONE("DONE"),
    REMOVED("REMOVED");

    @Getter
    private final String code;
}
