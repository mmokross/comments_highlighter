package com.clutcher.comments.annotator.impl;

import com.clutcher.comments.annotator.AbstractCommentHighlighterAnnotator;
import com.clutcher.comments.utils.AnnotatorUtils;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PHPDocCommentHighlighterAnnotator extends AbstractCommentHighlighterAnnotator {

    private final Class<?> phpDocCommentClazz;

    private static final String PHP_DOC_COMMENT_START = "/**";

    public PHPDocCommentHighlighterAnnotator() {
        this.phpDocCommentClazz = AnnotatorUtils.findClassByName("com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocToken");
    }

    @Override
    protected boolean isCommentHighlightingElement(@NotNull PsiElement element) {
        PsiElement firstChild = element.getFirstChild();
        if (firstChild == null) {
            return false;
        }
        boolean isPhpDocCommentElement = phpDocCommentClazz.isAssignableFrom(firstChild.getClass());

        if (isPhpDocCommentElement) {
            return PHP_DOC_COMMENT_START.equals(firstChild.getText());
        }

        return false;
    }

}
