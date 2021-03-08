package plugin.linearizability.violat.runstateconfig;


import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ViolatConfigurationType implements ConfigurationType {
    @Override
    @NotNull
    public String getDisplayName() {
        return "Violat";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Analyze your abstract data types using Violat";
    }

    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/META-INF/violatLogo.svg");
    }

    @NotNull
    @Override
    public String getId() {
        return "ViolatRunConfiguration";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new ViolatConfigurationFactory(this)};
    }
}