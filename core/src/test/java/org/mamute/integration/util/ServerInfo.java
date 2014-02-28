package org.mamute.integration.util;

public class ServerInfo {

	private static final String TEST_SERVER = "vraptor.server_host";
	static final int PORT = 8080;

	public interface AcceptanceTest {
		static final ServerInfo SERVER = new ServerInfo();
	}

	public String urlFor(String path) {
		return getRoot() + path;
	}
	
	public String getRoot() {
		return "http://" + ACTUAL_HOST;
	}

	private static final String ACTUAL_HOST = getHost();

	private static String getHost() {
		if (isRemoteRun())
			return System.getProperty(TEST_SERVER);
		return "localhost:" + PORT;
	}

	private static boolean isRemoteRun() {
		String server = System.getProperty(TEST_SERVER);
		return server != null && !server.isEmpty();
	}
}
