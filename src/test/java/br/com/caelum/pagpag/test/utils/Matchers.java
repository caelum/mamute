package br.com.caelum.pagpag.test.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Matchers {
	public static Matcher<String> temMesmaEstrutura(final String expected){
	    return new BaseMatcher<String>() {

	        protected String theExpected = expected;

	        @Override
	        public boolean matches(Object obj) {
	        	String xml = (String) obj;
	            return semEspacos(theExpected).equals(semEspacos(xml));
	        }

	        @Override
	        public void describeTo(Description description) {
	            description.appendText(theExpected);
	        }
	        

	    	private String semEspacos(String xml) {
	    		String[] linhas = xml.split("\n");
	    		
	    		StringBuilder xmlLimpo = new StringBuilder();
	    		for (String linha : linhas) {
	    			xmlLimpo.append(linha.trim());
	    		}
	    		return xmlLimpo.toString();
	    	}
	    };
	}
}
