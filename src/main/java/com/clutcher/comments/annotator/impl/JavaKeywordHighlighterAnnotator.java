package com.clutcher.comments.annotator.impl;

import com.clutcher.comments.annotator.AbstractKeywordHighlighterAnnotator;
import com.clutcher.comments.utils.AnnotatorUtils;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class JavaKeywordHighlighterAnnotator extends AbstractKeywordHighlighterAnnotator {

    private final Class<?> keywordTokenClazz;
    private final Class<?> methodClazz;

    public JavaKeywordHighlighterAnnotator() {
        this.keywordTokenClazz = AnnotatorUtils.findClassByName("com.intellij.psi.PsiKeyword");
        this.methodClazz = AnnotatorUtils.findClassByName("com.intellij.psi.PsiMethod");
    }


    @Override
    protected boolean isKeywordElement(@NotNull PsiElement element) {
        return keywordTokenClazz.isAssignableFrom(element.getClass());
    }

    @Override
    protected boolean isMethodAccessModifierKeyword(@NotNull PsiElement element) {
        PsiElement rootParent = AnnotatorUtils.getRootElement(element);
        if (rootParent == null) {
            return false;
        }
        return methodClazz.isAssignableFrom(rootParent.getClass());
    }

}
