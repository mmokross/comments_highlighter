package com.clutcher.comments.settings;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.clutcher.comments.highlighter.HighlightTokenType;
import com.clutcher.comments.highlighter.TokenHighlighter;
import com.clutcher.comments.highlighter.impl.CommentHighlighter;
import com.clutcher.comments.highlighter.impl.KeywordHighlighter;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

public class CommentHighlighterColorSettingsPage implements ColorSettingsPage {

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new PlainSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return " ";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        HighlightTokenConfiguration highlightTokenConfiguration = ServiceManager.getService(HighlightTokenConfiguration.class);
        // ? Is it possible to get list of services by Interface?
        TokenHighlighter commentHighlighter = ServiceManager.getService(CommentHighlighter.class);
        TokenHighlighter keywordHighlighter = ServiceManager.getService(KeywordHighlighter.class);

        Stream<AttributesDescriptor> commentColorStream = highlightTokenConfiguration.getAllTokensByType(HighlightTokenType.COMMENT).stream()
                .map(token -> createAttributeDescriptor(token, commentHighlighter));

        Stream<AttributesDescriptor> keywordColorStream = highlightTokenConfiguration.getAllTokensByType(HighlightTokenType.KEYWORD).stream()
                .map(token -> createAttributeDescriptor(token, keywordHighlighter));

        return Stream.concat(commentColorStream, keywordColorStream).toArray(AttributesDescriptor[]::new);
    }

    @NotNull
    private AttributesDescriptor createAttributeDescriptor(String token, TokenHighlighter tokenHighlighter) {

        if (tokenHighlighter instanceof CommentHighlighter) {
            return new AttributesDescriptor("Comment//" + token, createTextAttributeKey(token, tokenHighlighter));
        } else if (tokenHighlighter instanceof KeywordHighlighter) {
            return new AttributesDescriptor("Keyword//" + token, createTextAttributeKey(token, tokenHighlighter));
        }

        return new AttributesDescriptor("Other//" + token, createTextAttributeKey(token, tokenHighlighter));
    }

    @NotNull
    private TextAttributesKey createTextAttributeKey(String token, TokenHighlighter tokenHighlighter) {
        return TextAttributesKey.createTextAttributesKey(tokenHighlighter.getTextAttributeKeyByToken(token));
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Comments/Keywords";
    }
}
