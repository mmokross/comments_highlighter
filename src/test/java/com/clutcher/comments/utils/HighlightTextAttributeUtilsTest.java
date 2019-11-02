package com.clutcher.comments.utils;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.testFramework.LightPlatformTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class HighlightTextAttributeUtilsTest extends LightPlatformTestCase {

    private static final TextAttributesKey INFO_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("*"));
    private static final TextAttributesKey WARN_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("?"));
    private static final TextAttributesKey ERROR_COMMENT = TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken("!"));

    /**
     * Tests for one line comments
     */

    @Test
    public void testFindOneLineInfoComment() {
        // given
        String comment = " //  * testing string";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", INFO_COMMENT, highlights.values().iterator().next());
    }

    @Test
    public void testFindOneLineWarnComment() {
        // given
        String comment = " //  ?   ANOTHER TESTING STRING  ";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", WARN_COMMENT, highlights.values().iterator().next());
    }


    @Test
    public void testFindOneLineErrorComment() {
        // given
        String comment = " //!12232132131";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", ERROR_COMMENT, highlights.values().iterator().next());
    }


    @Test
    public void testFindNoComment() {
        // given
        String comment = "//1!2232132131";

        // when
        Map<TextRange, TextAttributesKey> highlights = HighlightTextAttributeUtils.getCommentHighlights(comment, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 0, highlights.size());
    }

    @Test
    public void testFindNoCommentInXMLComments() {
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
    public void testFindInfoCommentInMultilineComment() {
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
    public void testFindWarnCommentInMultilineComment() {
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
    public void testFindErrorCommentInMultilineComment() {
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
    public void testFindInfoCommentInDocLineComment() {
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
    public void testFindWarnCommentInDocLineComment() {
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
    public void testFindErrorCommentInDocLineComment() {
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
