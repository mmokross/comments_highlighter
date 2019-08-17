package com.clutcher.comments.utils;

import com.intellij.openapi.editor.colors.TextAttributesKey;

import java.util.Arrays;
import java.util.List;

public class HighlightTextAttributeUtils {

    private static final List<Character> IGNORE_START_LINE_CHARACTERS_LIST = Arrays.asList('/', '<', '-', ' ', '#');

    public static final TextAttributesKey INFO_COMMENT = TextAttributesKey.createTextAttributesKey("INFO_COMMENT");
    public static final TextAttributesKey WARN_COMMENT = TextAttributesKey.createTextAttributesKey("WARN_COMMENT");
    public static final TextAttributesKey ERROR_COMMENT = TextAttributesKey.createTextAttributesKey("ERROR_COMMENT");

    public static TextAttributesKey getHighlightTextAttribute(String line, boolean isDocComment) {
        boolean firstSkip = false;
        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);
            if (isHighlightTextCharacter(currentChar, line, i)) {
                if (isDocComment && currentChar == '*' && !firstSkip) {
                    firstSkip = true;
                    continue;
                }

                return getHighlightTextAttribute(currentChar);
            }
        }
        return null;
    }

    public static TextAttributesKey getHighlightTextAttribute(char firstChar) {
        if (firstChar == '*') {
            return INFO_COMMENT;
        } else if (firstChar == '?') {
            return WARN_COMMENT;
        } else if (firstChar == '!') {
            return ERROR_COMMENT;
        }
        return null;
    }

    private static boolean isHighlightTextCharacter(char currentChar, String line, int i) {
        // Optimized check to not highlight "/*" and "*/" lines
        return !IGNORE_START_LINE_CHARACTERS_LIST.contains(currentChar) && ((currentChar != '/' && currentChar != '*') || line.indexOf("/*") != i - 1 && line.indexOf("*/") != i);
    }


}