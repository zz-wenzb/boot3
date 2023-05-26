package com.wenzb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestService.class) //需要引入对应的类
public class JavaGradleTest {
    @Autowired
    private TestService service;

    @Test
    public void test() {
        System.out.println(service.test());
    }
}