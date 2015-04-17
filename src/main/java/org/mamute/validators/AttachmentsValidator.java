package org.mamute.validators;

import br.com.caelum.vraptor.validator.Validator;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.mamute.factory.MessageFactory;
import org.mamute.model.Attachment;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static org.mamute.model.Comment.ERROR_NOT_EMPTY;

public class AttachmentsValidator {

	@Inject
	private Validator validator;
	@Inject
	private MessageFactory factory;

	public void validate(List<Attachment> attachments) {
		Collection<String> names = transform(attachments, mapName());
		HashSet<String> namesSet = new HashSet<>(names);
		boolean hasRepeatedName = names.size() != namesSet.size();
		if (hasRepeatedName) {
			validator.add(factory.build("error", "question.errors.attachments.duplicated"));
		}
	}

	private Function<Attachment, String> mapName() {
		return 	new Function<Attachment, String>() {
			@Nullable
			@Override
			public String apply(Attachment a) {
				return a.getName();
			}
		};
	}
}
