package com.clutcher.comments.annotator.impl;

import com.clutcher.comments.annotator.AbstractKeywordHighlighterAnnotator;
import com.clutcher.comments.utils.AnnotatorUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class CKeywordHighlighterAnnotator extends AbstractKeywordHighlighterAnnotator {

    private final Class<?> keywordTokenClazz;
    private final Class<?> methodClazz;

    public CKeywordHighlighterAnnotator() {
        this.keywordTokenClazz = AnnotatorUtils.findClassByName("com.jetbrains.cidr.lang.parser.OCKeywordElementType");
        this.methodClazz = AnnotatorUtils.findClassByName("com.jetbrains.cidr.lang.psi.OCTypeElement");
    }


    @Override
    protected boolean isKeywordElement(@NotNull PsiElement element) {
        if (element instanceof LeafPsiElement) {
            IElementType elementType = ((LeafPsiElement) element).getElementType();
            return keywordTokenClazz.isAssignableFrom(elementType.getClass());
        }
        return false;
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
