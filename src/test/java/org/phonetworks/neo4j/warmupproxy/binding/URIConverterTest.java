package org.phonetworks.neo4j.warmupproxy.binding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class URIConverterTest {

    private URIConverter subject;

    @Before
    public void setUp() {
        subject = new URIConverter();
    }

    @After
    public void tearDown() {
        subject = null;
    }

    @Test
    public void happy() {
        assertNotNull(subject.convert("https://neo4j.com:80/"));
    }

    @Test
    public void missing() {
        assertNull(subject.convert(null));
        assertNull(subject.convert(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid() {
        assertNull(subject.convert("t!@#!@#!@#trash!!!"));
    }
}