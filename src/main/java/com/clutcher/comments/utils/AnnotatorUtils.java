package com.clutcher.comments.utils;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotatorUtils {

    private AnnotatorUtils() {
        // ! Util class must not be instantiated!
    }

    @NotNull
    public static Class<?> findClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return Void.class;
        }
    }

    @Nullable
    public static PsiElement getRootElement(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent == null) {
            return null;
        }
        return parent.getParent();
    }
}
