cd core
mvn clean eclipse:clean
mvn eclipse:eclipse -P eclipse
mvn package
cd ..
cp core/target/mamute-*.war example

cd example
unzip mamute-*.war
rm mamute-*.war