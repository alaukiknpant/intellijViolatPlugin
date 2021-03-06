package com.github.alaukiknpant.intellijviolatplugin.model;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtifactInitializer {
    private static final Logger log = Logger.getInstance(SpecificationInitializer.class);
    private String path;

    public ArtifactInitializer() {}

    private ArtifactInitializer(String path) {
        setPath(path);
    }

    @Nullable
    public static ArtifactInitializer createArtifactInitializer(String path) {
        ArtifactInitializer ii = new ArtifactInitializer(path);
        if(ii.confirm(path)) return ii;
        return null;
    }

    @Nullable
    public Boolean confirm(@NotNull String path) {
        if(path.endsWith(".jar")) {
            return true;
        } else {
            return false;
        }
    }

    public void setPath(String path) {
        if(path.endsWith(".jar")) this.path = path;
    }
    public String getPath() {return this.path;}

}

