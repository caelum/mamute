New Relic Java Agent
--------------------
New Relic is an application performance monitoring service that lets you see 
and understand application performance metrics in real time so you can fix problems fast.  The
Java agent collects system and application metrics by injecting byte-code into a select
set of methods.  The metrics are reported to the New Relic service once a minute.  The agent also
reports uncaught exceptions and slow transactions.  

Getting Started
---------------
Two files are required to start the New Relic java agent - newrelic.jar and newrelic.yml.  
newrelic.jar contains the agent class files, and the newrelic.yml file contains configuration 
information for the New Relic agent, including your license key, application name,
ssl settings, etc.

You can download both the jar and the tailored yml file by logging into http://rpm.newrelic.com, 
selecting the "Account" page, and then downloading the files from the right side of the page.
If you don't already have a New Relic account creating one is easy: http://newrelic.com/signup.

The agent requires Java 1.5+.

Installation
------------
Complete installation instructions and troubleshooting tips are available 
at: http://support.newrelic.com/kb/java/new-relic-for-java

Configuration options are available at: http://support.newrelic.com/kb/java/java-agent-configuration

The NewRelic Java agent bootstraps using the -javaagent command line option. 

Create a directory in your application server home directory named newrelic and copy the 
newrelic.jar and newrelic.yml files into it. 

On server startup the agent will search for the newrelic.yml file in the directory containing 
the newrelic.jar file.  The agent log will be written relative to the newrelic.jar file in 
a directory named 'logs'.

  newrelic/
    newrelic.jar
    newrelic.yml
    logs/
      ...


Tomcat
------
The Tomcat startup script (catalina.sh) can be configured to use the New Relic agent using the JAVA_OPTS environment variable:

export JAVA_OPTS="$JAVA_OPTS -javaagent:/full/path/to/newrelic.jar"

Jetty
-----
The Jetty startup script (jetty.sh) can be configured using the JAVA_OPTIONS environment variable:

export JAVA_OPTIONS="${JAVA_OPTIONS} -javaagent:/full/path/to/newrelic.jar"

JBoss
-----
Add this line to the bottom of bin/run.conf:

JAVA_OPTS="$JAVA_OPTS -javaagent:/full/path/to/newrelic.jar"

Other Application Servers
-------------------------
The New Relic Java agent has been tested on Tomcat, Jetty and JBoss, but it should work on any application server.



That's all you need to do.  Start your application server with the -javaagent parameters and log in to New Relic
to see your app's performance information.  It takes about 2 minutes for the application data to show up
in New Relic (http://rpm.newrelic.com).  By default, your data will appear under an application named 
"My Application". You can change this by updating the app_name setting in newrelic.yml.

Support
-------
Email support@newrelic.com, or visit our support site at http://support.newrelic.com.

Licenses
--------
The New Relic Java agent uses code from the following open source projects under the following licenses:
    ASM                   http://asm.ow2.org/                              http://asm.ow2.org/license.html
    Apache Commons CLI    http://commons.apache.org/cli/                   http://www.apache.org/licenses/LICENSE-2.0
    JSON.simple           http://code.google.com/p/json-simple/            http://www.apache.org/licenses/LICENSE-2.0
    SnakeYAML             http://code.google.com/p/snakeyaml/              http://www.apache.org/licenses/LICENSE-2.0
    Log4J                 http://logging.apache.org/log4j/1.2/             http://www.apache.org/licenses/LICENSE-2.0
    LogBack               http://logback.qos.ch/                           http://www.eclipse.org/legal/epl-v10.html

The remainder of the code is under the New Relic Agent License contained in the LICENSE file.
