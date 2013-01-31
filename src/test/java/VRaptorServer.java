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

	public VRaptorServer(String webappDirLocation) {
		this.server = createServer();
		this.contexts = new ContextHandlerCollection();
		reloadContexts(webappDirLocation);
	}

	private void reloadContexts(String webappDirLocation) {
		WebAppContext context = loadContext(webappDirLocation);
		contexts.setHandlers(new Handler[] { context, systemRestart() });
	}

	public void start() throws Exception {
		server.setHandler(contexts);
		server.start();
	}

	private static WebAppContext loadContext(String webappDirLocation) {
		WebAppContext context = new WebAppContext();
		context.setContextPath(getContext());
		context.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		context.setResourceBase(webappDirLocation);
		context.setParentLoaderPriority(false);
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
			webPort = "8080";
		}
		Server server = new Server(Integer.valueOf(webPort));
		return server;
	}

	private static String getPort() {
		return System.getenv("PORT");
	}

}
