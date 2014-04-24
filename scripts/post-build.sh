
TIMESTAMP=$1

VERSION=1.0.1-SNAPSHOT

TARGET_SCRIPT_RUN="target/mamute-$VERSION/run.sh"
TARGET_SCRIPT_UPDATE="target/mamute-$VERSION/update-mamute.sh"

ant -Dtimestamp=$TIMESTAMP \
    -lib ant-lib/yuicompressor-2.4.8.jar \
    -buildfile merge-assets.xml

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/run.sh.template \
    > $TARGET_SCRIPT_RUN

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/update-mamute.sh.template \
    > $TARGET_SCRIPT_UPDATE

chmod +x $TARGET_SCRIPT_RUN
chmod +x $TARGET_SCRIPT_UPDATE


