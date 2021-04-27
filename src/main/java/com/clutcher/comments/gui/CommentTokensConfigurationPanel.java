package com.clutcher.comments.gui;

import com.clutcher.comments.configuration.HighlightTokenConfiguration;
import com.intellij.CommonBundle;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.UIBundle;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.xml.util.XmlStringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class CommentTokensConfigurationPanel extends JPanel implements SearchableConfigurable, Configurable.NoScroll {
    private final JBTable tokenTable;
    private ListTableModel<Pair<HighlightTokenConfiguration.TokenType, String>> tableModel;

    private static final ColumnInfo<Pair<HighlightTokenConfiguration.TokenType, String>, String> TYPE_COLUMN = new ColumnInfo<Pair<HighlightTokenConfiguration.TokenType, String>, String>("Type") {
        @Override
        public @Nullable String valueOf(Pair<HighlightTokenConfiguration.TokenType, String> tokenTypeStringPair) {
            return tokenTypeStringPair.first.toString();
        }
    };
    private static final ColumnInfo<Pair<HighlightTokenConfiguration.TokenType, String>, String> NAME_COLUMN = new ColumnInfo<Pair<HighlightTokenConfiguration.TokenType, String>, String>("Token") {
        @Override
        public @Nullable String valueOf(Pair<HighlightTokenConfiguration.TokenType, String> tokenTypeStringPair) {
            return tokenTypeStringPair.second;
        }
    };

    private JLabel reopenLabel;

    public CommentTokensConfigurationPanel() {
        super(new BorderLayout());

        reopenLabel = new JLabel(XmlStringUtil.wrapInHtml("Reopen setting window to see new tokens on Color settings page (https://youtrack.jetbrains.com/issue/IDEA-226087)"));
        reopenLabel.setForeground(JBColor.RED);

        tokenTable = new JBTable();
        tokenTable.getEmptyText().setText("No tokens defined");
        reset();
        add(
                new JLabel(XmlStringUtil.wrapInHtml("Custom comment/keyword tokens would be used to highlight comments/keywords based on color settings")),
                BorderLayout.NORTH
        );

        add(
                ToolbarDecorator.createDecorator(tokenTable)
                        .setAddAction(button -> {
                            AddUpdateCommentTokenDialog dlg = new AddUpdateCommentTokenDialog();
                            dlg.setTitle("Add comment token");
                            if (dlg.showAndGet()) {
                                HighlightTokenConfiguration.TokenType tokenType = dlg.getCustomTokenType();
                                String tokenValue = dlg.getToken();

                                tableModel.addRow(Pair.create(tokenType, tokenValue));
                            }
                        })
                        .setRemoveAction(button -> {
                            int returnValue = Messages.showOkCancelDialog("Delete selected token?",
                                    UIBundle.message("delete.dialog.title"),
                                    ApplicationBundle.message("button.delete"),
                                    CommonBundle.getCancelButtonText(),
                                    Messages.getQuestionIcon());
                            if (returnValue == Messages.OK) {
                                tableModel.removeRow(tokenTable.getSelectedRow());
                            }
                        })
                        .setEditAction(button -> {
                            int selectedRow = tokenTable.getSelectedRow();
                            Pair<HighlightTokenConfiguration.TokenType, String> value = tableModel.getItem(selectedRow);

                            AddUpdateCommentTokenDialog dlg = new AddUpdateCommentTokenDialog();
                            dlg.setTitle("Edit comment token");
                            dlg.setCustomTokenType(value.first);
                            dlg.setToken(value.second);

                            if (dlg.showAndGet()) {
                                final HighlightTokenConfiguration.TokenType editedTokenType = dlg.getCustomTokenType();
                                final String editedToken = dlg.getToken();

                                tableModel.removeRow(selectedRow);
                                tableModel.insertRow(selectedRow, Pair.create(editedTokenType, editedToken));
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

        Map<HighlightTokenConfiguration.TokenType, List<String>> updatedTokens = getTokenMapFromModel(tableModel);

        HighlightTokenConfiguration tokenConfiguration = ServiceManager.getService(HighlightTokenConfiguration.class);
        tokenConfiguration.setCustomCommentTokens(updatedTokens.get(HighlightTokenConfiguration.TokenType.COMMENT));
        tokenConfiguration.setCustomKeywordTokens(updatedTokens.get(HighlightTokenConfiguration.TokenType.KEYWORD));


    }

    @Override
    public boolean isModified() {

        HighlightTokenConfiguration tokenConfiguration = ServiceManager.getService(HighlightTokenConfiguration.class);
        List<String> customCommentTokens = tokenConfiguration.getCustomCommentTokens();
        List<String> customKeywordTokens = tokenConfiguration.getCustomKeywordTokens();

        Map<HighlightTokenConfiguration.TokenType, List<String>> updatedTokens = getTokenMapFromModel(tableModel);

        return !customCommentTokens.equals(updatedTokens.get(HighlightTokenConfiguration.TokenType.COMMENT)) ||
                !customKeywordTokens.equals(updatedTokens.get(HighlightTokenConfiguration.TokenType.KEYWORD));
    }


    @Override
    public void reset() {
        tableModel = new ListTableModel<>(TYPE_COLUMN, NAME_COLUMN);

        List<String> customCommentTokens = ServiceManager.getService(HighlightTokenConfiguration.class).getCustomCommentTokens();
        for (String commentToken : customCommentTokens) {
            tableModel.addRow(Pair.create(HighlightTokenConfiguration.TokenType.COMMENT, commentToken));
        }

        List<String> customKeywordTokens = ServiceManager.getService(HighlightTokenConfiguration.class).getCustomKeywordTokens();
        for (String keywordToken : customKeywordTokens) {
            tableModel.addRow(Pair.create(HighlightTokenConfiguration.TokenType.KEYWORD, keywordToken));
        }
        tokenTable.setModel(tableModel);
    }

    private Map<HighlightTokenConfiguration.TokenType, List<String>> getTokenMapFromModel(ListTableModel<Pair<HighlightTokenConfiguration.TokenType, String>> tableModel) {
        Map<HighlightTokenConfiguration.TokenType, List<String>> tokenMap = new EnumMap<>(HighlightTokenConfiguration.TokenType.class);
        tokenMap.put(HighlightTokenConfiguration.TokenType.COMMENT, new ArrayList<>());
        tokenMap.put(HighlightTokenConfiguration.TokenType.KEYWORD, new ArrayList<>());

        for (Pair<HighlightTokenConfiguration.TokenType, String> item : tableModel.getItems()) {
            List<String> mapValueList = tokenMap.get(item.first);
            mapValueList.add(item.second);
        }

        return tokenMap;
    }

    @Override
    @Nls
    public String getDisplayName() {
        return "Custom comment/keyword tokens";
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
