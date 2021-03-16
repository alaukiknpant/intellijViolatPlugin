package com.github.alaukiknpant.intellijviolatplugin.runstateconfig;


import com.github.alaukiknpant.intellijviolatplugin.model.Checker;
import com.github.alaukiknpant.intellijviolatplugin.model.ViolatInstallation;
import com.github.alaukiknpant.intellijviolatplugin.model.ViolatLaunchOptions;
import com.github.alaukiknpant.intellijviolatplugin.model.buildtool.BuildToolChecker;
import com.github.alaukiknpant.intellijviolatplugin.model.tester.TesterChecker;
import com.github.alaukiknpant.intellijviolatplugin.pluginconfig.GlobalSettings;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.github.alaukiknpant.intellijviolatplugin.toolwindows.RunConfigurationEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViolatRunConfiguration extends RunConfigurationBase {
    private static final String PREFIX = "INTELLIJ_VIOLAT-";
    private static final String INSTALLATION = PREFIX + "INSTALLATION";
    private static final String CHECKERS = PREFIX + "CHECKERS";

    private ViolatLaunchOptions launchOptions;
    private Project project;


    ViolatRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        this.project = project;
        this.launchOptions = new ViolatLaunchOptions(project);


    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new RunConfigurationEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        List<Checker> selectedCheckers = launchOptions.getSelectedCheckers();

        if (GlobalSettings.getInstance().getTester() == "JCStress") throw new RuntimeConfigurationException("JCStress not available yet");
        if (!BuildToolChecker.returnInvalidInstallations()) throw new RuntimeConfigurationException("Java, maven or gradle might be missing");
        if (!TesterChecker.returnInvalidInstallations()) throw new RuntimeConfigurationException("Java Pathfinder not found in executable path");

        if(selectedCheckers == null || selectedCheckers.isEmpty()) throw new RuntimeConfigurationException("No Checker selected");
        if(launchOptions.getSelectedInstallation() == null || !launchOptions.getSelectedInstallation().isConfirmedWorking()) throw new RuntimeConfigurationException("No selected Installation or the Installation is invalid");

        if (selectedCheckers.size() > 1) throw new RuntimeConfigurationException("We can have a maximum of 1 Checker");
        for (int i =0; i < selectedCheckers.size(); i++) {
            Checker checker = selectedCheckers.get(i);
            if (checker.isInProgress()) throw new RuntimeConfigurationException("HISTORIES feature not available yet");
        }







    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) {
        return new ViolatRunState(this, executionEnvironment);
    }
    @Override
    public void readExternal(@NotNull Element element) throws InvalidDataException {
        super.readExternal(element);

        final ViolatInstallation installation = GlobalSettings.getInstance().getInstallationFromPath(JDOMExternalizerUtil.readField(element, INSTALLATION));
        if(installation != null) this.launchOptions.setSelectedInstallation(installation);

        final String checkerString = JDOMExternalizerUtil.readField(element, CHECKERS);
        if(checkerString != null) {
            List<Checker> newCheckerList = new ArrayList<>();
            Pattern p = Pattern.compile( "\\w+" );
            Matcher m = p.matcher(checkerString);
            while(m.find()) {
                newCheckerList.add(Checker.valueOf(m.group()));
            }
            this.launchOptions.setSelectedCheckers(newCheckerList);
        }
    }

    @Override
    public void writeExternal(@NotNull Element element) throws WriteExternalException {
        super.writeExternal(element);
        if(this.launchOptions.getSelectedInstallation() != null) JDOMExternalizerUtil.writeField(element, INSTALLATION, this.launchOptions.getSelectedInstallation().getPath());
        StringBuilder sb = new StringBuilder();
        for(Checker checker : this.launchOptions.getSelectedCheckers()) {
            sb.append(checker.getName()).append(" ");
        }
        JDOMExternalizerUtil.writeField(element, CHECKERS, sb.toString());
    }

    @NotNull
    String getViolatLaunchCmd() throws ExecutionException {
        return this.launchOptions.buildViolatLaunchCmd(this.project);
    }

    public ViolatLaunchOptions getLaunchOptions() {
        return this.launchOptions;
    }

    public Project getCurrentProject() {
        return this.project;
    }

}
