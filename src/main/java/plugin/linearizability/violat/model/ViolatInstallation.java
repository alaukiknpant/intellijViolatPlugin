package plugin.linearizability.violat.model;


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


    /**
     * Creates a new Infer Installation and checks if its working.
     * @param path The path of the root directory of the infer installation.
     * @param isDefault if the installation which should be created is a default one
     * @return An infer installation, or null if its not valid/working.
     */
    @Nullable
    public static ViolatInstallation createViolatInstallation(String path, boolean isDefault) {
        ViolatInstallation ii = new ViolatInstallation(path, isDefault);
        if(ii.confirm()) return ii;
        return null;
    }

    /**
     * Checks if this Installation is valid and gets the version.
     * @return true, if there is a valid infer installation in {@link #path}
     */
    private boolean confirm() {
        this.version = checkViolat(this.getPath());
        if(this.version != null) setConfirmedWorking(true);

        log.info(String.format("Confirmed Violat Installation : %b Version: %s Path: %s Default: %b", confirmedWorking, (this.version == null ? "null" : this.version.toString()), path, defaultInstall));

        return confirmedWorking;
    }

    /**
     * Checks if the Infer Installation at the given path is valid.
     * @param path Full path to the infer binary
     * @return The Version if the installation is valid, otherwise null
     */
    @Nullable
    public ViolatVersion checkViolat(@NotNull String path) {
        try {
            Process inferProcess = new ProcessBuilder(path , "--version").start();
            System.out.println("In CheckViolat\n");
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inferProcess.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            reader.close();

            inferProcess.waitFor(PROCESS_TIMEOUT, TimeUnit.MILLISECONDS);

            System.out.println("In CheckViolat - Violat version: " + output);
            String strMain = output.toString();
            String[] arrSplit = strMain.split("\\.");
//            System.out.println(Integer.parseInt(arrSplit[0]));
//            System.out.println(arrSplit[1]);
//            System.out.println(arrSplit[2]);


            if (inferProcess.exitValue() == 0) {
                try {
                    int major = Integer.parseInt(arrSplit[0].trim());
                    int minor = Integer.parseInt(arrSplit[1].trim());
                    int patch = Integer.parseInt(arrSplit[2].trim());
                    return new ViolatVersion(major, minor, patch);


//                    return new Gson().fromJson(output.toString(), ViolatVersion.class);
                } catch(JsonSyntaxException ex) {
                    return null;
                }
            }
        } catch(IOException | IllegalThreadStateException ex) { //IllegalThreadStateException means the timeout elapsed without infer finishing returning the version
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
        //make sure that the path is to a binary, not the directory of infer
        if(path.endsWith("violat") || path.endsWith(".bat") || path.endsWith(".sh")) this.path = path; //Exception: .bat and .sh endings are used for testing purposes, they shouldn't be changed
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
