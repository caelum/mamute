package br.com.caelum.pagpag.test.utils;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public abstract class TesteLegivel extends BlockJUnit4ClassRunner {
	@Override
	protected String testName(FrameworkMethod method) {
		return humanize(method.getMethod().getName());
	}

	public TesteLegivel(Class<?> klass) throws InitializationError {
		super(klass);
	}

	private String humanize(String word) {
	    Pattern pattern = Pattern.compile("([A-Z]|[a-z])[a-z]*");

	    Vector<String> tokens = new Vector<String>();
	    Matcher matcher = pattern.matcher(word);
	    String acronym = "";
	    while(matcher.find()) {
	        String found = matcher.group();
	        if(found.matches("^[A-Z]$")) {
	            acronym += found;
	        } else {
	            if(acronym.length() > 0) {
	                //we have an acronym to add before we continue
	                tokens.add(acronym);
	                acronym  = "";
	            }
	            tokens.add(found.toLowerCase());
	        }
	    }
	    if(acronym.length() > 0) {
	        tokens.add(acronym);
	    }
	    if (tokens.size() > 0) {
	        String humanisedString = capitaliseFirstLetter(tokens.remove(0));
	        for (String s : tokens) {
	            humanisedString +=  " " + s;
	        }
	        return humanisedString;
	    }

	    return word;
	}

	private String capitaliseFirstLetter(String texto) {
		// TODO Auto-generated method stub
		return texto.substring(0, 1).toUpperCase() + texto.substring(1);
	}
	
}
