package com.github.sibdevtools.web.app.bytes2png.api;


/**
 * Encode base64 content into PNG
 *
 * @author sibmaks
 * @since 0.0.1
 */
public record EncodeRq(
        Integer width,
        Integer height,
        String content,
        Boolean useGZIP
) {
}
