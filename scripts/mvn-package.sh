APPLICATION_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/..
TEMP_FOLDER_NAME=".mamute-build"
TEMP_FOLDER="${APPLICATION_DIR}/${TEMP_FOLDER_NAME}"
cd ${APPLICATION_DIR}
mvn clean
rsync -av --delete --exclude=${TEMP_FOLDER_NAME} --exclude=.git . ${TEMP_FOLDER} 
cd ${TEMP_FOLDER}
mvn package -DskipTests
mv target ../target
