package com.clutcher.comments.settings;

import com.clutcher.comments.configuration.CommentTokenConfiguration;
import com.clutcher.comments.utils.HighlightTextAttributeUtils;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.List;
import java.util.Map;

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
        final List<String> tokens = CommentTokenConfiguration.getInstance().getAllTokens();
        final int size = tokens.size();
        AttributesDescriptor[] attributesDescriptors = new AttributesDescriptor[size];
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                final String token = tokens.get(i);
                attributesDescriptors[i] = new AttributesDescriptor(token, TextAttributesKey.createTextAttributesKey(HighlightTextAttributeUtils.getTextAttributeKeyByToken(token)));
            }
        }
        return attributesDescriptors;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Comments";
    }
}
