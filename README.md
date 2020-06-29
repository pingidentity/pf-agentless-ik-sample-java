[![Build Status](https://travis-ci.org/pingidentity/pf-agentless-ik-sample.svg?branch=master)](https://travis-ci.org/pingidentity/pf-agentless-ik-sample)

PingFederate Agentless Integration Kit Sample
======================================================

### Overview

These sample applications provide a way to test an end-to-end identity provider (IdP) and service provider (SP) integration with PingFederate using the Agentless Integration Kit.

The sample distribution consists of two Java web applications and a PingFederate configuration archive which enables the sample applications to work on a single instance of PingFederate. The web applications are implemented with the Java Servlet API and JavaServer Pages (JSP) so that the applications are easy to build and deploy, and the source can be readily viewed by developers.

### System requirements and dependencies

* PingFederate 8.x or later.

### Building the Source

1. Install the latest Maven (https://maven.apache.org/).
2. Open a command line interface and from the project root execute:
```
mvn clean install
```
3. The Agentless Integration Kit Sample distribution file will be in the <project_root>/assembly/target directory.

### Installation and Configuration

For the latest documentation, please visit our [Knowledge Center](https://docs.pingidentity.com/) (docs.pingidentity.com).

### Reporting bugs

Please report issues using this project's issue tracker.

### License

This project is licensed under the Apache License 2.0.
