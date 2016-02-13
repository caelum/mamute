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

public class VRaptorServer {

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
		String webPort = getPort();
		if (webPort == null || webPort.isEmpty()) {
			webPort = System.getProperty("server.port", "8080");
		}
		Server server = new Server(Integer.valueOf(webPort));
		String webHost = getHost();
		if (webHost == null || webHost.isEmpty()) {
			webHost = System.getProperty("server.host", "0.0.0.0");
		}
		server.getConnectors()[0].setHost(webHost);
		server.setAttribute("jetty.host", webHost);
		return server;
	}

	private static String getPort() {
		String port = System.getenv("PORT");
		return port;
	}
	
	private static String getHost() {
		String host = System.getenv("HOST");
		return host;
	}

	public void stop() {
		try {
			this.server.stop();
		} catch (Exception e) {
			throw new RuntimeException("Could not stop server", e);
		}
	}
}
