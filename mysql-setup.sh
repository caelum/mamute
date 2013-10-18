if [ $1 = "install" ]; then
mkdir -p ~/mysql
cat > ~/.my.cnf <<EOF
[client]
socket=/home/travis/mysql/mysql.sock
[mysqld]
datadir=/home/travis/mysql/data
user=jenkins
socket=/home/travis/mysql/mysql.sock
EOF

export MYSQL_HOME=~/mysql
mysql_install_db
/usr/libexec/mysqld &

# We need to let the thread sleep for a few seconds so the new socket setup is done
sleep 5
mysql -uroot -e "drop database if exists brutal_development;"
mysql -uroot -e "create database brutal_development;"
mysql -uroot -e "drop database if exists brutal_test;"
mysql -uroot -e "create database brutal_test;"
mysql -uroot -e "drop database if exists brutal_acceptance;"
mysql -uroot -e "create database brutal_acceptance;"
fi

if [ $1 = "uninstall" ]; then
#mysqladmin shutdown --user=root --password=""  --socket=$MYSQL_HOME/mysql.sock
fi