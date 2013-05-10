package br.com.caelum.brutal.components;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerokuDatabaseInformation {
	
	private static final Logger logger = LoggerFactory
			.getLogger(HerokuDatabaseInformation.class);

	private final URI database;

	public HerokuDatabaseInformation(String databaseUrl) {
		try {
			this.database = new URI(databaseUrl);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public void exportToSystem() {
		logger.info("Using " + mysqlUrl());
		System.setProperty("heroku_database", mysqlUrl());

		String[] userInfo = userInfo();
		logger.info("Using " + user(userInfo));
		logger.info("Using " + password(userInfo));
		System.setProperty("heroku_database_user", user(userInfo));
		System.setProperty("heroku_database_password", password(userInfo));
	}

	private String[] userInfo() {
		return database.getUserInfo().split(":");
	}

	private String password(String[] userInfo) {
		return userInfo[1];
	}

	private String user(String[] userInfo) {
		return userInfo[0];
	}

	private String mysqlUrl() {
		return "jdbc:mysql://" + database.getHost() + database.getPath();
	}

	public Properties exportToProperties() {
		Properties p = new Properties();
		logger.info("Using " + mysqlUrl());
		p.put("hibernate.connection.url", mysqlUrl());

		String[] userInfo = userInfo();
		logger.info("Using " + user(userInfo));
		logger.info("Using " + password(userInfo));
		p.put("hibernate.connection.username", user(userInfo));
		p.put("hibernate.connection.password", password(userInfo));
		return p;
	}

}
