
TIMESTAMP=$1

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/run.sh.template \
    > target/mamute-1.0.0-SNAPSHOT/run.sh

