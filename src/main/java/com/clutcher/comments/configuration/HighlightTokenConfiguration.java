package com.clutcher.comments.configuration;

import com.clutcher.comments.highlighter.HighlightTokenType;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@State(name = "HighlightTokenConfiguration", storages = @Storage("customHighlightTokens.xml"))
public class HighlightTokenConfiguration implements PersistentStateComponent<HighlightTokenConfiguration.State> {

    private static final Multimap<HighlightTokenType, String> DEFAULT_HIGHLIGHT_TOKEN_MAP = new ImmutableMultimap.Builder<HighlightTokenType, String>()
            .putAll(HighlightTokenType.COMMENT, Arrays.asList("!", "?", "*"))
            .putAll(HighlightTokenType.KEYWORD, "public")
            .build();

    State currentState;

    public Collection<String> getAllTokensByType(Collection<HighlightTokenType> tokenTypes) {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (HighlightTokenType tokenType : tokenTypes) {
            builder.addAll(currentState.customHighlightTokenMap.get(tokenType));
            builder.addAll(DEFAULT_HIGHLIGHT_TOKEN_MAP.get(tokenType));
        }
        return builder.build();
    }

    public Collection<String> getAllTokensByType(HighlightTokenType type) {
        return ImmutableSet.<String>builder()
                .addAll(currentState.customHighlightTokenMap.get(type))
                .addAll(DEFAULT_HIGHLIGHT_TOKEN_MAP.get(type))
                .build();
    }

    public Map<HighlightTokenType, Collection<String>> getCustomTokens() {
        return Collections.unmodifiableMap(currentState.customHighlightTokenMap);
    }

    public void setCustomTokens(Map<HighlightTokenType, List<String>> updatedTokens) {
        for (Map.Entry<HighlightTokenType, List<String>> entry : updatedTokens.entrySet()) {
            currentState.customHighlightTokenMap.put(entry.getKey(), entry.getValue());
        }
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
        Map<HighlightTokenType, Collection<String>> initialHighlightMap = new HashMap<>();
        initialHighlightMap.put(HighlightTokenType.COMMENT, new ArrayList<>());
        initialHighlightMap.put(HighlightTokenType.KEYWORD, new ArrayList<>());

        currentState = new State();
        currentState.customHighlightTokenMap = initialHighlightMap;
    }

    static class State {
        public Map<HighlightTokenType, Collection<String>> customHighlightTokenMap;
    }
}
