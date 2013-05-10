package br.com.caelum.brutal.components;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

public class HerokuDatabaseInformationTest {

	@Test
	public void test() {
		String hostPlusDb = "us-cdbr-east-03.cleardb.com/heroku_f7be99a98d938e8?reconnect=true";
		HerokuDatabaseInformation dbInfo = new HerokuDatabaseInformation("mysql://b000991ffe1aee:e27f0462@"
				+ hostPlusDb);
		Properties p = dbInfo.exportToProperties();
		assertEquals("jdbc:mysql://" + hostPlusDb, p.get("hibernate.connection.url"));
	}

}
