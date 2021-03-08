package plugin.linearizability.violat.runstateconfig;


import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ViolatRunState extends CommandLineState {
    private static final Logger log = Logger.getInstance(ViolatRunState.class);

    private ExecutionEnvironment ee;
    private ViolatRunConfiguration runCfg;

    ViolatRunState(ViolatRunConfiguration runCfg, ExecutionEnvironment environment) {
        super(environment);
        this.ee = environment;
        this.runCfg = runCfg;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {

        final String runCmd = runCfg.getViolatLaunchCmd();

        log.info("Running Violat with Command: " + runCmd);

        GeneralCommandLine commandLine = new GeneralCommandLine("/bin/sh", "-c", runCmd);
        if(runCfg.getProject().getBasePath() == null) throw new ExecutionException("Plugin unable to get the base path of the project");
        commandLine.setWorkDirectory(new File(runCfg.getProject().getBasePath()));
        ProcessHandler ph = new ColoredProcessHandler(commandLine);
        ph.addProcessListener(new ViolatProcessListener(runCfg.getProject()));
        return ph;
    }
}
