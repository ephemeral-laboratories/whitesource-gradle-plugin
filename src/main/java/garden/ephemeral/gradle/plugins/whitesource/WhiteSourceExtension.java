package garden.ephemeral.gradle.plugins.whitesource;

import javax.inject.Inject;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;

/**
 * Gradle extension for WhiteSource configuration.
 */
public class WhiteSourceExtension {

    private final RegularFileProperty configurationFile;

    /**
     * Constructs the extension.
     */
    public WhiteSourceExtension() {
        configurationFile = getObjectFactory().fileProperty();
    }

    @Inject
    protected ObjectFactory getObjectFactory() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the configuration file property. This can be used to set the configuration file.
     *
     * The default value for the configuration file is "wss-unified-agent.config" in the project directory.
     *
     * @return the configuration file property.
     */
    public RegularFileProperty getConfigurationFile() {
        return configurationFile;
    }
}
