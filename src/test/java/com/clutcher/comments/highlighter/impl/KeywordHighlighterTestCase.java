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

@RunWith(JUnit4.class)
public class KeywordHighlighterTestCase extends LightPlatformTestCase {

    private static final TextAttributesKey PUBLIC_KEYWORD = TextAttributesKey.createTextAttributesKey("public_KEYWORD");
    @InjectMocks
    private KeywordHighlighter keywordHighlighter;

    @Before
    public void testSetup() {
        MockitoAnnotations.openMocks(this);

        TestApplicationManager testApplicationManager = getApplication();
        Assert.assertNotNull("ServiceManager is not available for HighlightTokenConfiguration.class.", testApplicationManager);
    }


    @Test
    public void shouldFindPublicKeyword() {
        // given
        String keyword = "public";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = keywordHighlighter.getHighlights(keyword, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", PUBLIC_KEYWORD, highlights.get(0).second);
    }

    @Test
    public void shouldFindPublicKeywordIgnoringCases() {
        // given
        String keyword = "pUBlic";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = keywordHighlighter.getHighlights(keyword, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 1, highlights.size());
        Assert.assertEquals("Wrong highlight was assigned", PUBLIC_KEYWORD, highlights.get(0).second);
    }

    @Test
    public void shouldNotFindTestKeyword() {
        // given
        String keyword = "test";

        // when
        List<Pair<TextRange, TextAttributesKey>> highlights = keywordHighlighter.getHighlights(keyword, 0);

        // then
        Assert.assertEquals("Wrong number of highlights was found", 0, highlights.size());
    }

}