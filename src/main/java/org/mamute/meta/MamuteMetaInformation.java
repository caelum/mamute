package org.mamute.meta;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.model.interfaces.Flaggable;

@ApplicationScoped
public class MamuteMetaInformation {

	private List<Class<? extends Flaggable>> moderatableTypes = new ArrayList<>();

	public void add(Class<? extends Flaggable> clazz) {
		moderatableTypes.add(clazz);
	}

	public List<Class<? extends Flaggable>> getFlaggableTypes() {
		return moderatableTypes;
	}

}
