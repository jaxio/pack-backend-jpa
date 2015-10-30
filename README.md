## Celerio Generation Template Pack: JPA backend

The Celerio Generation Template Pack "JPA backend" contains a set of source code file templates that
are interpreted by Celerio code generator in order to generate an application persistence layer.

## Generate/run an application that uses this pack

### Software Requirements & Maven Configuration

First make sure you have at least:

* Maven 3.1.0 installed
* JDK 1.8 installed

Then add Jaxio's Maven repository to your `~/.m2/settings.xml` file:

```xml
<settings xmlns="http://maven.apache.org/POM/4.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <!-- ... -->
    <profiles>
        <!-- ... -->
        <profile>
            <id>celerio</id>
            <repositories>
                <repository>
                    <id>jaxio-repository</id>
                    <url>http://maven.jaxio.com/repository</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>jaxio-repository</id>
                    <url>http://maven.jaxio.com/repository</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
        <!-- ... -->
    <profiles>
    <!-- ... -->
</settings>
```

### Type the following command from your console

`mvn -Pcelerio com.jaxio.celerio:bootstrap-maven-plugin:4.0.0:bootstrap`

Then follow the instructions...

## Celerio Documentation

Please refer to [Celerio Documentation][] for more information.

## Contributors

In alphabetical order:

* Jean-Louis Boudart
* Sébastien Péralta
* Florent Ramière
* Nicolas Romanetti

## License

The Celerio Generation Template Pack "JPA backend" is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Celerio Documentation]: http://www.jaxio.com/documentation/celerio/