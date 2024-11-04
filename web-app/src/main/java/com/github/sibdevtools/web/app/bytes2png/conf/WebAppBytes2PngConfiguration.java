package com.github.sibdevtools.web.app.bytes2png.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Base64;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@PropertySource("classpath:web/app/bytes2png/application.properties")
public class WebAppBytes2PngConfiguration {

    @Bean("webAppBytes2PngBase64Encoder")
    public Base64.Encoder webAppBytes2PngBase64Encoder() {
        return Base64.getEncoder();
    }

    @Bean("webAppBytes2PngBase64Decoder")
    public Base64.Decoder webAppBytes2PngBase64Decoder() {
        return Base64.getDecoder();
    }

}
