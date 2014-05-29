package { ["openjdk-7-jdk", "unzip", "mysql-server"]:
	ensure => installed
}

service { "mysql":
	ensure => running,
	enable => true,
	hasstatus => true,
	hasrestart => true,
	require => Package["mysql-server"]
}

exec { "mamute_development":
	command => "mysqladmin -uroot create mamute_development",
	unless => "mysql -u root mamute_development",
	path => "/usr/bin",
	require => Service["mysql"]
}


exec {
    'download':
        command     => '/usr/bin/wget http://dl.bintray.com/caelum/Mamute/mamute-1.0.1.war',
        path   => '/home/vagrant',
        unless      => '/usr/bin/test -f /home/vagrant/mamute-1.0.1.war',
        require => Exec['mamute_development'];
}

exec {
	'unzips':
        command     => '/usr/bin/unzip -o /home/vagrant/mamute-1.0.1.war',
        path   => '/home/vagrant',
        require => [Package['unzip'], Exec['download']];
}

service {
    'mamute':
        ensure      => running,
        enable      => true,
        hasstatus => false,
        provider => base,
        path => '/home/vagrant',
        start => '/home/vagrant/run.sh &',
        stop => 'killall -9 java',
        require     => Exec['unzips'];
}