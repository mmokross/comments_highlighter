package com.clutcher.comments.highlighter.impl;

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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Tests for one line comments
 */
@RunWith(JUnit4.class)
public class OneLineCommentCommentHighlighterTestCase extends LightPlatformTestCase {

    private static final TextAttributesKey INFO_COMMENT = TextAttributesKey.createTextAttributesKey("*_COMMENT");
    private static final TextAttributesKey WARN_COMMENT = TextAttributesKey.createTextAttributesKey("?_COMMENT");
    private static final TextAttributesKey ERROR_COMMENT = TextAttributesKey.createTextAttributesKey("!_COMMENT");

    @InjectMocks
    private CommentHighlighter commentHighlighter;

    @Before
    public void testSetup() {
        MockitoAnnotations.openMocks(this);

        TestApplicationManager testApplicationManager = getApplication();
        Assert.assertNotNull("ServiceManager is not available for HighlightTokenConfiguration.class.", testApplicationManager);
    }

    @Test
    public void shouldFindOneLineInfoComment() {
        // given
        String comment = " //  * testing string";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", INFO_COMMENT, highlights.get(0).second);
    }

    @Test
    public void shouldFindOneLineWarnComment() {
        // given
        String comment = " //  ?   ANOTHER TESTING STRING  ";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", WARN_COMMENT, highlights.get(0).second);
    }


    @Test
    public void shouldFindOneLineErrorComment() {
        // given
        String comment = " //!12232132131";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.get(0).second);
    }


    @Test
    public void shouldFindNoComment() {
        // given
        String comment = "//1!2232132131";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 0, highlights.size());
    }

    @Test
    public void shouldFindNoCommentInXMLComments() {
        // given
        String comment = "<!-- Empty comment -->";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 0, highlights.size());
    }


    /**
     * Tests for multiline comments
     */

    @Test
    public void shouldFindInfoCommentInMultilineComment() {
        // given
        String comment = "/*\n" +
                "* Info comment\n" +
                "*/";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", INFO_COMMENT, highlights.get(0).second);
    }

    @Test
    public void shouldFindWarnCommentInMultilineComment() {
        // given
        String comment = "/*\n" +
                "? Warn comment\n" +
                "*/";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", WARN_COMMENT, highlights.get(0).second);
    }


    @Test
    public void shouldFindErrorCommentInMultilineComment() {
        // given
        String comment = "/*\n" +
                " ! Error comment\n" +
                "*/";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.get(0).second);
    }


    /**
     * Tests for doc multiline comments
     */


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
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", INFO_COMMENT, highlights.get(0).second);

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
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", WARN_COMMENT, highlights.get(0).second);
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
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.get(0).second);
    }

    @Test
    public void shouldNotThrowStringIndexOutOfBoundsExceptionOnStarEdgeCase() {
        // given
        String comment = "* ";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
    }

    @Test
    public void shouldNotThrowStringIndexOutOfBoundsExceptionOnExclamationEdgeCase() {
        // given
        String comment = "! ";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
    }

}
