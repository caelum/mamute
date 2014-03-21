package org.mamute.model;

import java.io.InputStreamReader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MarkDown {

	public synchronized static String parse(String content) {
		if(content == null || content.isEmpty()) return null;
		ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");
		Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);
		try {
			js.eval(new InputStreamReader(MarkDown.class.getResourceAsStream("/marked.js")));
			bindings.put("content", content);
			Object result = js.eval("marked(content)");
			return result.toString();
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}

}
