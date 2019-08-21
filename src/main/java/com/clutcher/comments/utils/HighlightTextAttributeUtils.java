package com.clutcher.comments.utils;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighlightTextAttributeUtils {

    private static final List<Character> START_LINE_CHARACTERS_LIST = Arrays.asList('/', '<', '-', ' ', '#', '*', '!');


    public static final TextAttributesKey INFO_COMMENT = TextAttributesKey.createTextAttributesKey("INFO_COMMENT");
    public static final TextAttributesKey WARN_COMMENT = TextAttributesKey.createTextAttributesKey("WARN_COMMENT");
    public static final TextAttributesKey ERROR_COMMENT = TextAttributesKey.createTextAttributesKey("ERROR_COMMENT");


    public static Map<TextRange, TextAttributesKey> getCommentHighlights(String comment, int startOffset) {
        // General comment data
        final int commentLength = comment.length();
        final int lastCharPosition = comment.length() - 1;
        final boolean isDocComment = comment.startsWith("/**");

        // Variables to process current line highlighting
        int currentLineStartIndex = startOffset;
        boolean isProcessedCurrentLine = false;
        boolean isHighlightedCurrentLine = false;
        boolean isSkippedFirstStarCharInDocComment = false;
        TextAttributesKey currentLineHighlightAttribute = null;

        // Result map from which annotation would be created
        Map<TextRange, TextAttributesKey> highlightAnnotationData = new HashMap<>();

        for (int i = 0; i < commentLength; i++) {
            char c = comment.charAt(i);
            // Reset attributes and create highlight on line end
            if (c == '\n' || i == lastCharPosition) {
                if (isHighlightedCurrentLine) {
                    highlightAnnotationData.put(new TextRange(currentLineStartIndex, startOffset + i + 1), currentLineHighlightAttribute);
                }
                currentLineStartIndex = startOffset + i + 1;
                isHighlightedCurrentLine = false;
                isProcessedCurrentLine = false;
                isSkippedFirstStarCharInDocComment = false;
                continue;
            }

            // Skip processing of current char in line if highlight was already defined
            if (isProcessedCurrentLine) {
                continue;
            }

            // Skip processing of first "*" in doc comments
            if (!isSkippedFirstStarCharInDocComment && shouldSkipFistStarInDocComment(c, isDocComment)) {
                isSkippedFirstStarCharInDocComment = true;
                continue;
            }

            // Create highlight if current char is valid highlight char
            if (isHighlightTriggerChar(c) && isValidPosition(comment, i)) {
                isHighlightedCurrentLine = true;
                currentLineHighlightAttribute = getHighlightTextAttribute(c);
            }

            // Check that line highlight was defined and no more processing needs
            if (!isValidStartLineChar(c)) {
                isProcessedCurrentLine = true;
            }
        }
        return highlightAnnotationData;
    }

    private static boolean shouldSkipFistStarInDocComment(char c, boolean isDocComment) {
        return isDocComment && c == '*';
    }


    private static boolean isValidPosition(String comment, int i) {
        char c = comment.charAt(i);
        if (c == '!') {
            // Skip "<!-" and shebang "#!/"
            return !((comment.charAt(i - 1) == '<' && comment.charAt(i + 1) == '-') || (comment.charAt(i - 1) == '#' && comment.charAt(i + 1) == '/'));
        } else if (c == '*') {
            // Skip "/*", "*/", "/**"
            return !(comment.charAt(i - 1) == '/' || (comment.charAt(i - 2) == '/' && comment.charAt(i - 1) == '*') || comment.charAt(i + 1) == '/');
        }

        return true;
    }

    private static boolean isValidStartLineChar(char c) {
        return START_LINE_CHARACTERS_LIST.contains(c);
    }

    private static boolean isHighlightTriggerChar(char c) {
        return c == '*' || c == '?' || c == '!';
    }

    private static TextAttributesKey getHighlightTextAttribute(char firstChar) {
        if (firstChar == '*') {
            return INFO_COMMENT;
        } else if (firstChar == '?') {
            return WARN_COMMENT;
        } else if (firstChar == '!') {
            return ERROR_COMMENT;
        }
        return null;
    }

}