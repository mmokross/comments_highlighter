package com.clutcher.comments.annotator;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class PHPKeywordHighlighterAnnotator extends AbstractKeywordHighlighterAnnotator {

    private final Class<?> keywordTokenClazz;
    private final Class<?> methodClazz;

    private PHPKeywordHighlighterAnnotator() {
        this.keywordTokenClazz = findClassByName("com.jetbrains.php.lang.psi.PhpElementType");
        this.methodClazz = findClassByName("com.jetbrains.php.lang.psi.elements.impl.MethodImpl");
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
        PsiElement rootParent = getRootElement(element);
        if (rootParent == null) {
            return false;
        }
        return methodClazz.isAssignableFrom(rootParent.getClass());
    }

}
