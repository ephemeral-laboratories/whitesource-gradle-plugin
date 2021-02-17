package garden.ephemeral.gradle.plugins.whitesource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.gradle.api.GradleException;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WhiteSourcePluginTest {

    @TempDir
    public File tempDir;

    private File projectDir;

    @BeforeEach
    public void prepareProjectDir() throws Exception {
        projectDir = new File(tempDir, "test-project");
        Files.createDirectory(projectDir.toPath());
    }

    @Test
    public void testApplyingPlugin() throws Exception {
        GradleRunner runner = createRunner();

        BuildResult result = runner.withArguments("check", "--stacktrace", "-Duser.language=en").build();

        assertThat(result.task(":check").getOutcome(), is(TaskOutcome.UP_TO_DATE));
    }

    // TODO: Test actually running it, but how? Mock their service?

    /**
     * Convenience method to write a file.
     *
     * @param relativePath the relative path to the file from the project directory.
     * @param content      the content to write into the file.
     * @throws Exception if an error occurs.
     */
    private void write(String relativePath, String... content) throws Exception
    {
        Path filePath = projectDir.toPath().resolve(relativePath);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, ImmutableList.copyOf(content), StandardCharsets.UTF_8);
    }

    /**
     * Gets a classpath string suitable for inserting into a Gradle build.
     *
     * @return the classpath string.
     */
    private String getGradleClassPath()
    {
        return Splitter.on(File.pathSeparator).splitToList(System.getProperty("java.class.path")).stream()
                .map(element -> '\"' + element + '\"')
                .collect(Collectors.joining(", "));
    }

    /**
     * Creates a runner to run Gradle.
     *
     * @return the Gradle runner.
     * @throws Exception if an error occurs.
     */
    private GradleRunner createRunner() throws Exception
    {
        write("settings.gradle.kts", "rootProject.name = \"plugin-test-project\"");

        URL resource = WhiteSourcePluginTest.class.getClassLoader().getResource("testkit-gradle.properties");
        if (resource == null) {
            throw new GradleException("testkit file not found");
        }
        try (InputStream stream = resource.openStream()) {
            Files.copy(stream, projectDir.toPath().resolve("gradle.properties"));
        }

        // Normally we'd be including the plugin using the `plugins` block but there is some issue
        // where it can't be loaded from the classpath using that.
        String[] buildFileContents = {
                "import garden.ephemeral.gradle.plugins.whitesource.*",
                "",
                "buildscript {",
                "    dependencies {",
                "        classpath(files(" + getGradleClassPath() + "))",
                "    }",
                "}",
                "",
                "repositories {",
                "    jcenter()",
                "}",
                "",
                "apply<JavaPlugin>()",
                "apply<WhiteSourcePlugin>()",
                "",
                "dependencies {",
                "    \"testImplementation\"(\"junit:junit:4.12\")",
                "    \"testImplementation\"(\"org.hamcrest:java-hamcrest:2.0.0.0\")",
                "    \"testImplementation\"(\"org.hamcrest:hamcrest-junit:2.0.0.0\")",
                "}",
        };
        write("build.gradle.kts", buildFileContents);

        return GradleRunner.create().withProjectDir(projectDir);
    }
}
