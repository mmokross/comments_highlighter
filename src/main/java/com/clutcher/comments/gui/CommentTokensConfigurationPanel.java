package com.clutcher.comments.gui;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.intellij.CommonBundle;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.UIBundle;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.xml.util.XmlStringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;


public class CommentTokensConfigurationPanel extends JPanel implements SearchableConfigurable, Configurable.NoScroll {
    private final JBTable myTokenTable;
    private List<String> myTokens;
    private ListTableModel<String> myModel;

    private static final ColumnInfo<String, String> NAME_COLUMN = new ColumnInfo<String, String>("Token") {
        @Override
        public String valueOf(String token) {
            return token;
        }
    };

    private JLabel reopenLabel;

    public CommentTokensConfigurationPanel() {
        super(new BorderLayout());

        reopenLabel = new JLabel(XmlStringUtil.wrapInHtml("Reopen setting window to see new tokens on Color settings page (https://youtrack.jetbrains.com/issue/IDEA-226087)"));
        reopenLabel.setForeground(JBColor.RED);

        myTokenTable = new JBTable();
        myTokenTable.getEmptyText().setText("No tokens defined");
        reset();
        add(
                new JLabel(XmlStringUtil.wrapInHtml("Custom comment tokens would be used to highlight comments based on color settings")),
                BorderLayout.NORTH
        );

        add(
                ToolbarDecorator.createDecorator(myTokenTable)
                        .setAddAction(button -> {
                            AddUpdateCommentTokenDialog dlg = new AddUpdateCommentTokenDialog();
                            dlg.setTitle("Add comment token");
                            if (dlg.showAndGet()) {
                                myTokens.add(dlg.getToken());
                                myModel.fireTableDataChanged();
                            }
                        })
                        .setRemoveAction(button -> {
                            int returnValue = Messages.showOkCancelDialog("Delete selected token?",
                                    UIBundle.message("delete.dialog.title"),
                                    ApplicationBundle.message("button.delete"),
                                    CommonBundle.getCancelButtonText(),
                                    Messages.getQuestionIcon());
                            if (returnValue == Messages.OK) {
                                int selRow = myTokenTable.getSelectedRow();
                                myTokens.remove(selRow);
                                myModel.fireTableDataChanged();
                                if (myTokenTable.getRowCount() > 0) {
                                    if (selRow >= myTokenTable.getRowCount()) {
                                        selRow--;
                                    }
                                    myTokenTable.getSelectionModel().setSelectionInterval(selRow, selRow);
                                }
                            }
                        })
                        .setEditAction(button -> {
                            String token = myModel.getItem(myTokenTable.getSelectedRow());
                            AddUpdateCommentTokenDialog dlg = new AddUpdateCommentTokenDialog();
                            dlg.setTitle("Edit comment token");
                            dlg.setToken(token);
                            if (dlg.showAndGet()) {
                                final String editedToken = dlg.getToken();
                                myTokens.remove(token);
                                myTokens.add(editedToken);
                                myModel.fireTableDataChanged();
                            }
                        })
                        .setButtonComparator("Add", "Edit", "Remove")
                        .disableUpDownActions()
                        .createPanel(),
                BorderLayout.CENTER
        );
    }

    @Override
    public void apply() {
        add(reopenLabel, BorderLayout.SOUTH);
        ServiceManager.getService(HighlightTokenConfiguration.class).setCustomCommentTokens(myTokens);
    }

    @Override
    public boolean isModified() {
        return !myTokens.equals(ServiceManager.getService(HighlightTokenConfiguration.class).getCustomCommentTokens());
    }

    @Override
    public void reset() {
        myTokens = new ArrayList<>(ServiceManager.getService(HighlightTokenConfiguration.class).getCustomCommentTokens());
        myModel = new ListTableModel<>(new ColumnInfo[]{NAME_COLUMN,}, myTokens, 0);
        myTokenTable.setModel(myModel);
    }

    @Override
    @Nls
    public String getDisplayName() {
        return "Custom comment tokens";
    }

    @Override
    @NotNull
    public String getId() {
        return "comment.highlighter.token.configuration";
    }

    @Override
    public JComponent createComponent() {
        return this;
    }
}
