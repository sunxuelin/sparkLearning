package com.powersi.midd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.powersi.midd.mapper")
public class MiddApplication extends SpringBootServletInitializer
{

    public static void main(String[] args) {
        try {
            SpringApplication.run(MiddApplication.class, args);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MiddApplication.class);
    }


}
