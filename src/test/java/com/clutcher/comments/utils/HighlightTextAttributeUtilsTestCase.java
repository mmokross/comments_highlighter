package com.clutcher.comments.utils;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.testFramework.LightPlatformTestCase;
import com.intellij.testFramework.TestApplicationManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;

@RunWith(JUnit4.class)
public class HighlightTextAttributeUtilsTestCase extends LightPlatformTestCase {

    private static final TextAttributesKey INFO_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("*"));
    private static final TextAttributesKey WARN_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("?"));
    private static final TextAttributesKey ERROR_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("!"));

    @Before
    public void testSetup() {
        TestApplicationManager testApplicationManager = initApplication();
        Assert.assertNotNull("ServiceManager is not available for CommentTokenConfiguration.class.", testApplicationManager);
    }

    /**
     * Tests for one line comments
     */

    @Test
    public void shouldFindOneLineInfoComment() {
        // given
        String comment = " //  * testing string";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", INFO_COMMENT, highlights.values().iterator().next());
    }

    @Test
    public void shouldFindOneLineWarnComment() {
        // given
        String comment = " //  ?   ANOTHER TESTING STRING  ";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", WARN_COMMENT, highlights.values().iterator().next());
    }


    @Test
    public void shouldFindOneLineErrorComment() {
        // given
        String comment = " //!12232132131";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.values().iterator().next());
    }


    @Test
    public void shouldFindNoComment() {
        // given
        String comment = "//1!2232132131";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 0, highlights.size());
    }

    @Test
    public void shouldFindNoCommentInXMLComments() {
        // given
        String comment = "<!-- Empty comment -->";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

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
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", INFO_COMMENT, highlights.values().iterator().next());
    }

    @Test
    public void shouldFindWarnCommentInMultilineComment() {
        // given
        String comment = "/*\n" +
                "? Warn comment\n" +
                "*/";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", WARN_COMMENT, highlights.values().iterator().next());
    }


    @Test
    public void shouldFindErrorCommentInMultilineComment() {
        // given
        String comment = "/*\n" +
                " ! Error comment\n" +
                "*/";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.values().iterator().next());
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
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

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
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

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
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.values().iterator().next());
    }

}
