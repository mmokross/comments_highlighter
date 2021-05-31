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


@State(name = "HighlightTokenConfiguration", storages = @Storage("highlightTokenConfiguration-v1.xml"))
public class HighlightTokenConfiguration implements PersistentStateComponent<HighlightTokenConfiguration.State> {

    private static final Multimap<HighlightTokenType, String> INITIAL_TOKENS = new ImmutableMultimap.Builder<HighlightTokenType, String>()
            .putAll(HighlightTokenType.COMMENT, Arrays.asList("!", "?", "*"))
            .putAll(HighlightTokenType.KEYWORD, "public")
            .build();

    State currentState;

    static class State {
        public Map<HighlightTokenType, Collection<String>> highlightTokenMap;
    }

    public Collection<String> getAllTokensByType(Collection<HighlightTokenType> tokenTypes) {
        ImmutableSet.Builder<String> setBuilder = ImmutableSet.builder();
        for (HighlightTokenType tokenType : tokenTypes) {
            addTokenByType(tokenType, setBuilder);
        }
        return setBuilder.build();
    }

    public Collection<String> getAllTokensByType(HighlightTokenType type) {
        ImmutableSet.Builder<String> setBuilder = ImmutableSet.builder();
        addTokenByType(type, setBuilder);
        return setBuilder.build();
    }

    public Map<HighlightTokenType, Collection<String>> getAllTokens() {
        return Collections.unmodifiableMap(currentState.highlightTokenMap);
    }

    public void setCustomTokens(Map<HighlightTokenType, List<String>> updatedTokens) {
        for (Map.Entry<HighlightTokenType, List<String>> entry : updatedTokens.entrySet()) {
            currentState.highlightTokenMap.put(entry.getKey(), entry.getValue());
        }
    }

    private void addTokenByType(HighlightTokenType type, ImmutableSet.Builder<String> setBuilder) {
        Collection<String> tokens = currentState.highlightTokenMap.get(type);
        if (tokens != null) {
            setBuilder.addAll(tokens);
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
    @SuppressWarnings("java:S1640")
    public void noStateLoaded() {
        // ! We can't use EnumMap here, because Intellij IDEA can't serialize it.
        Map<HighlightTokenType, Collection<String>> initialHighlightMap = new HashMap<>();
        for (HighlightTokenType tokenType : HighlightTokenType.values()) {
            Collection<String> tokens = INITIAL_TOKENS.get(tokenType);
            if (tokens != null) {
                initialHighlightMap.put(tokenType, new ArrayList<>(tokens));
            } else {
                initialHighlightMap.put(tokenType, new ArrayList<>());
            }
        }

        currentState = new State();
        currentState.highlightTokenMap = initialHighlightMap;
    }

    @Override
    public void initializeComponent() {
        if (currentState == null || currentState.highlightTokenMap == null) {
            noStateLoaded();
        }
    }

}
