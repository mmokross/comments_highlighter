package com.clutcher.comments.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@State(name = "CommentTokenConfiguration", storages = @Storage("commentTokens.xml"))
public class CommentTokenConfiguration implements PersistentStateComponent<CommentTokenConfiguration> {

    private static final List<String> DEFAULT_TOKENS = Collections.unmodifiableList(Arrays.asList("!", "?", "*"));

    private List<String> myCustomTokens = new ArrayList<>();

    public static CommentTokenConfiguration getInstance() {
        return ServiceManager.getService(CommentTokenConfiguration.class);
    }

    public List<String> getAllTokens() {
        final ArrayList<String> tokens = new ArrayList<>(DEFAULT_TOKENS);
        tokens.addAll(myCustomTokens);
        return tokens;
    }

    public List<String> getCustomTokens() {
        return myCustomTokens;
    }

    public void setCustomTokens(final List<String> tokens) {
        myCustomTokens = new ArrayList<>(tokens);
    }

    @Override
    public CommentTokenConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CommentTokenConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
