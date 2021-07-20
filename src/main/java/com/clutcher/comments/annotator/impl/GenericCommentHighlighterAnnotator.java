package com.clutcher.comments.annotator.impl;

import com.clutcher.comments.annotator.AbstractCommentHighlighterAnnotator;
import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPlainText;
import org.jetbrains.annotations.NotNull;

public class GenericCommentHighlighterAnnotator extends AbstractCommentHighlighterAnnotator {

    @Override
    protected boolean isCommentHighlightingElement(@NotNull PsiElement element) {
        return isCommentType(element) || isPlainTextHighlight(element);
    }

    private boolean isPlainTextHighlight(@NotNull PsiElement element) {
        return element instanceof PsiPlainText && ServiceManager.getService(HighlightTokenConfiguration.class).isPlainTextFileHighlightEnabled();
    }

    private boolean isCommentType(@NotNull PsiElement element) {
        return element instanceof PsiComment;
    }

}
