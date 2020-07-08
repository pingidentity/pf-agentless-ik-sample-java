[![Build Status](https://travis-ci.org/pingidentity/pf-agentless-ik-sample-java.svg?branch=master)](https://travis-ci.org/pingidentity/pf-agentless-ik-sample-java)

<p align="center">
    <img src="https://assets.pingone.com/ux/end-user/0.14.0/images/ping-logo.svg" height="150" width="150" />
</p>


PingFederate Agentless Integration Kit Sample for Java
======================================================

### Overview

These sample applications provide a way to test an end-to-end identity provider (IdP) and service provider (SP) integration with PingFederate using the Agentless Integration Kit.

The sample distribution consists of two Java web applications and a PingFederate configuration archive which enables the sample applications to work on a single instance of PingFederate. The web applications are implemented with the Java Servlet API and JavaServer Pages (JSP) so that the applications are easy to build and deploy, and the source can be readily viewed by developers.

<p align="center">
    <img src="/images/example.gif"/>
</p>


### System requirements and dependencies

* PingFederate 8.x or later.
* PingFederate Agentless Integration Kit 1.5 or later.

### Browser compatibility

The samples work with all major browsers, including Chrome, Firefox, Microsoft Edge, and Internet Explorer 11.

## Setup

### Deploying the PingFederate configuration archive
The included configuration archive creates the adapter instances and connections needed to run the sample applications.

To deploy the configuration archive, import it through the administrator console or copy it to the drop-in-deployer directory. For instructions, see [Configuration archive](https://docs.pingidentity.com/csh?Product=pf-latest&topicname=oor1564002974031.html) in the PingFederate documentation.

**Caution:** Deploying the configuration archive will destroy your existing PingFederate configuration. We recommend that you test it on a fresh installation of PingFederate or back up your current configuration as shown in [Exporting an archive](https://docs.pingidentity.com/csh?Product=pf-latest&topicname=amd1564002974196.html) in the PingFederate documentation.


### Building the Source

1. Install the latest Maven (https://maven.apache.org/).
2. Open a command line interface and from the project root execute:
```
mvn clean install
```
3. The Agentless Integration Kit Sample distribution file will be in the <project_root>/assembly/target directory.

### Running the applications
1. Locate the pf-agentless-ik-sample-x.x.x.zip file from the `<project_root>/assembly/target` directory.
2. Extract the contents of the `pf-agentless-ik-sample-<version>.zip`.
3. Stop PingFederate.
4. If you are upgrading from an earlier version of the integration, delete `AgentlessIdPSample.war` and `AgentlessSPSample.war` from `<pf_install>/pingfederate/server/default/deploy`.
5. From the .zip file, copy the contents of `dist` to `<pf_install>/pingfederate/server/default/deploy`.
6. Start PingFederate.
7. If you operate PingFederate in a cluster, repeat steps 2-5 for each instance of PingFederate.
### Installation and Configuration

For the latest documentation, please visit our [Knowledge Center](https://docs.pingidentity.com/) (docs.pingidentity.com).

### Reporting bugs

Please report issues using this project's issue tracker.

### License

This project is licensed under the Apache License 2.0.
