package com.clutcher.comments.gui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

public class AddUpdateCommentTokenDialog extends DialogWrapper {

    private JTextField myCommentTokenTextField;

    protected AddUpdateCommentTokenDialog() {
        super(false);

        this.myCommentTokenTextField = new JTextField("");
        this.myCommentTokenTextField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                updateFeedback();
            }
        });

        init();
    }

    private void updateFeedback() {
        setOKActionEnabled(true);
    }

    @Override
    @Nullable
    protected JComponent createCenterPanel() {
        return this.myCommentTokenTextField;
    }


    public String getToken() {
        return myCommentTokenTextField.getText();
    }

    public void setToken(final String commentToken) {
        myCommentTokenTextField.setText(commentToken);
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return myCommentTokenTextField;
    }

}
