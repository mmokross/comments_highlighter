package com.clutcher.comments.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import static com.clutcher.comments.utils.HighlightTextAttributeUtils.getHighlightTextAttribute;

public class CommentHighlighterAnnotator implements Annotator {

    private final static String NEW_LINE = System.getProperty("line.separator");
    private final static int NEW_LINE_LENGTH = NEW_LINE.length();


    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiComment) {

            String elementText = element.getText();
            boolean isMultiline = elementText.contains(NEW_LINE);
            if (isMultiline) {
                int startIndex = element.getTextRange().getStartOffset();
                int endIndex = element.getTextRange().getStartOffset();

                boolean isDocComment = isDocComment(elementText);
                String[] lines = elementText.split(NEW_LINE);
                for (String line : lines) {
                    endIndex += line.length() + NEW_LINE_LENGTH;
                    TextAttributesKey highlightAttribute = getHighlightTextAttribute(line, isDocComment);
                    if (highlightAttribute != null) {
                        TextRange textRange = new TextRange(startIndex + elementText.indexOf(line), endIndex);
                        Annotation annotation = holder.createInfoAnnotation(textRange, null);
                        annotation.setTextAttributes(highlightAttribute);
                    }

                }
            } else {
                TextAttributesKey highlightAttribute = getHighlightTextAttribute(elementText, false);
                if (highlightAttribute != null) {
                    Annotation annotation = holder.createInfoAnnotation(element, null);
                    annotation.setTextAttributes(highlightAttribute);
                }
            }
        }
    }

    private boolean isDocComment(String elementText) {
        return elementText.startsWith("/**");
    }


}
