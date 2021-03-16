package com.github.alaukiknpant.intellijviolatplugin.pluginconfig;

import com.github.alaukiknpant.intellijviolatplugin.model.SpecificationInitializer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.github.alaukiknpant.intellijviolatplugin.model.ArtifactInitializer;
import com.github.alaukiknpant.intellijviolatplugin.model.ClassInitializer;
import com.github.alaukiknpant.intellijviolatplugin.model.ViolatInstallation;
import com.github.alaukiknpant.intellijviolatplugin.model.tester.Testers;

import java.util.ArrayList;
import java.util.List;

@State(name = "ViolatApplicationSettings", storages = {@Storage("$APP_CONFIG$/violat.xml")})
//@State(name = "InferApplicationSettings", storages = {@Storage("$APP_CONFIG$/infer.xml")})
public class GlobalSettings implements PersistentStateComponent<GlobalSettings> {

    @Property
    private List<ViolatInstallation> installations = new ArrayList<>();
    @Property
    private boolean showConsole = false;

    @Property
    private boolean showAllBugs = false;

    @Property
    private String pathToSpecs;

    @Property
    private String pathToArtifact;

    @Property
    private String pathToClass;

    @Property
    private String tester;

    @Property
    private List<Testers> availableTesters = Testers.getTesters();


    public void negateShowAllBugs() {
        this.showAllBugs = !this.showAllBugs;
        System.out.println("Show all bugs is now:" + this.showAllBugs);
    }

    public boolean isShowAllBugs() {
        return this.showAllBugs;

    }

    public boolean addTester(Testers tester) {
        boolean added = false;
        if (Testers.getTesters().contains(tester)) {
            this.tester = tester.getName();
            added = true;
        }
        return added;
    }

    public String getTester() {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        if(this.tester != null) {
            return tester;
        }
        return "";
    }

    // Related to adding JSON Specs
    public boolean addJsonSpecs(String path) {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        SpecificationInitializer spec = SpecificationInitializer.createSpecificationInitializer(path);
        System.out.println("path:" + path);

        if(spec != null) {
            pathToSpecs = path;
            return true;
        }
        return false;
    }

    public void clearJSONSpecs() {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        this.pathToSpecs = null;
    }

    public String getJsonSpecs() {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        if(this.pathToSpecs != null) {
            return pathToSpecs;
        }
        return "";
    }


    public String getArtifactSpecs() {
        //check if a JAR file exists in the given path and whether the JSON file is valid
        if(this.pathToArtifact != null) {
            return pathToArtifact;
        }
        return "";
    }

    public String getADTSpecs() {
        //check if a JAR file exists in the given path and whether the JSON file is valid
        if(this.pathToClass != null) {
            return pathToClass;
        }
        return "";
    }

    public String getPathToClass() {
        //check if a JAR file exists in the given path and whether the JSON file is valid
        if(this.pathToClass != null) {
            return pathToClass;
        }
        return "";
    }

    public void clearArtifacts() {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        this.pathToArtifact = null;
    }

    public void clearClass() {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        this.pathToClass = null;
    }

    public boolean addArtifact(String path) {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        ArtifactInitializer artifact = ArtifactInitializer.createArtifactInitializer(path);
        System.out.println("path:" + path);

        if(artifact != null) {
            pathToArtifact = path;
            return true;
        }
        return false;
    }

    public boolean addClass(String path) {
        //check if a JSON file exists in the given path and whether the JSON file is valid
        ClassInitializer cls = ClassInitializer.createClassInitializer(path);
        System.out.println("path:" + path);
        if(cls != null) {
            pathToClass = path;
            return true;
        }
        return false;
    }


    /**
     * Adds a ViolatInstallation to the global list, which is used by run configurations.
     * @param path The path of the installation
     * @param isDefault if the installation is a default installation
     * @return true, if the installation was added successfully, otherwise false
     */
    public boolean addInstallation(String path, boolean isDefault) {
        //check if a default installation already exists

        if(isDefault && this.getInstallations().stream().anyMatch(ViolatInstallation::isDefaultInstall)) return false;

        ViolatInstallation ii = ViolatInstallation.createViolatInstallation(path, isDefault);

        System.out.println("path:" + path);
        if(ii != null) {
            installations.add(ii);
            return true;
        }

        return false;
    }

    /**
     * Removes the given installation from the global list.
     * @param ii the given installation
     */
    public void removeInstallation(ViolatInstallation ii) {
        installations.remove(ii);
    }









    /**
     * Gets the default Installation
     * @return Default Installation. Returns null when there is none.
     */
    @Nullable
    public ViolatInstallation getDefaultInstallation() {
        return this.getInstallations().stream().filter(ViolatInstallation::isDefaultInstall).findFirst().orElse(null);
    }

    /**
     * Returns if at least one valid Installation exists
     * @return If a Installation exists, which is confirmed working
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasValidInstallation() {
        return this.getInstallations().stream().anyMatch(ViolatInstallation::isConfirmedWorking);
    }

    /**
     * Gets a valid Violat Installation
     * @return A valid Violat Installation, or null if there is none
     */
    @Nullable
    public ViolatInstallation getAnyValidInstallation() {
        return this.getInstallations().stream().filter(ViolatInstallation::isConfirmedWorking).findAny().orElse(null);
    }

    /**
     * Returns the Installation of Violat if given a path
     * @param path The given path
     * @return A Installation if it exists at that path, otherwise null
     */
    @Nullable
    public ViolatInstallation getInstallationFromPath(String path) {
        return this.getInstallations().stream().filter((x) -> x.getPath().equals(path)).findFirst().orElse(null);
    }

    public static GlobalSettings getInstance() {
        return ApplicationManager.getApplication().getComponent(GlobalSettings.class);
    }

    @Nullable
    @Override
    public GlobalSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @NotNull
    public List<ViolatInstallation> getInstallations() {
        return installations;
    }

    @NotNull
    public List<Testers> getAvailableTesters() {
        return availableTesters;
    }

    public void setInstallations(List<ViolatInstallation> installations) {
        this.installations = installations;
    }
    public boolean isShowConsole() {
        return showConsole;
    }
    public void setShowConsole(boolean showConsole) {
        this.showConsole = showConsole;
    }
}
