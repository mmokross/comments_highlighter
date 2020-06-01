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

/**
 * Tests for doc multiline comments
 */
@RunWith(JUnit4.class)
public class DocCommentHighlightTextAttributeUtilsTestCase extends LightPlatformTestCase {

    private static final TextAttributesKey INFO_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("*"));
    private static final TextAttributesKey WARN_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("?"));
    private static final TextAttributesKey ERROR_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("!"));

    @Before
    public void testSetup() {
        TestApplicationManager testApplicationManager = initApplication();
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

    @Test
    public void shouldNotThrowStringIndexOutOfBoundsExceptionOnStarEdgeCase() {
        // given
        String comment = "* ";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
    }

    @Test
    public void shouldNotThrowStringIndexOutOfBoundsExceptionOnExclamationEdgeCase() {
        // given
        String comment = "! ";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
    }

}
