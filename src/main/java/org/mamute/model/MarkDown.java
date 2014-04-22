package org.mamute.model;

import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MarkDown {
	
	private static ScriptEngine js;

	static {
		loadJs();
	}

	private static void loadJs() {
		MarkDown.js = new ScriptEngineManager().getEngineByName("javascript");
		try {
			js.eval(new InputStreamReader(MarkDown.class.getResourceAsStream("/marked.js")));
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized static String parse(String content) {
		if(content == null || content.isEmpty()) return null;
		try {
			Invocable invocable = (Invocable) js;
			Object result = invocable.invokeFunction("marked", content);
			return result.toString();
		} catch (ScriptException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
