## Celerio Generation Template Pack: JPA backend

[![Build Status](https://travis-ci.org/jaxio/pack-backend-jpa.svg?branch=master)](https://travis-ci.org/jaxio/pack-backend-jpa)

The Celerio Generation Template Pack "JPA backend" contains a set of source code file templates that
are interpreted by Celerio code generator in order to generate an application persistence layer.

## Generate an application

This pack is part of Celerio distribution.

Have already Maven 3 and Java 1.8 installed ?

To generate an application from this pack simply execute:

    mvn com.jaxio.celerio:bootstrap-maven-plugin:4.0.9:bootstrap

Please read [Celerio Documentation](http://www.jaxio.com/documentation/celerio) for more details.

## Change Log

### 1.0.4 (2016-09-12)

* Leverage JPA @Convert for enum (see documentation)
* Audit in entity now supports different date types

### 1.0.3

* Fix name clash with table whose name is 'Document'.
* Comply with Celerio 4.0.3 which expects celerio-pack.xml to be present (instead of celerio.txt) in the jar of the pack

### 1.0.2

* Uses Celerio 4.0.2. As a result, no longer need to use Jaxio's repository since Celerio is now on Maven Central.

### 1.0.1

* Fix login page i18n rendering
* Clean up generated pom.xml

### 1.0

* initial version, upgraded from our private distribution

## Contributors

In alphabetical order:

* Jean-Louis Boudart
* Sébastien Péralta
* Florent Ramière
* Nicolas Romanetti

## License

The Celerio Generation Template Pack "JPA backend" is released under version 2.0
of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0)
