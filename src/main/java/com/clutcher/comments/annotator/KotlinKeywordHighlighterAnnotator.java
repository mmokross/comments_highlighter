package com.clutcher.comments.annotator;

import com.clutcher.comments.utils.AnnotatorUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class KotlinKeywordHighlighterAnnotator extends AbstractKeywordHighlighterAnnotator {

    private final Class<?> keywordTokenClazz;
    private final Class<?> methodClazz;

    private KotlinKeywordHighlighterAnnotator() {
        this.keywordTokenClazz = AnnotatorUtils.findClassByName("org.jetbrains.kotlin.lexer.KtKeywordToken");
        this.methodClazz = AnnotatorUtils.findClassByName("org.jetbrains.kotlin.psi.KtFunction");
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
