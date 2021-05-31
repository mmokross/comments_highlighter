package com.clutcher.comments.annotator;

import com.clutcher.comments.highlighter.HighlightTokenType;
import com.clutcher.comments.highlighter.impl.KeywordHighlighter;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiKeyword;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JavaKeywordHighlighterAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiKeyword) {
            final String keyword = element.getText();
            int startOffset = element.getTextRange().getStartOffset();

            List<Pair<TextRange, TextAttributesKey>> highlights = getKeywordHighlights(element, keyword, startOffset);

            for (Pair<TextRange, TextAttributesKey> highlight : highlights) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(highlight.first)
                        .textAttributes(highlight.second)
                        .create();
            }
        }
    }

    private List<Pair<TextRange, TextAttributesKey>> getKeywordHighlights(@NotNull PsiElement element, String keyword, int startOffset) {
        var keywordHighlighter = ServiceManager.getService(KeywordHighlighter.class);

        List<Pair<TextRange, TextAttributesKey>> highlights = new ArrayList<>();
        highlights.addAll(keywordHighlighter.getHighlights(HighlightTokenType.KEYWORD, keyword, startOffset));

        if (isMethodAccessModifierKeyword(element)) {
            highlights.addAll(keywordHighlighter.getHighlights(HighlightTokenType.METHOD_KEYWORD, keyword, startOffset));
        }
        return highlights;
    }

    private boolean isMethodAccessModifierKeyword(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent == null) {
            return false;
        }
        return parent.getParent() instanceof PsiMethod;
    }

}
