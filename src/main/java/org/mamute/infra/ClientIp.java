package org.mamute.infra;

import com.google.common.base.Objects;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class ClientIp {

	@Inject
	private HttpServletRequest request;

	private String ip;

	@PostConstruct
	public void setup() {
		String header = request.getHeader("X-Real-IP");
		String ip = request.getRemoteAddr();
		this.ip = Objects.firstNonNull(header, ip);
	}

	public String get() {
		return ip;
	}
}
