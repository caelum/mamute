package org.mamute.components;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;
import org.mamute.components.HerokuDatabaseInformation;

public class HerokuDatabaseInformationTest {

	@Test
	public void should_build_jbdc_string() {
		String hostPlusDb = "us-cdbr-east-03.cleardb.com/heroku_f7be99a98d938e8";
		HerokuDatabaseInformation dbInfo = new HerokuDatabaseInformation("mysql://b000991ffe1aee:e27f0462@"
				+ hostPlusDb);
		Properties p = dbInfo.exportToProperties();
		assertEquals("jdbc:mysql://" + hostPlusDb, p.get("hibernate.connection.url"));
	}

}
