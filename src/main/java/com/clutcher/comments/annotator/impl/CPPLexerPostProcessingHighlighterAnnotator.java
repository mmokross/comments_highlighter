package com.clutcher.comments.annotator.impl;

import com.clutcher.comments.annotator.AbstractLexerPostProcessingHighlighterAnnotator;
import com.clutcher.comments.utils.AnnotatorUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public class CPPLexerPostProcessingHighlighterAnnotator extends AbstractLexerPostProcessingHighlighterAnnotator {
    private final Class<?> highlightElementClazz;
    private final Class<?> tokenTypeClazz;

    public CPPLexerPostProcessingHighlighterAnnotator() {
        this.highlightElementClazz = AnnotatorUtils.findClassByName("com.jetbrains.rider.cpp.fileType.CppFile");
        this.tokenTypeClazz = AnnotatorUtils.findClassByName("com.jetbrains.rider.cpp.fileType.lexer.CppElementType");
    }

    @Override
    protected boolean isElementForHighlight(PsiElement element) {
        return highlightElementClazz.isAssignableFrom(element.getClass());
    }

    @Override
    protected boolean isCommentToken(IElementType token) {
        return tokenTypeClazz.isAssignableFrom(token.getClass()) && token.getDebugName().contains("COMMENT");
    }

    @Override
    protected boolean isKeywordToken(IElementType token) {
        return tokenTypeClazz.isAssignableFrom(token.getClass()) && token.getDebugName().contains("KEYWORD");
    }

    @Override
    protected boolean isMethodKeywordToken(IElementType token) {
        return false;
    }
}
