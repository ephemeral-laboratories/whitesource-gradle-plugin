package garden.ephemeral.gradle.plugins.whitesource;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.internal.ExecActionFactory;
import org.gradle.process.internal.JavaExecAction;

/**
 * Task to run a WhiteSource check.
 */
public class WhiteSourceTask extends DefaultTask {

    private final ConfigurableFileCollection whiteSourceClassPath;

    private final RegularFileProperty configurationFile;

    public WhiteSourceTask() {
        setDescription("Runs WhiteSource on the project");

        ObjectFactory objectFactory = getObjectFactory();
        whiteSourceClassPath = objectFactory.fileCollection();
        configurationFile = objectFactory.fileProperty();
    }

    @Inject
    protected ObjectFactory getObjectFactory() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected ExecActionFactory getExecActionFactory() {
        throw new UnsupportedOperationException();
    }

    @TaskAction
    public void runWhiteSource() {
        JavaExecAction javaExecAction = getExecActionFactory().newJavaExecAction();
        javaExecAction.setClasspath(getWhiteSourceClassPath());
        javaExecAction.execute();
    }

    /**
     * Gets the classpath to use to run WhiteSource. Can be used to set an alternative classpath.
     *
     * Projects should generally not need to mess with this, and should instead be setting the
     * version to use via the "whitesource" configuration.
     *
     * @return the classpath property.
     */
    public ConfigurableFileCollection getWhiteSourceClassPath() {
        return whiteSourceClassPath;
    }

    /**
     * Gets the configuration file to use.
     *
     * Projects should generally not need to mess with this, and should instead be setting via
     * the "whitesource" extension.
     *
     * @return the configuration file property.
     */
    public RegularFileProperty getConfigurationFile() {
        return configurationFile;
    }
}
