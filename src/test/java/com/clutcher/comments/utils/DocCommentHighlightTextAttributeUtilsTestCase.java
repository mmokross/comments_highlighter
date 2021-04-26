package com.clutcher.comments.utils;

import com.clutcher.comments.highlighter.impl.CommentHighlighter;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.testFramework.LightPlatformTestCase;
import com.intellij.testFramework.TestApplicationManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for doc multiline comments
 */
@RunWith(JUnit4.class)
public class DocCommentHighlightTextAttributeUtilsTestCase extends LightPlatformTestCase {

    private static final TextAttributesKey INFO_COMMENT = TextAttributesKey.createTextAttributesKey("*_COMMENT");
    private static final TextAttributesKey WARN_COMMENT = TextAttributesKey.createTextAttributesKey("?_COMMENT");
    private static final TextAttributesKey ERROR_COMMENT = TextAttributesKey.createTextAttributesKey("!_COMMENT");


    @Before
    public void testSetup() {
        TestApplicationManager testApplicationManager = getApplication();
        Assert.assertNotNull("ServiceManager is not available for CommentTokenConfiguration.class.", testApplicationManager);
    }


    @Test
    public void shouldFindInfoCommentInDocLineComment() {
        // given
        String comment = "/**\n" +
                " * Doc comment\n" +
                " *\n" +
                " * * Info comment\n" +
                " *\n" +
                "*/";

        // when

        // Result map from which annotation would be created
        Map<TextRange, TextAttributesKey> highlightAnnotationData = new HashMap<>();

        CommentHighlighter commentHighlighter = new CommentHighlighter();
        List<Pair<TextRange, TextAttributesKey>> highlights1 = commentHighlighter.getHighlights(comment, 0);
        for (Pair<TextRange, TextAttributesKey> highlight : highlights1) {
            highlightAnnotationData.put(highlight.first, highlight.second);
        }

        Map<TextRange, TextAttributesKey> highlights = highlightAnnotationData;

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", INFO_COMMENT, highlights.values().iterator().next());

    }

    @Test
    public void shouldFindWarnCommentInDocLineComment() {
        // given
        String comment = "/**\n" +
                " * Doc comment\n" +
                " *\n" +
                " * ? Warn comment\n" +
                " *\n" +
                "*/";

        // when

        // Result map from which annotation would be created
        Map<TextRange, TextAttributesKey> highlightAnnotationData = new HashMap<>();

        CommentHighlighter commentHighlighter = new CommentHighlighter();
        List<Pair<TextRange, TextAttributesKey>> highlights1 = commentHighlighter.getHighlights(comment, 0);
        for (Pair<TextRange, TextAttributesKey> highlight : highlights1) {
            highlightAnnotationData.put(highlight.first, highlight.second);
        }

        Map<TextRange, TextAttributesKey> highlights = highlightAnnotationData;

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", WARN_COMMENT, highlights.values().iterator().next());
    }


    @Test
    public void shouldFindErrorCommentInDocLineComment() {
        // given
        String comment = "/**\n" +
                " * Doc comment\n" +
                " *\n" +
                " * ! Error comment\n" +
                " *\n" +
                "*/";

        // when

        // Result map from which annotation would be created
        Map<TextRange, TextAttributesKey> highlightAnnotationData = new HashMap<>();

        CommentHighlighter commentHighlighter = new CommentHighlighter();
        List<Pair<TextRange, TextAttributesKey>> highlights1 = commentHighlighter.getHighlights(comment, 0);
        for (Pair<TextRange, TextAttributesKey> highlight : highlights1) {
            highlightAnnotationData.put(highlight.first, highlight.second);
        }

        Map<TextRange, TextAttributesKey> highlights = highlightAnnotationData;

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.values().iterator().next());
    }

    @Test
    public void shouldNotThrowStringIndexOutOfBoundsExceptionOnStarEdgeCase() {
        // given
        String comment = "* ";

        // when

        // Result map from which annotation would be created
        Map<TextRange, TextAttributesKey> highlightAnnotationData = new HashMap<>();

        CommentHighlighter commentHighlighter = new CommentHighlighter();
        List<Pair<TextRange, TextAttributesKey>> highlights1 = commentHighlighter.getHighlights(comment, 0);
        for (Pair<TextRange, TextAttributesKey> highlight : highlights1) {
            highlightAnnotationData.put(highlight.first, highlight.second);
        }

        Map<TextRange, TextAttributesKey> highlights = highlightAnnotationData;

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
    }

    @Test
    public void shouldNotThrowStringIndexOutOfBoundsExceptionOnExclamationEdgeCase() {
        // given
        String comment = "! ";

        // when

        // Result map from which annotation would be created
        Map<TextRange, TextAttributesKey> highlightAnnotationData = new HashMap<>();

        CommentHighlighter commentHighlighter = new CommentHighlighter();
        List<Pair<TextRange, TextAttributesKey>> highlights1 = commentHighlighter.getHighlights(comment, 0);
        for (Pair<TextRange, TextAttributesKey> highlight : highlights1) {
            highlightAnnotationData.put(highlight.first, highlight.second);
        }

        Map<TextRange, TextAttributesKey> highlights = highlightAnnotationData;

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
    }

}
