package com.github.alaukiknpant.intellijviolatplugin.model;


import com.intellij.execution.ExecutionException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import com.github.alaukiknpant.intellijviolatplugin.pluginconfig.GlobalSettings;
import com.github.alaukiknpant.intellijviolatplugin.model.buildtool.BuildToolChecker;
import com.github.alaukiknpant.intellijviolatplugin.model.tester.Testers;

import java.util.List;

public class ViolatLaunchOptions {
    private static final Logger log = Logger.getInstance(ViolatLaunchOptions.class);

    //Tracks changed files for use with the reactive mode
    private FileDocumentManagerListener changeListener;
    private List<Testers> availableTesters;

    //Saved Violat Configuration Options
    private ViolatInstallation selectedInstallation;
    private List<Checker> selectedCheckers;
    private Testers selectedTester;
    private String backend;



    public ViolatLaunchOptions(Project project) {
        this.selectedInstallation = GlobalSettings.getInstance().getDefaultInstallation();
        this.selectedCheckers = Checker.getDefaultCheckers();
        this.availableTesters = Testers.getTesters();
        this.backend = GlobalSettings.getInstance().getTester();
    }

    /**
     * Returns Violats launch command
     */
    @NotNull
    public String buildViolatLaunchCmd(Project project) throws ExecutionException {
        if (this.backend == "JCStress") throw new ExecutionException("Violat Execution failed: JCStress not available yet");
        if(this.selectedInstallation == null) throw new ExecutionException("Violat Execution failed: No Installation selected");
        if(!BuildToolChecker.returnInvalidInstallations()) throw new ExecutionException("Violat Execution failed: You do not have all the software pre-reqs(Java, Maven, Gradle)");
        if(this.selectedCheckers == null || this.selectedCheckers.isEmpty()) throw new ExecutionException("Violat Execution failed: No Checkers selected");
        if(this.selectedCheckers.size() > 1) throw new ExecutionException("Violat Execution failed: More than 1 Checkers Selected");
        for (int i =0; i < selectedCheckers.size(); i++) {
            Checker checker = selectedCheckers.get(i);
            if (checker.isInProgress()) throw new ExecutionException("Violat Execution failed: HISTORIES feature not available yet");
        }
        StringBuilder sb = new StringBuilder(this.selectedInstallation.getPath());

        //Checkers
        for(Checker checker : selectedCheckers) {
            sb.append(checker.getActivationArgument()).append(" ");
        }

        //Specs
        final String pathToSpecs = GlobalSettings.getInstance().getJsonSpecs();
        if(pathToSpecs == null || pathToSpecs.isEmpty()) throw new ExecutionException("Violat Execution failed: Could not find the JSON specs");
        sb.append(pathToSpecs).append(" ");

        // JAR
        final String pathToArtifact = GlobalSettings.getInstance().getArtifactSpecs();

        if (!(pathToArtifact == null) && !pathToSpecs.isEmpty()) {
            sb.append("--jar").append(" ").append(pathToArtifact).append(" ");
        }

        // Tester
        final String selectedTester = GlobalSettings.getInstance().getTester();
        if(selectedTester == null || selectedTester.isEmpty()) throw new ExecutionException("Violat Execution failed: Tester not selected Correctly");
        if (!(selectedTester == null) && !selectedTester.isEmpty()) {
            sb.append("--tester").append(" ").append(selectedTester);
        }

        final Boolean isShowAllBugs = GlobalSettings.getInstance().isShowAllBugs();
        if (!isShowAllBugs) {
            sb.append(" ").append("--max-programs").append(" ").append("1");
        }


        String result = sb.toString() + " >&1 | tee result.txt";

        return result;
    }

    public List<Checker> getSelectedCheckers() {
        return selectedCheckers;
    }

    public void setSelectedCheckers(List<Checker> selectedCheckers) {
        this.selectedCheckers = selectedCheckers;
    }

    public void setSelectedTester(Testers selectedTester) {
        GlobalSettings.getInstance().addTester(selectedTester);
        this.selectedTester = selectedTester;
    }

    public Testers getSelectedTester() {
        return this.selectedTester;
    }

    public ViolatInstallation getSelectedInstallation() {
        return selectedInstallation;
    }

    public void setSelectedInstallation(ViolatInstallation selectedInstallation) {
        this.selectedInstallation = selectedInstallation;
    }

    public List<Testers> getAvailableTesters() {
        return availableTesters;
    }
}
