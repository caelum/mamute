package org.mamute.managers;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.TagDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoggedUser;
import org.mamute.model.Tag;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class TagsManagerTest {
	private TagsManager sut;
	private Environment environment = mock(Environment.class);
	private TagDAO tags = mock(TagDAO.class);
	private LoggedUser user = mock(LoggedUser.class);
	private Validator validator = mock(Validator.class);
	private MessageFactory messageFactory = mock(MessageFactory.class);

	@Before
	public void setup() {
		when(environment.get("tags.sanitizer.regex")).thenReturn("[a-zA-Z0-9-]");
		sut = new TagsManager(environment, tags, user, validator, messageFactory);
	}

	@Test
	public void should_prevent_creation_of_nonalpha_tag() {
		when(environment.supports("feature.tags.add.anyone")).thenReturn(true);
		sut.findOrCreate(Arrays.asList("mysql,java"));
		verify(validator, times(1)).add(any(Message.class));
		verify(tags, never()).saveIfDoesntExists(any(Tag.class));
	}

	@Test
	public void should_allow_creation_of_alpha_tag() {
		when(environment.supports("feature.tags.add.anyone")).thenReturn(true);
		sut.findOrCreate(Arrays.asList("mysql", "java"));
		verify(validator, never()).add(any(Message.class));
		verify(tags, times(2)).saveIfDoesntExists(any(Tag.class));
	}
}
