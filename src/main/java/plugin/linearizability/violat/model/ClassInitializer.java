package plugin.linearizability.violat.model;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.linearizability.violat.specgenerator.GetSpecs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassInitializer {
    private static final Logger log = Logger.getInstance(SpecificationInitializer.class);
    private String path;

    public ClassInitializer() {}

    private ClassInitializer(String path) {
        setPath(path);
    }

    @Nullable
    public static ClassInitializer createClassInitializer(String path) {
        ClassInitializer ii = new ClassInitializer(path);
        if(ii.confirm(path)) return ii;
        return null;
    }

    /**
     * Checks if the Infer Installation at the given path is valid.
     * @param path Full path to the infer binary
     * @return The Version if the installation is valid, otherwise null
     */
    @Nullable
    public Boolean confirm(@NotNull String path) {
        if(path.contains(".")) {
            return true;
        } else {
            return false;
        }
    }

    public void setPath(String path) {
         this.path = path;
    }

    public static boolean generateSpecs(Class cls, String pathToSpecFile) throws ClassNotFoundException {
        boolean createdSpecFile = false;
        if (cls != null) {
            JsonObject j = GetSpecs.getSpecs(cls);

            try (FileWriter file = new FileWriter(pathToSpecFile)) {
                file.write(j.toJson());
                file.flush();
                createdSpecFile = true;
            } catch (IOException e) {
                System.out.println("Noo canot generate Specs \n\n\n\n\n");
                e.printStackTrace();
            }
        }
        return createdSpecFile;
    }


    public static String compileClassAndCreateJar(String path, String packages) throws IOException, InterruptedException {
        // Compile the java class
        Process compileProcess = new ProcessBuilder("javac", path).start();
        compileProcess.waitFor();
        // Create a jar file through scripting
        String mainPackage = packages.split("\\.")[0];
        String basePath = path.substring(0, path.indexOf(mainPackage));
        String mainPackagePath = basePath + mainPackage;
        String jarName = mainPackage + ".jar";
        String pathToJarFolder = basePath + "jarFiles/";
        String jarPath = pathToJarFolder + jarName;
        Process makeJarFolderProcess = new ProcessBuilder("mkdir", pathToJarFolder).start();
        makeJarFolderProcess.waitFor();


        Process artifactProcess = null;
        ProcessBuilder pb = new ProcessBuilder("jar", "cvf", jarPath, mainPackage);
        pb.directory(new File(basePath));
        System.out.println(basePath);
        System.out.println("\n\n\n\n\n");
        artifactProcess = pb.start();
        artifactProcess.waitFor();
        return jarPath;
    }

    public static Class loadClass(String path, String packageName) throws IOException, ClassNotFoundException {
        String mainPackage = packageName.split("\\.")[0];
        String basePath = path.substring(0, path.indexOf(mainPackage));
        Class cls = new URLClassLoader(new URL[]{new File(basePath).toURI().toURL()}).loadClass(packageName);
        return cls;
    }

    public static void deleteClass(String path) throws IOException, InterruptedException {
        // Remove the compiled java class
        String pathToClassFile = path.substring(0, path.indexOf(".java")) + ".class";
        Process removeProcess = new ProcessBuilder("rm", pathToClassFile).start();
        removeProcess.waitFor();
    }

    public static String createSpecsFolder(String path, String packages) throws InterruptedException, IOException {

        // Create a specs folder through scripting
        String mainPackage = packages.split("\\.")[0];
        String basePath = path.substring(0, path.indexOf(mainPackage));
        String mainPackagePath = basePath + mainPackage;
        String specName = mainPackage + ".json";
        String pathToSpecFolder = basePath + "specFiles/";
        Process makeJarFolderProcess = new ProcessBuilder("mkdir", pathToSpecFolder).start();
        makeJarFolderProcess.waitFor();
        String pathToSpec = pathToSpecFolder + specName;
        return pathToSpec;
    }

}

