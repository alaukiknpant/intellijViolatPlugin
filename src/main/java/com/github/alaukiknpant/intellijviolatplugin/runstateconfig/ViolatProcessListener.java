package com.github.alaukiknpant.intellijviolatplugin.runstateconfig;


import com.github.alaukiknpant.intellijviolatplugin.pluginconfig.GlobalSettings;
import com.github.alaukiknpant.intellijviolatplugin.resultparsers.ResultParser;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ViolatProcessListener implements ProcessListener {
    private static final Logger log = Logger.getInstance(ViolatProcessListener.class);
    private Project project;

    ViolatProcessListener(Project project) { this.project = project; }

    /**
     * Checks if Violat terminated correctly
     * @param event Current run of violat
     */
    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
        if(event.getExitCode() == 0) {
            log.info("Violat Process terminated successfully: Status Code 0");

            // Is checking if the user wants to see the run of violat
            if(!GlobalSettings.getInstance().isShowConsole()) {
                ApplicationManager.getApplication().invokeAndWait(() -> ToolWindowManager.getInstance(project).getToolWindow("Violat").activate(null, true));
            }

            final Path reportPath = Paths.get(project.getBasePath() + "/result.txt");
            if(Files.exists(reportPath)) ResultParser.getInstance(project).parse(reportPath);
            else log.error("result.txt does not exist after Violat terminated successfully: Check the Violat log");

        } else {
            log.warn("Violat Process terminated with a failure status code of " + event.getExitCode());
            Notifications.Bus.notify(new Notification("Violat", "Failure", "Violat terminated unsuccessfully", NotificationType.ERROR));
        }
    }

    @Override public void startNotified(@NotNull ProcessEvent event) { }
    @Override public void processWillTerminate(@NotNull ProcessEvent event, boolean willBeDestroyed) { }
    @Override public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) { }
}