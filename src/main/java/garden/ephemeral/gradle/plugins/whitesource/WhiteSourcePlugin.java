package garden.ephemeral.gradle.plugins.whitesource;

import javax.annotation.Nonnull;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySet;

/**
 * Main entry point for the WhiteSource plugin.
 */
public class WhiteSourcePlugin implements Plugin<Project> {

    @Override
    public void apply(@Nonnull Project project) {
        WhiteSourceExtension extension = project.getExtensions().create("whitesource", WhiteSourceExtension.class);

        // Converting an action to lambda breaks Gradle up-to-date checking
        //noinspection Convert2Lambda
        project.getExtensions().configure("whitesource", new Action<WhiteSourceExtension>() {
            @Override
            public void execute(@Nonnull WhiteSourceExtension extension) {
                extension.getConfigurationFile().convention(() -> project.file("wss-unified-agent.config"));
            }
        });

        // set up a configuration named 'antlr' for the user to specify the antlr libs to use in case
        // they want a specific version etc.
        final Configuration whiteSourceConfiguration = project.getConfigurations().create("whitesource")
                .setVisible(false)
                .setDescription("The WhiteSource unified agent to use for this project.");

        // Converting an action to lambda breaks Gradle up-to-date checking
        //noinspection Convert2Lambda
        whiteSourceConfiguration.defaultDependencies(new Action<>() {
            @Override
            public void execute(@Nonnull DependencySet dependencies) {
                dependencies.add(project.getDependencies().create("org.whitesource:wss-unified-agent:19.7.3"));
            }
        });

        project.getTasks().register("whitesource", WhiteSourceTask.class);

        // Converting an action to lambda breaks Gradle up-to-date checking
        //noinspection Convert2Lambda
        project.getTasks().withType(WhiteSourceTask.class).configureEach(new Action<>() {
            @Override
            public void execute(@Nonnull WhiteSourceTask task) {
                task.getWhiteSourceClassPath().from(whiteSourceConfiguration);
                task.getConfigurationFile().convention(extension.getConfigurationFile());
            }
        });
    }
}