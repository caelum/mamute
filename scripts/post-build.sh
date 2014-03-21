
TIMESTAMP=$1

TARGET_SCRIPT="target/mamute-1.0.0-SNAPSHOT/run.sh"

ant -Dtimestamp=$TIMESTAMP \
    -lib ant-lib/yuicompressor-2.4.8.jar \
    -buildfile merge-assets.xml

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/run.sh.template \
    > $TARGET_SCRIPT

chmod +x $TARGET_SCRIPT


