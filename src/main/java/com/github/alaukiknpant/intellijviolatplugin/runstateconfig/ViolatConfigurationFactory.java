package com.github.alaukiknpant.intellijviolatplugin.runstateconfig;

import com.github.alaukiknpant.intellijviolatplugin.pluginconfig.GlobalSettings;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViolatConfigurationFactory extends ConfigurationFactory {
    private static final String FACTORY_NAME = "Violat configuration factory";

    ViolatConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    @NotNull
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new ViolatRunConfiguration(project, this, "Violat");
    }

    @Override
    @NotNull
    @SuppressWarnings("com.intellij.util.DeprecatedMethodException")
    public String getName() {
        return FACTORY_NAME;
    }

    /**
     * Creates a Violat Run Configuration
     * @param runManager The RunManager of the project where it should be generated
     * @param name The name of Violat Run Configuration
     * @return A run configuration for violat or null
     */
    @Nullable
    public static RunnerAndConfigurationSettings createValidConfiguration(RunManagerImpl runManager, String name) {
        if(!GlobalSettings.getInstance().hasValidInstallation()) return null;
        final ConfigurationFactory violatFactory = runManager.getFactory("ViolatRunConfiguration", "Violat configuration factory");
        if(violatFactory != null) {
            RunnerAndConfigurationSettings rcs = runManager.createConfiguration(name, violatFactory);
            ViolatRunConfiguration violatRunConfig = (ViolatRunConfiguration) rcs.getConfiguration();
            violatRunConfig.getLaunchOptions().setSelectedInstallation(GlobalSettings.getInstance().getAnyValidInstallation());
            return rcs;
        }
        return null;
    }

}
