package com.clutcher.comments.utils;

import com.clutcher.comments.configuration.CommentTokenConfiguration;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighlightTextAttributeUtils {

    private HighlightTextAttributeUtils() {
    }

    private static final List<Character> START_LINE_CHARACTERS_LIST = Arrays.asList('/', '<', '-', ' ', '#', '*', '!');

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

        // Code is a little bit crappy, but has better performance
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
            if (isValidPosition(comment, i) && isHighlightTriggerChar(c) && containsHighlightToken(comment.substring(i))) {
                isHighlightedCurrentLine = true;
                isProcessedCurrentLine = true;
                currentLineHighlightAttribute = getHighlightTextAttribute(comment.substring(i));
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
        // Length and i checks is used to not fall in StringIndexOutOfBoundsException
        if (i > 0) {
            int length = comment.length();
            if (c == '!' && length >= 3) {
                // Skip "<!-" and shebang "#!/"
                return !((comment.charAt(i - 1) == '<' && comment.charAt(i + 1) == '-') || (comment.charAt(i - 1) == '#' && comment.charAt(i + 1) == '/'));
            } else if (c == '*') {

                // Skip "/*", "*/", "/**"
                return !(comment.charAt(i - 1) == '/' || (i >= 2 && comment.charAt(i - 2) == '/' && comment.charAt(i - 1) == '*') || ((i + 1) < length && comment.charAt(i + 1) == '/'));
            }
        }

        return true;
    }

    private static boolean isValidStartLineChar(char c) {
        return START_LINE_CHARACTERS_LIST.contains(c);
    }

    private static boolean isHighlightTriggerChar(char c) {
        final List<String> allCommentTokens = CommentTokenConfiguration.getInstance().getAllTokens();
        for (String token : allCommentTokens) {
            if (token.charAt(0) == c) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsHighlightToken(String commentSubstring) {
        final List<String> allCommentTokens = CommentTokenConfiguration.getInstance().getAllTokens();
        for (String token : allCommentTokens) {
            if (commentSubstring.startsWith(token)) {
                return true;
            }
        }
        return false;
    }


    private static TextAttributesKey getHighlightTextAttribute(String commentSubstring) {
        final List<String> allCommentTokens = CommentTokenConfiguration.getInstance().getAllTokens();
        for (String token : allCommentTokens) {
            if (commentSubstring.startsWith(token)) {
                return TextAttributesKey.createTextAttributesKey(getTextAttributeKeyByToken(token));
            }
        }
        return null;
    }

    @NotNull
    public static String getTextAttributeKeyByToken(String token) {
        return token + "_COMMENT";
    }
}
