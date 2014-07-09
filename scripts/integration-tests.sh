#!/usr/bin/env bash

if [ ! -z $1 ]; then
	echo "    Usage:"
	echo "    LOCAL_TEST=remote|true PHANTOM_JS_PATH=path/to/phantomjs-home $0"
	echo "    Default values: "
	echo "        LOCAL_TEST = remote"
	echo "        PHANTOM_JS_PATH = /home/ubuntu/phantomjs/phantomjs"
	exit 1
fi

LOCAL_TEST=${LOCAL_TEST:-remote}
export LOCAL_TEST
PHANTOM_JS_PATH=${PHANTOM_JS_PATH:-/home/ubuntu/phantomjs/phantomjs}

export PORT=9999
TEST_SERVER=localhost:${PORT}
export ACCEPTANCE_ENV=acceptance
export VRAPTOR_ENVIRONMENT="acceptance"
export DATAIMPORT_ENV=$ACCEPTANCE_ENV
SERVER_PACKAGE="br.com.caelum.vraptor.server"

if [ $LOCAL_TEST = "remote" ]; then
	echo "Integration test Remote running..."
elif [ $LOCAL_TEST = "true" ]; then
    echo "Integration test Local running..."
else
	echo "Check your LOCAL_TEST variable. LOCAL_TEST=remote|true"
	exit 1
fi
sleep 2

rm -f src/main/webapp/WEB-INF/lib/*.jar
mvn clean
mvn package -DskipTests

mvn exec:java -Dexec.mainClass="org.mamute.util.DataImport" -Dexec.classpathScope="test" || exit 1

java -Dvraptor.webappdir=target/mamute-1.0.0-SNAPSHOT/ \
	-Djava.security.policy=jetty/java.policy \
	-Ddeploy.timestamp=${TIMESTAMP} \
	-cp target/mamute-1.0.0-SNAPSHOT/WEB-INF/classes/:target/mamute-1.0.0-SNAPSHOT/WEB-INF/lib/* \
	br.com.caelum.vraptor.server.Main &


SERVER_PID=$!
sleep 30 

if [ $LOCAL_TEST = "remote" ]; then
	export CLOSE_DRIVER_ON_EXIT=true
	echo "subindo ghostdriver..."
	$PHANTOM_JS_PATH/bin/phantomjs --webdriver=localhost:8787 & 
	PHANTOM_JS_PID=$!
    mvn integration-test -DargLine="-Dvraptor.server_host=${TEST_SERVER}" -P acceptance-tests
	RESULT=$?
	kill $PHANTOM_JS_PID
elif [ $LOCAL_TEST = "true" ]; then
	export FAIL_FAST=false
    mvn integration-test -DargLine="-Dvraptor.server_host=${TEST_SERVER}" -P acceptance-tests
	RESULT=$?
fi

kill $SERVER_PID

exit $RESULT
