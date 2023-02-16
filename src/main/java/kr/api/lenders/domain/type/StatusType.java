package kr.api.lenders.domain.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusType {
    LIVE(0), REMOVED(1);

    @Getter
    private final int code;
}
