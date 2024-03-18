package pl.daveproject.webdiet.caloricneeds.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityLevel {
    NONE(1.2, "activity-level.none"),
    SMALL(1.4, "activity-level.low"),
    MEDIUM(1.6, "activity-level.medium"),
    BIG(1.8, "activity-level.high"),
    VERY_BIG(2.0, "activity-level.very-high");

    private final double pal;
    private final String translationKey;
}
