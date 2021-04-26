package com.clutcher.comments.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TokenHighlighter {

    List<Pair<TextRange, TextAttributesKey>> getHighlights(String text, int startOffset);

    @NotNull
    String getTextAttributeKeyByToken(String token);
}
