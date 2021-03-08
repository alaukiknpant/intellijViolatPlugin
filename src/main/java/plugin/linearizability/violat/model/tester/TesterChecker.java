package plugin.linearizability.violat.model.tester;

public class TesterChecker {
    @SuppressWarnings("UnusedReturnValue")
    public static boolean returnInvalidInstallations() {

        final String JPF_VER = "jpf -version";
        final String exitCodeCmd = "echo $?";
        boolean isValid = true;
        try {
            Process p = Runtime.getRuntime().exec(JPF_VER);
            p.waitFor();
            int exitStatus = p.exitValue();
            if (exitStatus != 0) {
                isValid = false;
                }
            }  catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }
}