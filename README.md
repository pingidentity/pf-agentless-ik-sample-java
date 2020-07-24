[![Build Status](https://travis-ci.org/pingidentity/pf-agentless-ik-sample-java.svg?branch=master)](https://travis-ci.org/pingidentity/pf-agentless-ik-sample-java)

<p align="center">
    <img src="https://assets.pingone.com/ux/end-user/0.14.0/images/ping-logo.svg" height="150" width="150" />
</p>


# PingFederate Agentless Integration Kit Sample Applications for Java

## Overview

These sample applications let you test an integration with the Agentless Integration Kit. PingFederate acts as both the identity provider (IdP) and service provider (SP), showing the complete end-to-end configuration and user experience.

The package includes two independent Java web applications, one for each of the IdP and SP roles. You can see the source code and deploy the applications easily inside PingFederate.
The included PingFederate configuration archive allows a single instance of PingFederate to run both sample applications.

<p align="center">
    <img src="/images/example.gif"/>
</p>


## System requirements and dependencies

* PingFederate 8.x or later
* PingFederate Agentless Integration Kit 1.5 or later
* A Java environment listed on [System Requirements](https://docs.pingidentity.com/csh?Product=pf-latest&topicname=rjt1564002959360.html
) in the PingFederate documentation

### Browser compatibility

The samples work with all major browsers, including Chrome, Firefox, and Microsoft Edge.

## Setup

### Deploying the PingFederate configuration archive
The included configuration archive creates the adapter instances and connections needed to run the sample applications.

To deploy the configuration archive, import it through the administrator console or copy it to the drop-in-deployer directory. For instructions, see [Configuration archive](https://docs.pingidentity.com/csh?Product=pf-latest&topicname=oor1564002974031.html) in the PingFederate documentation.

**Caution:** Deploying the configuration archive will destroy your existing PingFederate configuration. We recommend that you test it on a fresh installation of PingFederate or back up your current configuration as shown in [Exporting an archive](https://docs.pingidentity.com/csh?Product=pf-latest&topicname=amd1564002974196.html) in the PingFederate documentation.


### Building the Source

1. Install the latest version of [Maven](https://maven.apache.org/).
2. Go to the root directory of this project.
3. Build the sample application `.zip` file by executing the following command: `mvn clean install`

### Running the applications
1. In the resulting `<project_root>/assembly/target` directory, extract the `pf-agentless-ik-sample-version.zip` file.
2. Stop PingFederate.
3. From the `.zip` file, copy the contents of `dist` to `<pf_install>/pingfederate/server/default/deploy`.
4. Start PingFederate. The sample applications start automatically.
5. In your browser, go to the following URL to start IdP single sign-on flow: `https://localhost:9031/sp/startSSO.ping?PartnerIdpId=PF-DEMO`
6. Sign on as `joe`, `scott`, or `sarah` with a password of `test`.

## Modifying your application
When you are ready to make changes to your own application, see the examples in the `example-code` directory to help you get started.

### Documentation

For the latest documentation, see [Agentless Integration Kit](https://docs.pingidentity.com/bundle/integrations/page/ygj1563994984859.html) in the Ping Identity [Support Home](https://support.pingidentity.com/s/)

### Reporting bugs

Please report issues using this project's issue tracker.

### License

This project is licensed under the Apache license. See the [LICENSE](LICENSE) file for more information.
