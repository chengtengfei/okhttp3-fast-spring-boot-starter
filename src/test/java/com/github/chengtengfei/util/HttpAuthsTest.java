package com.github.chengtengfei.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpAuthsTest {

    @Test
    public void basicAuth() {
        Assert.assertEquals("Basic YWRtaW46YWRtaW4=", HttpAuths.basicAuth("admin", "admin"));
    }
}