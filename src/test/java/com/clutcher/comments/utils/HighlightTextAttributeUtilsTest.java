package com.clutcher.comments.utils;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.junit.Assert;
import org.junit.Test;

public class HighlightTextAttributeUtilsTest {

    /**
     * Tests for one line comments
     */

    @Test
    public void shouldFindOneLineInfoComment() {
        // given
        String infoComment = " //  * testing string";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(infoComment, false);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.INFO_COMMENT, highlightTextAttribute);
    }

    @Test
    public void shouldFindOneLineWarnComment() {
        // given
        String warnComment = " //  ?   ANOTHER TESTING STRING  ";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(warnComment, false);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.WARN_COMMENT, highlightTextAttribute);
    }


    @Test
    public void shouldFindOneLineErrorComment() {
        // given
        String errorComment = " //!12232132131";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(errorComment, false);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.ERROR_COMMENT, highlightTextAttribute);
    }


    @Test
    public void shouldFindNoComment() {
        // given
        String noComment = "//1!2232132131";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(noComment, false);

        // then
        Assert.assertNull("Wrong text attribute assigned", highlightTextAttribute);
    }


    /**
     * Tests for multiline comments
     */

    @Test
    public void shouldFindInfoCommentInMultilineComment() {
        // given
        String infoComment = "   * testing string";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(infoComment, false);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.INFO_COMMENT, highlightTextAttribute);
    }

    @Test
    public void shouldFindWarnCommentInMultilineComment() {
        // given
        String warnComment = "   ?   ANOTHER TESTING STRING  ";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(warnComment, false);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.WARN_COMMENT, highlightTextAttribute);
    }


    @Test
    public void shouldFindErrorCommentInMultilineComment() {
        // given
        String errorComment = "   !  12232132131";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(errorComment, false);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.ERROR_COMMENT, highlightTextAttribute);
    }

    @Test
    public void shouldFindNothingInMultilineCommentStart() {
        // given
        String noComment = "/*";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(noComment, false);

        // then
        Assert.assertNull("Wrong text attribute assigned", highlightTextAttribute);
    }

    /**
     * Tests for doc multiline comments
     */

    @Test
    public void shouldFindNothingForDocCommentStart() {
        // given
        String noComment = "/**";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(noComment, true);

        // then
        Assert.assertNull("Wrong text attribute assigned", highlightTextAttribute);
    }

    @Test
    public void shouldFindNothingForDocCommentEnd() {
        // given
        String noComment = "*/";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(noComment, true);

        // then
        Assert.assertNull("Wrong text attribute assigned", highlightTextAttribute);
    }


    @Test
    public void shouldFindInfoCommentInDocLineComment() {
        // given
        String infoComment = "   *  * testing string";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(infoComment, true);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.INFO_COMMENT, highlightTextAttribute);
    }

    @Test
    public void shouldFindWarnCommentInDocLineComment() {
        // given
        String warnComment = "*   ?   ANOTHER TESTING STRING  ";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(warnComment, true);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.WARN_COMMENT, highlightTextAttribute);
    }


    @Test
    public void shouldFindErrorCommentInDocLineComment() {
        // given
        String errorComment = "  * !  12232132131";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(errorComment, true);

        // then
        Assert.assertEquals("Wrong text attribute assigned", HighlightTextAttributeUtils.ERROR_COMMENT, highlightTextAttribute);
    }

    @Test
    public void shouldFindNothingInDocLineComment() {
        // given
        String noComment = " *  1 more comment";

        // when
        TextAttributesKey highlightTextAttribute = HighlightTextAttributeUtils.getHighlightTextAttribute(noComment, true);

        // then
        Assert.assertNull("Wrong text attribute assigned", highlightTextAttribute);
    }


}
