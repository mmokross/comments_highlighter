package com.clutcher.comments.annotator;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class JavaKeywordHighlighterAnnotator extends AbstractKeywordHighlighterAnnotator {

    private final Class<?> keywordTokenClazz;
    private final Class<?> methodClazz;

    private JavaKeywordHighlighterAnnotator() {
        this.keywordTokenClazz = findClassByName("com.intellij.psi.PsiKeyword");
        this.methodClazz = findClassByName("com.intellij.psi.PsiMethod");
    }


    @Override
    protected boolean isKeywordElement(@NotNull PsiElement element) {
        return keywordTokenClazz.isAssignableFrom(element.getClass());
    }

    @Override
    protected boolean isMethodAccessModifierKeyword(@NotNull PsiElement element) {
        PsiElement rootParent = getRootElement(element);
        if (rootParent == null) {
            return false;
        }
        return methodClazz.isAssignableFrom(rootParent.getClass());
    }

}
