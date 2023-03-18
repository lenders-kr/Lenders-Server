package kr.api.lenders.domain.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostCategoryType {
    ELECTRONICS("ELECTRONICS"),
    FURNITURE("FURNITURE"),
    KITCHEN("KITCHEN"),
    INTERIOR("INTERIOR"),
    LIVING("LIVING"),
    BABY("BABY"),
    BOOK("BOOK"),
    CLOTHES("CLOTHES"),
    FASHION("FASHION"),
    BEAUTY("BEAUTY"),
    SPORTS_AND_LEISURE("SPORTS_AND_LEISURE"),
    GAME("GAME"),
    MUSIC("MUSIC"),
    PLANTS("PLANTS"),
    OTHER("OTHER");

    @Getter
    private final String code;
}
