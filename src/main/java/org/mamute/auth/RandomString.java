package org.mamute.auth;

import java.util.Random;

/**
 * Created by siddjain on 10/27/15.
 * http://stackoverflow.com/a/41156/147530
 */
public class RandomString
{

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private static final Random random = new Random();

    public static String generate(int length)
    {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        char[] buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
