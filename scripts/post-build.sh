
TIMESTAMP=$1

TARGET_SCRIPT_RUN="target/mamute-1.0.0-SNAPSHOT/run.sh"
TARGET_SCRIPT_UPDATE="target/mamute-1.0.0-SNAPSHOT/update-mamute.sh"

ant -Dtimestamp=$TIMESTAMP \
    -lib ant-lib/yuicompressor-2.4.8.jar \
    -buildfile merge-assets.xml

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/run.sh.template \
    > $TARGET_SCRIPT_RUN

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/update-mamute.sh.template \
    > $TARGET_SCRIPT_UPDATE

chmod +x $TARGET_SCRIPT_RUN
chmod +x $TARGET_SCRIPT_UPDATE


