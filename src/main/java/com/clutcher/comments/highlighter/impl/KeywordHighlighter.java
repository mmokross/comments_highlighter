package com.clutcher.comments.highlighter.impl;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.clutcher.comments.highlighter.HighlightTokenType;
import com.clutcher.comments.highlighter.TokenHighlighter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeywordHighlighter implements TokenHighlighter {

    private static final HighlightTokenConfiguration tokenConfiguration = ApplicationManager.getApplication().getService(HighlightTokenConfiguration.class);

    @Override
    public List<Pair<TextRange, TextAttributesKey>> getHighlights(String text, int startOffset) {
        Collection<String> supportedTokens = tokenConfiguration.getAllTokensByType(getSupportedTokenTypes());

        var textRange = new TextRange(startOffset, startOffset + text.length());
        return createHighlightsForMatchingTokens(supportedTokens, text, textRange);
    }


    @Override
    public List<Pair<TextRange, TextAttributesKey>> getHighlights(HighlightTokenType tokenType, String text, int startOffset) {
        Collection<String> supportedTokens = tokenConfiguration.getAllTokensByType(tokenType);

        var textRange = new TextRange(startOffset, startOffset + text.length());
        return createHighlightsForMatchingTokens(supportedTokens, text, textRange);
    }

    @NotNull
    @Override
    public String getTextAttributeKeyByToken(String token) {
        return token + "_KEYWORD";
    }

    @Override
    public List<HighlightTokenType> getSupportedTokenTypes() {
        return List.of(HighlightTokenType.KEYWORD, HighlightTokenType.METHOD_KEYWORD);
    }

    private List<Pair<TextRange, TextAttributesKey>> createHighlightsForMatchingTokens(Collection<String> supportedTokens, String text, TextRange textRange) {
        return supportedTokens.stream()
                              .filter(token -> token.equalsIgnoreCase(text))
                              .map(token -> TextAttributesKey.createTextAttributesKey(getTextAttributeKeyByToken(token)))
                              .map(highlightAttribute -> Pair.create(textRange, highlightAttribute))
                              .collect(Collectors.toList());

    }
}
