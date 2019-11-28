package com.clutcher.comments.settings;

import com.clutcher.comments.gui.CommentTokensConfigurationPanel;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class CommentHighlighterCustomTokensSettingsPage implements SearchableConfigurable {

    private CommentTokensConfigurationPanel myTokenSettingsPanel;

    @NotNull
    @Override
    public String getId() {
        return "settings.CommentHighlighterCustomTokensSettingsPage";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "settings.CommentHighlighterCustomTokensSettingsPage";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (myTokenSettingsPanel == null) {
            myTokenSettingsPanel = new CommentTokensConfigurationPanel();
        }
        return myTokenSettingsPanel;
    }

    @Override
    public boolean isModified() {
        return myTokenSettingsPanel.isModified();
    }

    @Override
    public void apply() {
        myTokenSettingsPanel.apply();
    }
}
