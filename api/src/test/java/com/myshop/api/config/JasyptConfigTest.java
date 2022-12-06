package com.myshop.api.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.logging.Logger;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class JasyptConfigTest {

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    StringEncryptor stringEncryptor;

    Logger logger = Logger.getAnonymousLogger();

    @Test
    public void jasyptTest() {
        logger.info(stringEncryptor.encrypt("root"));
        logger.info(stringEncryptor.encrypt("1234"));
    }
}