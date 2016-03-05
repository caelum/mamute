package org.mamute.sanitizer;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

@ApplicationScoped
public class MamutePolicyProducer {

	private HtmlElementsBuilder builder;
	private PolicyFactory policy;

	/**
	 * @deprecated CDI eyes only
	 */
	public MamutePolicyProducer() {
	}
	
	@Inject
	public MamutePolicyProducer(HtmlElementsBuilder builder) {
		this.builder = builder;
	}

	@PostConstruct
	public void setUp(){
		List<HtmlElement> allowedElements = builder.build();
		HtmlPolicyBuilder policyBuilder = new HtmlPolicyBuilder();
		for (HtmlElement htmlElement : allowedElements) {
			htmlElement.configure(policyBuilder);
		}
		policy = policyBuilder
				.allowUrlProtocols("https", "http")
				.requireRelNofollowOnLinks()
				.toFactory();
	}

	@Produces
	public PolicyFactory getInstance(){
		return policy;
	}
}
