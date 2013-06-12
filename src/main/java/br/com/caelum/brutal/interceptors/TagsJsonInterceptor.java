package br.com.caelum.brutal.interceptors;

import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;

import com.thoughtworks.xstream.XStream;

@Intercepts
public class TagsJsonInterceptor implements Interceptor {

	private final TagDAO tags;
	private XStream json;
	private final Result result;

	public TagsJsonInterceptor(XStreamBuilder builder, TagDAO tags, Result result) {
		this.result = result;
		json = builder.withoutRoot().jsonInstance();
		this.tags = tags;
	}
	
	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object obj) throws InterceptionException {
		String allTags = json.toXML(tags.all());
		result.include("allTags", allTags);
		stack.next(method, obj);
	}

}
