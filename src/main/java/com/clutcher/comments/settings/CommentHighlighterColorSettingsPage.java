package com.clutcher.comments.settings;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.clutcher.comments.highlighter.impl.CommentHighlighter;
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
        List<String> tokens = ServiceManager.getService(HighlightTokenConfiguration.class).getAllCommentTokens();
        int size = tokens.size();

        AttributesDescriptor[] attributesDescriptors = new AttributesDescriptor[size];
        if (size > 0) {
            CommentHighlighter commentHighlighter = ServiceManager.getService(CommentHighlighter.class);
            for (int i = 0; i < size; i++) {
                String token = tokens.get(i);
                TextAttributesKey textAttributesKey = TextAttributesKey.createTextAttributesKey(commentHighlighter.getTextAttributeKeyByToken(token));

                attributesDescriptors[i] = new AttributesDescriptor(token, textAttributesKey);
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
