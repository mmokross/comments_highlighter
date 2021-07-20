package com.clutcher.comments.annotator.impl;

import com.clutcher.comments.annotator.AbstractKeywordHighlighterAnnotator;
import com.clutcher.comments.utils.AnnotatorUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class CSharpKeywordHighlighterAnnotator extends AbstractKeywordHighlighterAnnotator {

    private final Class<?> keywordTokenClazz;
    private final Class<?> dummyBlockClazz;

    public CSharpKeywordHighlighterAnnotator() {
        this.keywordTokenClazz = AnnotatorUtils.findClassByName("com.jetbrains.rider.ideaInterop.fileTypes.csharp.lexer.CSharpKeywordTokenNodeType");
        this.dummyBlockClazz = AnnotatorUtils.findClassByName("com.jetbrains.rider.ideaInterop.fileTypes.csharp.psi.CSharpDummyBlock");
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
        return dummyBlockClazz.isAssignableFrom(rootParent.getClass());
    }

}
