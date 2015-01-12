package org.mamute.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class TagTest {

    private User user;
    private Tag subject;
    private String description;
    private String name;

    @Before
    public void setup()
    {
        name = "Tag With spêcial!Chars";
        description = "Great desc";
        user = mock(User.class);
        subject = new Tag(name, description, user);
    }

    @Test
    public void testGetName()
    {
        assertEquals(subject.getName(), name.toLowerCase());
    }

    @Test
    public void testGetDescription()
    {
        assertEquals(subject.getDescription(), description);
    }

    @Test
    public void testGetUriName()
    {
        assertEquals(subject.getUriName(), "tag-with-specialchars");
    }
}