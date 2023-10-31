package com.willyoubackend.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KoreanSizeValidator implements ConstraintValidator<KoreanSize, String> {
    private int max;

    @Override
    public void initialize(KoreanSize constraintAnnotation) {
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int koreanCharCount = 0;
        for (char c : value.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                    Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO ||
                    Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
                koreanCharCount++;
            }
        }
        return koreanCharCount <= max;
    }
}
