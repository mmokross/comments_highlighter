package com.clutcher.comments.highlighter.impl;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.clutcher.comments.highlighter.HighlightTokenType;
import com.clutcher.comments.highlighter.TokenHighlighter;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeywordHighlighter implements TokenHighlighter {

    @Override
    public List<Pair<TextRange, TextAttributesKey>> getHighlights(String text, int startOffset) {
        var textRange = new TextRange(startOffset, startOffset + text.length());

        HighlightTokenConfiguration tokenConfiguration = ServiceManager.getService(HighlightTokenConfiguration.class);
        Collection<String> supportedTokens = tokenConfiguration.getAllTokensByType(getSupportedTokenTypes());

        return supportedTokens.stream()
                .filter(token -> token.equalsIgnoreCase(text))
                .map(token -> TextAttributesKey.createTextAttributesKey(getTextAttributeKeyByToken(token)))
                .map(highlightAttribute -> Pair.create(textRange, highlightAttribute))
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public String getTextAttributeKeyByToken(String token) {
        return token + "_KEYWORD";
    }

    @Override
    public List<HighlightTokenType> getSupportedTokenTypes() {
        return Collections.singletonList(HighlightTokenType.KEYWORD);
    }
}
