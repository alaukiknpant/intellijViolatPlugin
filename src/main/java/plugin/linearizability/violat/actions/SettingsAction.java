package plugin.linearizability.violat.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;

import plugin.linearizability.violat.pluginconfig.PluginConfigurable;

import org.jetbrains.annotations.NotNull;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println(PluginConfigurable.class);
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), PluginConfigurable.class);
    }
}
