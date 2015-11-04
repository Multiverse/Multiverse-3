package com.mvplugin.core.destination;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static com.mvplugin.core.destination.DestinationUtil.*;

public class DestinationUtilTest {

    @Test
    public void testNumberRegex() throws Exception {
        Pattern pattern = Pattern.compile(numberRegex("test"));

        Matcher matcher = pattern.matcher(":abc");
        assertFalse(matcher.matches());
        matcher = pattern.matcher("abc");
        assertFalse(matcher.matches());
        matcher = pattern.matcher(":1.2");
        assertFalse(matcher.matches());
        matcher = pattern.matcher(" 3 ");
        assertFalse(matcher.matches());
        matcher = pattern.matcher("");
        assertFalse(matcher.matches());
        matcher = pattern.matcher(" ");
        assertFalse(matcher.matches());
        matcher = pattern.matcher("-1235-951");
        assertFalse(matcher.matches());
        matcher = pattern.matcher(":123.12345:11231");
        assertFalse(matcher.matches());

        matcher = pattern.matcher("1.2");
        assertTrue(matcher.matches());
        matcher = pattern.matcher("50000");
        assertTrue(matcher.matches());
        matcher = pattern.matcher("+50000");
        assertTrue(matcher.matches());
        matcher = pattern.matcher("-50000");
        assertTrue(matcher.matches());
        matcher = pattern.matcher("10023.4554");
        assertTrue(matcher.matches());
        matcher = pattern.matcher("14123456135661241123123124516123156");
        assertTrue(matcher.matches());
        matcher = pattern.matcher("14123456135661241.123123124516123156");
        assertTrue(matcher.matches());

        matcher = pattern.matcher("123");
        assertTrue(matcher.matches());
        assertEquals(2, matcher.groupCount());
        matcher = pattern.matcher("123.12345");
        assertTrue(matcher.matches());
        assertEquals(2, matcher.groupCount());
        assertEquals("123.12345", matcher.group("test"));
    }

    @Test
    public void testColonJoin() throws Exception {
        String joined = colonJoin("", "", "");
        assertEquals("::", joined);
        joined = colonJoin("a", "b", "c");
        assertEquals("a:b:c", joined);
        joined = colonJoin("d", "e");
        assertEquals("d:e", joined);
    }

    @Test
    public void testRemovePrefix() throws Exception {
        String removedPrefix = removePrefix("test");
        assertEquals("test", removedPrefix);
        removedPrefix = removePrefix("test:blah");
        assertEquals("blah", removedPrefix);
        removedPrefix = removePrefix("test:blah:bloo");
        assertEquals("blah:bloo", removedPrefix);
    }
}