package br.com.caelum.vraptor.server;

import java.util.Arrays;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
		List<String> options = Arrays.asList(args);
		String webappDirLocation = getWebAppDir();

		String webXmlLocation = getWebXmlLocation(webappDirLocation);

		VRaptorServer vraptor = new VRaptorServer(webappDirLocation, webXmlLocation);
		if (args.length == 0 || !options.contains("not-grunt")) {
			new Thread(new GruntRunner(vraptor)).start();
		}
		vraptor.start();
	}

    private static String getWebXmlLocation(String webappDirLocation) {
        String webxml = System.getenv("VRAPTOR_WEBXML");
		webxml = webxml == null ? webappDirLocation + "/WEB-INF/web.xml" : webxml;
		return webxml;
    }

	private static String getWebAppDir() {
		return System.getProperty("vraptor.webappdir", "src/main/webapp/");
	}

}
