package com.github.alaukiknpant.intellijviolatplugin.model;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpecificationInitializer {
    private static final Logger log = Logger.getInstance(SpecificationInitializer.class);
    private String path;

    public SpecificationInitializer() {}

    private SpecificationInitializer(String path) {
        setPath(path);
    }

    @Nullable
    public static SpecificationInitializer createSpecificationInitializer(String path) {
        SpecificationInitializer ii = new SpecificationInitializer(path);
        if(ii.confirm(path)) return ii;
        return null;
    }

    @Nullable
    public Boolean confirm(@NotNull String path) {
        if(path.endsWith(".json")) {
            return true;
        } else {
            return false;
        }
    }

    public void setPath(String path) {
        if(path.endsWith(".json")) this.path = path;
    }

}
