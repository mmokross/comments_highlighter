package com.clutcher.comments.configuration;

import com.clutcher.comments.highlighter.HighlightTokenType;
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

    static class State {
        public Map<HighlightTokenType, List<String>> customHighlightTokenMap;
    }

    public List<String> getAllKeywordTokens() {
        final ArrayList<String> tokens = new ArrayList<>(DEFAULT_KEYWORD_TOKENS);

        List<String> customKeywordTokens = currentState.customHighlightTokenMap.get(HighlightTokenType.KEYWORD);
        if (customKeywordTokens != null) {
            tokens.addAll(customKeywordTokens);
        }
        return tokens;
    }

    public List<String> getCustomKeywordTokens() {
        List<String> customTokens = currentState.customHighlightTokenMap.get(HighlightTokenType.KEYWORD);
        if (customTokens == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(customTokens);
    }

    public List<String> getAllCommentTokens() {
        final ArrayList<String> tokens = new ArrayList<>(DEFAULT_COMMENT_TOKENS);

        List<String> customCommentTokens = currentState.customHighlightTokenMap.get(HighlightTokenType.COMMENT);
        if (customCommentTokens != null) {
            tokens.addAll(customCommentTokens);
        }
        return tokens;
    }

    public List<String> getCustomCommentTokens() {
        List<String> customTokens = currentState.customHighlightTokenMap.get(HighlightTokenType.COMMENT);
        if (customTokens == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(customTokens);
    }

    public void setCustomCommentTokens(final List<String> tokens) {
        currentState.customHighlightTokenMap.put(HighlightTokenType.COMMENT, new ArrayList<>(tokens));
    }

    public void setCustomKeywordTokens(final List<String> tokens) {
        currentState.customHighlightTokenMap.put(HighlightTokenType.KEYWORD, new ArrayList<>(tokens));
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
        Map<HighlightTokenType, List<String>> initialHighlightMap = new HashMap<>();
        initialHighlightMap.put(HighlightTokenType.COMMENT, new ArrayList<>());
        initialHighlightMap.put(HighlightTokenType.KEYWORD, new ArrayList<>());

        currentState = new State();
        currentState.customHighlightTokenMap = initialHighlightMap;
    }
}
