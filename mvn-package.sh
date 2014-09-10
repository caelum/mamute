APPLICATION_DIR=`readlink -f $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )`
TEMP_FOLDER_NAME=".mamute-build"
TEMP_FOLDER="${APPLICATION_DIR}/${TEMP_FOLDER_NAME}"
rsync -av --delete --exclude=${TEMP_FOLDER_NAME} . ${TEMP_FOLDER} 
cd ${TEMP_FOLDER}
mvn package -DskipTests
