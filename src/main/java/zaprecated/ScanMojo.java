package zaprecated;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Mojo(name = "scan", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class ScanMojo extends AbstractMojo {

    /**
     * Directory containing the class files to analyze
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
    File classFilesDirectory;

    /**
     * Directory containing the test class files  to analyze
     */
    @Parameter(defaultValue = "${project.build.testOutputDirectory}", required = true)
    File testClassFilesDirectory;

    public void execute() {
        getLog().debug("****** ScanMojo execute *******");
        getLog().debug("classFilesDirectory: " + classFilesDirectory.getAbsolutePath());

        getLog().debug("Tree: " + tree(classFilesDirectory));
    }

    private List<String> tree(File file) {
        if (file == null || !file.exists()) return emptyList();

        if (file.isDirectory()) {
            return file.listFiles() == null
                    ? emptyList()
                    : Arrays.stream(file.listFiles())
                            .flatMap(f -> tree(f).stream())
                            .collect(toList());
        } else {
            return file.getName().endsWith(".class")
                    ? singletonList(file.getAbsolutePath())
                    : emptyList();
        }
    }
}
