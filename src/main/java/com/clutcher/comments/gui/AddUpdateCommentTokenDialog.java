package com.clutcher.comments.gui;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

public class AddUpdateCommentTokenDialog extends DialogWrapper {

    private JTextField customTokenInput;
    private ComboBox<HighlightTokenConfiguration.TokenType> customTokenType;

    protected AddUpdateCommentTokenDialog() {
        super(false);

        this.customTokenInput = new JTextField("");
        this.customTokenInput.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                updateFeedback();
            }
        });

        this.customTokenType = new ComboBox<>(HighlightTokenConfiguration.TokenType.values());

        init();
    }

    private void updateFeedback() {
        setOKActionEnabled(true);
    }

    @Override
    @Nullable
    protected JComponent createCenterPanel() {
        JPanel jPanel = new JPanel();
        jPanel.add(this.customTokenType);
        jPanel.add(this.customTokenInput);
        return jPanel;
    }

    public HighlightTokenConfiguration.TokenType getCustomTokenType() {
        return customTokenType.getItem();
    }

    public void setCustomTokenType(HighlightTokenConfiguration.TokenType tokenType) {
        customTokenType.setItem(tokenType);
    }

    public String getToken() {
        return customTokenInput.getText();
    }

    public void setToken(final String commentToken) {
        customTokenInput.setText(commentToken);
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return customTokenInput;
    }

}
