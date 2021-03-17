package com.github.alaukiknpant.intellijviolatplugin.model;


import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class ViolatInstallation implements Serializable {
    private static final Logger log = Logger.getInstance(ViolatInstallation.class);
    private static final long PROCESS_TIMEOUT = 500; //How long the check waits before it declares the binary the process started from as invalid (in ms)

    private String path = "violat";
    private ViolatVersion version;
    private boolean confirmedWorking;
    private boolean defaultInstall = false;

    public ViolatInstallation() {}

    private ViolatInstallation(String path, boolean defaultInstall) {
        setPath(path);
        setDefaultInstall(defaultInstall);
    }

    @Nullable
    public static ViolatInstallation createViolatInstallation(String path, boolean isDefault) {
        ViolatInstallation ii = new ViolatInstallation(path, isDefault);
        if(ii.confirm()) return ii;
        return null;
    }

    private boolean confirm() {
        this.version = checkViolat(this.getPath());
        if(this.version != null) setConfirmedWorking(true);

        log.info(String.format("Confirmed Violat Installation : %b Version: %s Path: %s Default: %b", confirmedWorking, (this.version == null ? "null" : this.version.toString()), path, defaultInstall));

        return confirmedWorking;
    }

    @Nullable
    public ViolatVersion checkViolat(@NotNull String path) {
        try {
            Process violatVersion = new ProcessBuilder(path , "--version").start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(violatVersion.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            reader.close();

            violatVersion.waitFor(PROCESS_TIMEOUT, TimeUnit.MILLISECONDS);

            String strMain = output.toString();
            String[] arrSplit = strMain.split("\\.");

            if (violatVersion.exitValue() == 0) {
                try {
                    int major = Integer.parseInt(arrSplit[0].trim());
                    int minor = Integer.parseInt(arrSplit[1].trim());
                    int patch = Integer.parseInt(arrSplit[2].trim());
                    return new ViolatVersion(major, minor, patch);

                } catch(JsonSyntaxException ex) {
                    return null;
                }
            }
        } catch(IOException | IllegalThreadStateException ex) { //IllegalThreadStateException refers to Violat not returning the correct version
            return null;
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public String toString() {
        return (defaultInstall ? "[Default] " : "") + path + " " + "(" + version + ")";
    }
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        if(path.endsWith("violat")) this.path = path;
        else this.path = path + "/bin/violat";
    }

    public ViolatVersion getVersion() {
        return version;
    }
    public void setVersion(ViolatVersion version) {
        this.version = version;
    }
    public boolean isConfirmedWorking() {
        return confirmedWorking;
    }
    public void setConfirmedWorking(boolean confirmedWorking) {
        this.confirmedWorking = confirmedWorking;
    }
    public boolean isDefaultInstall() {
        return defaultInstall;
    }
    public void setDefaultInstall(boolean defaultInstall) {
        this.defaultInstall = defaultInstall;
    }
}
