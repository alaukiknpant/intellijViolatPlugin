package plugin.linearizability.violat.pluginconfig;

import com.intellij.openapi.components.BaseComponent;
import org.jetbrains.annotations.NotNull;

public class DefaultConfigComponent implements BaseComponent {
    @NotNull
    @Override
    public String getComponentName() {
        return "ConfigComponent";
    }

    @Override
    public void initComponent() {
        GlobalSettings.getInstance().addInstallation("violat", true);
    }
}
