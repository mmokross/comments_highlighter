package com.clutcher.comments.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@State(name = "HighlightTokenConfiguration", storages = @Storage("customHighlightTokens.xml"))
public class HighlightTokenConfiguration implements PersistentStateComponent<HighlightTokenConfiguration.State> {

    private static final List<String> DEFAULT_COMMENT_TOKENS = Collections.unmodifiableList(Arrays.asList("!", "?", "*"));
    private static final List<String> DEFAULT_KEYWORD_TOKENS = Collections.singletonList("public");

    State currentState;

    public enum TokenType {COMMENT, KEYWORD}

    static class State {
        public Map<TokenType, List<String>> customHighlightTokenMap;
    }

    public List<String> getAllKeywordTokens() {
        final ArrayList<String> tokens = new ArrayList<>(DEFAULT_KEYWORD_TOKENS);

        List<String> customKeywordTokens = currentState.customHighlightTokenMap.get(TokenType.KEYWORD);
        if (customKeywordTokens != null) {
            tokens.addAll(customKeywordTokens);
        }
        return tokens;
    }

    public List<String> getAllCommentTokens() {
        final ArrayList<String> tokens = new ArrayList<>(DEFAULT_COMMENT_TOKENS);

        List<String> customCommentTokens = currentState.customHighlightTokenMap.get(TokenType.COMMENT);
        if (customCommentTokens != null) {
            tokens.addAll(customCommentTokens);
        }
        return tokens;
    }

    public List<String> getCustomCommentTokens() {
        List<String> customTokens = currentState.customHighlightTokenMap.get(TokenType.COMMENT);
        if (customTokens == null) {
            return Collections.emptyList();
        }
        return customTokens;
    }

    public void setCustomCommentTokens(final List<String> tokens) {
        currentState.customHighlightTokenMap.put(TokenType.COMMENT, new ArrayList<>(tokens));
    }

    public void setCustomKeywordTokens(final List<String> tokens) {
        currentState.customHighlightTokenMap.put(TokenType.KEYWORD, new ArrayList<>(tokens));
    }

    @Override
    public State getState() {
        return currentState;
    }

    @Override
    public void loadState(@NotNull State state) {
        currentState = state;
    }

    @Override
    public void noStateLoaded() {
        Map<TokenType, List<String>> initialHighlightMap = new HashMap<>();
        initialHighlightMap.put(TokenType.COMMENT, new ArrayList<>());
        initialHighlightMap.put(TokenType.KEYWORD, new ArrayList<>());

        currentState = new State();
        currentState.customHighlightTokenMap = initialHighlightMap;
    }
}
