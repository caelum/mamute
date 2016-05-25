package br.com.caelum.vraptor.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VRaptorServer {
	private static final Logger LOG = LoggerFactory.getLogger(VRaptorServer.class);
	private final Server server;
	private final ContextHandlerCollection contexts;

	public VRaptorServer(String webappDirLocation, String webXmlLocation) {
		this.server = createServer();
		this.contexts = new ContextHandlerCollection();
		reloadContexts(webappDirLocation, webXmlLocation);
	}

	private void reloadContexts(String webappDirLocation, String webXmlLocation) {
		WebAppContext context = loadContext(webappDirLocation, webXmlLocation);
		if ("development".equals(getEnv())) {
			contexts.setHandlers(new Handler[]{context, systemRestart()});
		} else {
			contexts.setHandlers(new Handler[]{context});
		}

	}

	public void start() throws Exception {
		server.setHandler(contexts);
		if (server.isStarted()) server.stop();
		server.start();
	}

	private static WebAppContext loadContext(String webappDirLocation, String webXmlLocation) {
		WebAppContext context = new WebAppContext();
		context.setContextPath(getContext());
		context.setDescriptor(webXmlLocation);
		context.setResourceBase(webappDirLocation);
		context.setParentLoaderPriority(true);
		return context;
	}

	private static String getContext() {
		return System.getProperty("vraptor.context", "/");
	}

	private ContextHandler systemRestart() {
		AbstractHandler system = new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest,
					HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException {
				restartContexts();
				response.setContentType("text/html;charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
				baseRequest.setHandled(true);
				response.getWriter().println("<h1>Done</h1>");
			}
		};
		ContextHandler context = new ContextHandler();
		context.setContextPath("/vraptor/restart");
		context.setResourceBase(".");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.setHandler(system);
		return context;
	}

	private String getEnv() {
		String envVar = System.getenv("VRAPTOR_ENV");
		String environment = envVar != null? envVar : System.getProperty("br.com.caelum.vraptor.environment", "development");
		return environment;
	}

	void restartContexts() {
		try {
			contexts.stop();
			contexts.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Server createServer() {
		// - System.getProperty returns value of a java property that is set by using the -D
		//   option when running the JVM e.g. -DskipTests=true
		// - System.getenv returns value of an environment variable
		// - the java property should override the environment variable because it is more specific to this process
		//   environment variables are shared by many processes
		String webPort = System.getProperty("server.port", System.getenv("PORT"));
		if (webPort == null || webPort.isEmpty())
		{
			webPort = "8080";
		}
		String webHost = System.getProperty("server.host", System.getenv("HOST"));
		if (webHost == null || webHost.isEmpty())
		{
			webHost = "0.0.0.0";
		}
		Server server = new Server(Integer.valueOf(webPort));
		server.getConnectors()[0].setHost(webHost);
		server.setAttribute("jetty.host", webHost);
		LOG.info("starting http server at {}:{}", webHost, webPort);
		return server;
	}

	public void stop() {
		try {
			this.server.stop();
		} catch (Exception e) {
			throw new RuntimeException("Could not stop server", e);
		}
	}
}
