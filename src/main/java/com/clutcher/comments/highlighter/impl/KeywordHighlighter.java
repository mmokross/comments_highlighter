package com.clutcher.comments.highlighter.impl;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.clutcher.comments.highlighter.TokenHighlighter;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordHighlighter implements TokenHighlighter {

    @Override
    public List<Pair<TextRange, TextAttributesKey>> getHighlights(String text, int startOffset) {
        // Result list of pairs from which annotation would be created
        List<Pair<TextRange, TextAttributesKey>> highlightAnnotationData = new ArrayList<>();

        List<String> keywordTokens = ServiceManager.getService(HighlightTokenConfiguration.class).getAllKeywordTokens();

        for (String token : keywordTokens) {
            if (token.equalsIgnoreCase(text)) {
                TextAttributesKey highlightAttribute = TextAttributesKey.createTextAttributesKey(getTextAttributeKeyByToken(token));
                highlightAnnotationData.add(Pair.create(new TextRange(startOffset, startOffset + text.length()), highlightAttribute));
            }
        }
        return highlightAnnotationData;
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
