package plugin.linearizability.violat.model.buildtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildToolChecker {
    @SuppressWarnings("UnusedReturnValue")
    public static boolean returnInvalidInstallations() {

        final String JAVA_VER = "java -version";
        final String JAVAC_VER = "javac -version";
        final String MVN_VER_CMD = "mvn -version";
        final String GRADLE_VER_CMD = "gradle -version";
        final List<String> cmds = Arrays.asList(JAVA_VER, JAVAC_VER, MVN_VER_CMD, GRADLE_VER_CMD);
        final String exitCodeCmd = "echo $?";
        ArrayList<String> invalidInstallations = new ArrayList<>();
        boolean isValid = true;
        try {
            for (int i = 0; i < cmds.size(); i++) {
                String cmnd = cmds.get(i);
                Process p = Runtime.getRuntime().exec(cmnd);
                p.waitFor();
                int exitStatus = p.exitValue();
                if (exitStatus != 0) {
                    invalidInstallations.add(cmnd);
                    isValid = false;
                }
            }
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }
}

