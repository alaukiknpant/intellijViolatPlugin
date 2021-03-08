package plugin.linearizability.violat.runstateconfig;


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
import plugin.linearizability.violat.pluginconfig.GlobalSettings;
import plugin.linearizability.violat.model.Checker;
import plugin.linearizability.violat.model.ViolatInstallation;
import plugin.linearizability.violat.model.ViolatLaunchOptions;
import plugin.linearizability.violat.model.buildtool.BuildToolChecker;
import plugin.linearizability.violat.model.tester.TesterChecker;
import plugin.linearizability.violat.toolwindows.RunConfigurationEditor;

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

        if (!BuildToolChecker.returnInvalidInstallations()) throw new RuntimeConfigurationException("Java, maven or gradle might be missing");
        if (!TesterChecker.returnInvalidInstallations()) throw new RuntimeConfigurationException("Java Pathfinder not found in executable path");
        if(launchOptions.getSelectedCheckers() == null || launchOptions.getSelectedCheckers().isEmpty()) throw new RuntimeConfigurationException("No Checker selected");
        if(launchOptions.getSelectedInstallation() == null || !launchOptions.getSelectedInstallation().isConfirmedWorking()) throw new RuntimeConfigurationException("No selected Installation or the Installation is invalid");
        if (launchOptions.getSelectedCheckers().size() > 1) throw new RuntimeConfigurationException("We can have a maximum of 1 Checker");
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
