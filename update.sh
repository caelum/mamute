
#!/bin/bash
if [ ! -z $1 ]; then
        echo "    Usage:"
        echo "    	Using git: REPO_PATH=path/of/mamute/repository $0"
        echo "    	Using war: WAR_PATH=path/of/mamute/war $0"
        echo "    If you run ./mamute/update-mamute.sh without arguments the default argument will be:"
        echo "		REPO_PATH = ../mamute"
        exit 1
fi

echo "Your APPLICATION_DIR is $APPLICATION_DIR"

if [ ! -z "$WAR_PATH" ]; then
	if [ ! -f $WAR_PATH ]; then
		echo "the war $WAR_PATH doesn't exists!"
		exit 1
	fi 
else
	REPO_PATH=${REPO_PATH:-"$MAMUTE_DIR"} #if no REPO_PATH specified, try to use MAMUTE_DIR
	REPO_PATH=${REPO_PATH:-`dirname "$APPLICATION_DIR/../mamute"`} #if no MAMUTE_DIR specified, use default value

	if [ ! -d $REPO_PATH ]; then
		echo "the folder $REPO_PATH doesn't exists!"
		exit 1
	fi 
fi



echo "I will update the mamute of the instance: $APPLICATION_DIR . Press enter to continue..."
read

if [ ! -d "$APPLICATION_DIR"/overrides ]; then
	mkdir "$APPLICATION_DIR/overrides"
fi

customFiles=("WEB-INF/classes/messages*.properties"
 "WEB-INF/classes/environment.properties"
 "WEB-INF/classes/development.properties"
 "WEB-INF/classes/acceptance.properties"
 "WEB-INF/classes/production.properties"
 "WEB-INF/classes/META-INF/beans.xml"
 "css/deps/custom.css"
 "css/mamute/mamute.*.css"
 "js/mamute/mamute.*.js"
 "js/jquery/jquery-plugins-*.js"
 "WEB-INF/classes/production"
 "WEB-INF/jsp/theme/custom"
 "imgs/custom")

cd "$APPLICATION_DIR/mamute"

echo 'Copying custom files'
for ((i=0; i<${#customFiles[@]} ; i++)); do
    if [ -d ${customFiles[i]} ]; then
        mkdir -p $APPLICATION_DIR/overrides/${customFiles[i]}
        cp -R $APPLICATION_DIR/mamute/${customFiles[i]}/* $APPLICATION_DIR/overrides/${customFiles[i]}/
    else
        for F in `ls ${customFiles[i]}`; do
            DIR=`dirname ${F}` 
            FILENAME=`basename $F`
            mkdir -p $APPLICATION_DIR/overrides/$DIR
            cp -v $APPLICATION_DIR/mamute/$DIR/$FILENAME $APPLICATION_DIR/overrides/$DIR
        done
    fi 
done 

echo  'Removing old version of mamute' 
mv "$APPLICATION_DIR/mamute" "${APPLICATION_DIR}/mamute-old_`date +%s`"
mkdir "$APPLICATION_DIR/mamute"

echo 'Updating mamute'

if [ ! -z "$WAR_PATH" ]; then
	echo "Using war $WAR_PATH"

	unzip -d "$APPLICATION_DIR/mamute" "$WAR_PATH"

else
	echo "Using github repository  $REPO_PATH"
	
	cd "$REPO_PATH"

	git checkout master
	git pull

	$REPO_PATH/scripts/mvn-package.sh

	echo 'Copying new version of mamute'

	VERSION=`scripts/get-version.sh`
	cp -R $REPO_PATH/target/mamute-$VERSION/* "$APPLICATION_DIR/mamute"
fi

echo 'Copying overrides to new version of mamute'
cp -R $APPLICATION_DIR/overrides/* "$APPLICATION_DIR/mamute"

echo 'Removing custom files from overrides'
for ((i=0; i<${#customFiles[@]} ; i++))
do
	echo "Removing ${customFiles[i]}"
	rm -r $APPLICATION_DIR/overrides/${customFiles[i]}
done
