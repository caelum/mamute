TIMESTAMP=$1

VERSION=`scripts/get-version.sh`

TARGET_DIR="target/mamute-$VERSION/"
TARGET_SCRIPT_RUN="target/mamute-$VERSION/run.sh"
TARGET_SCRIPT_UPDATE="target/mamute-$VERSION/update-mamute.sh"

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/run.sh.template \
    > $TARGET_SCRIPT_RUN

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/update-mamute.sh.template \
    > $TARGET_SCRIPT_UPDATE

chmod +x $TARGET_SCRIPT_RUN
chmod +x $TARGET_SCRIPT_UPDATE