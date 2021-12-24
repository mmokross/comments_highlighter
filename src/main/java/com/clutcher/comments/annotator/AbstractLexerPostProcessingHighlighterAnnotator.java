package com.clutcher.comments.annotator;

import com.clutcher.comments.highlighter.HighlightTokenType;
import com.clutcher.comments.highlighter.impl.CommentHighlighter;
import com.clutcher.comments.highlighter.impl.KeywordHighlighter;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter;
import com.intellij.openapi.editor.ex.util.SegmentArrayWithData;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLexerPostProcessingHighlighterAnnotator implements Annotator {

    private final CommentHighlighter commentHighlighter = ServiceManager.getService(CommentHighlighter.class);

    private final KeywordHighlighter keywordHighlighter = ServiceManager.getService(KeywordHighlighter.class);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (isElementForHighlight(element)) {
            Document document = element.getContainingFile().getViewProvider().getDocument();
            if (document == null) {
                return;
            }

            SegmentArrayWithData segments = extractLexerSegmentsFromDocument(document);
            if (segments == null) {
                return;
            }

            List<Pair<TextRange, TextAttributesKey>> highlights = getHighlights(document, segments);

            for (Pair<TextRange, TextAttributesKey> highlight : highlights) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                      .range(highlight.first)
                      .textAttributes(highlight.second)
                      .create();
            }
        }
    }

    @NotNull
    private List<Pair<TextRange, TextAttributesKey>> getHighlights(Document document, SegmentArrayWithData segments) {
        CharSequence documentText = document.getImmutableCharSequence();

        List<Pair<TextRange, TextAttributesKey>> allHighlights = new ArrayList<>();
        int amountOfSegments = segments.getSegmentCount();
        for (int i = 0; i < amountOfSegments; i++) {
            IElementType token = segments.unpackTokenFromData(segments.getSegmentData(i));
            if (isCommentToken(token)) {
                CharSequence tokenText = documentText.subSequence(segments.getSegmentStart(i), segments.getSegmentEnd(i));

                List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(tokenText.toString(), segments.getSegmentStart(i));
                allHighlights.addAll(highlights);
            } else if (isKeywordToken(token)) {
                CharSequence tokenText = documentText.subSequence(segments.getSegmentStart(i), segments.getSegmentEnd(i));

                List<Pair<TextRange, TextAttributesKey>> keywordHighlights = keywordHighlighter.getHighlights(HighlightTokenType.KEYWORD, tokenText.toString(), segments.getSegmentStart(i));
                if (isMethodKeywordToken(token)) {
                    List<Pair<TextRange, TextAttributesKey>> methodKeywordHighlights = keywordHighlighter.getHighlights(HighlightTokenType.METHOD_KEYWORD, tokenText.toString(), segments.getSegmentStart(i));
                    allHighlights.addAll(methodKeywordHighlights);
                }

                allHighlights.addAll(keywordHighlights);
            }
        }
        return allHighlights;
    }

    private SegmentArrayWithData extractLexerSegmentsFromDocument(Document document) {
        try {
            DocumentListener[] listeners = (DocumentListener[]) MethodUtils.invokeMethod(document, true, "getListeners");

            for (DocumentListener listener : listeners) {
                if (listener instanceof LexerEditorHighlighter) {
                    return ((LexerEditorHighlighter) listener).getSegments();
                }
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }

        return null;
    }

    protected abstract boolean isElementForHighlight(PsiElement element);

    protected abstract boolean isKeywordToken(IElementType token);

    protected abstract boolean isCommentToken(IElementType token);

    protected abstract boolean isMethodKeywordToken(IElementType token);

}
