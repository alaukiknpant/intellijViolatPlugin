package com.github.alaukiknpant.intellijviolatplugin.actions;

import com.github.alaukiknpant.intellijviolatplugin.pluginconfig.PluginConfigurable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;

import org.jetbrains.annotations.NotNull;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), PluginConfigurable.class);
    }
}
