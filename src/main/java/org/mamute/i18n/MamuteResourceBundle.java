package org.mamute.i18n;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;


@Vetoed
public class MamuteResourceBundle extends ResourceBundle {

	private final ResourceBundle customBundle;
	private final ResourceBundle mamuteBundle;

	/**
	 * @deprecated CDI eyes only
-	 */
	protected MamuteResourceBundle() {
		this(null, null);
	}
	
	@Inject
	public MamuteResourceBundle(ResourceBundle customBundle, ResourceBundle mamuteBundle) {
		this.customBundle = customBundle;
		this.mamuteBundle = mamuteBundle;
	}

	@Override
	protected Object handleGetObject(String key) {
		if(customBundle.containsKey(key)) return customBundle.getObject(key);
		if(mamuteBundle.containsKey(key)) return mamuteBundle.getObject(key);
		return "???" + key + "???";
	}

	@Override
	public Enumeration<String> getKeys() {
		Enumeration<String> defaultKeys = customBundle.getKeys();
		Enumeration<String> mamuteKeys = mamuteBundle.getKeys();
		Vector<String> allKeys = new Vector<>();

		while (defaultKeys.hasMoreElements()) {
			allKeys.add(defaultKeys.nextElement());
		}
		
		while (mamuteKeys.hasMoreElements()) {
			allKeys.add(mamuteKeys.nextElement());
		}
		
		return allKeys.elements();
	}

}
