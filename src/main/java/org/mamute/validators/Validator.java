package org.mamute.validators;

/**
 * Created by siddjain on 5/20/16.
 */
public class Validator
{
    public static void notNullOrEmpty(String key, String value)
    {
        if (value == null || value.isEmpty())
        {
            throw new IllegalArgumentException(key + " cannot be null or empty");
        }
    }
}
