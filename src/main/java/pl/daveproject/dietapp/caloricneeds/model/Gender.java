package pl.daveproject.dietapp.caloricneeds.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE(66, 13.7, 5.0, 6.8, "gender.male"),
    FEMALE(665, 9.6, 1.8, 4.7, "gender.female");

    private final int mainPrefix;
    private final double weightPrefix;
    private final double heightPrefix;
    private final double agePrefix;
    private final String translationKey;
}
