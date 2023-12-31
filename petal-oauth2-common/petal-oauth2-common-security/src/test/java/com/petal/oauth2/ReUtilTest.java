package com.petal.oauth2;

import cn.hutool.core.util.ReUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

@SpringBootTest
public class ReUtilTest {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    @Test
    public void test(){
        String result = ReUtil.
                replaceAll("/user/{username}/{password}", PATTERN, "*");
        System.out.println(result);

    }

}
