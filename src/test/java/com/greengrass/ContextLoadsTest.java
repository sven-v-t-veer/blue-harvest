package com.greengrass;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ContextLoadsTest {

    @Test
    void testContextLoads() {
        Assertions.assertTrue(true);
    }
}
