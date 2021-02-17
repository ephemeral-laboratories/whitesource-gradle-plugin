# WhiteSource Gradle plugin

A simple Gradle plugin to run WhiteSource.

## Usage

* Apply plugin in `build.gradle`:

    ```kotlin
    plugins {
        id("garden.ephemeral.whitesource") version "0.1.0"
    }
    ```

* Pick a version of WhiteSource to use and specify that in the dependencies:

    ```kotlin
    dependencies {
        whitesource("com.whitesource:wss-unified-agent:${wssUnifiedAgentVersion}")
    }
    ```

* Place your WhiteSource configuration file in the project directory as `wss-unified-agent.config`.
  Or perhaps you might want to relocate the configuration file?
  
    ```kotlin
    configure<WhiteSourceExtension> {
        configurationFile.set("config/wss-unified-agent.config")
    }
    ```

* Run the task like any other:

    ```shell
    ./gradlew whitesource
    ```
