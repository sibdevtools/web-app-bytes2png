package com.github.sibdevtools.web.app.bytes2png.service;

import com.github.sibdevtools.web.app.bytes2png.exception.UnexpectedErrorException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class WebAppBytes2PngService {

    /**
     * Convert bytes to png image
     *
     * @param width  excepted width of image
     * @param height excepted height of image
     * @param bytes  bytes to convert
     * @return resulted image
     */
    public BufferedImage encode(Integer width, Integer height, byte[] bytes) {
        var buffer = serialize(bytes);
        var bufferSize = buffer.length;
        var pixels = bufferSize / 3.;

        if (width == null && height == null) {
            var size = (int) Math.ceil(Math.sqrt(pixels));
            width = size;
            height = size;
        } else if (width == null) {
            width = (int) Math.ceil(pixels / height);
        } else if (height == null) {
            height = (int) Math.ceil(pixels / width);
        }

        if (buffer.length > width * height * 3) {
            throw new UnexpectedErrorException("Need to increase image size");
        }

        return createImage(width, height, buffer);
    }

    private static BufferedImage createImage(Integer width, Integer height, byte[] buffer) {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        var graphics = image.createGraphics();

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (index >= buffer.length) break;

                int r = buffer[index++] + 128;
                int g = (index < buffer.length) ? buffer[index++] + 128 : 128;
                int b = (index < buffer.length) ? buffer[index++] + 128 : 128;

                graphics.setColor(new Color(r, g, b));
                graphics.fillRect(x, y, 1, 1);
            }
        }
        graphics.dispose();
        return image;
    }

    private static byte[] serialize(byte[] bytes) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        try (var gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
             var objectOutputStream = new ObjectOutputStream(gzipOutputStream)) {
            objectOutputStream.writeInt(bytes.length);
            objectOutputStream.write(bytes);
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't serialize image", e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Decode PNG image to bytes
     *
     * @param pngBytes PNG bytes
     * @return decoded bytes
     */
    public byte[] decode(byte[] pngBytes) {
        var byteArrayInputStream = new ByteArrayInputStream(pngBytes);
        BufferedImage image;
        try {
            image = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't read image", e);
        }
        return toBytes(image);
    }

    private static byte[] toBytes(BufferedImage image) {
        var width = image.getWidth();
        var height = image.getHeight();
        var byteOutputStream = new ByteArrayOutputStream(width * height * 3);
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                var rgb = image.getRGB(x, y);
                var color = new Color(rgb);
                byteOutputStream.write(color.getRed() - 128);
                byteOutputStream.write(color.getGreen() - 128);
                byteOutputStream.write(color.getBlue() - 128);
            }
        }

        var bytes = byteOutputStream.toByteArray();
        return deserialize(bytes);
    }

    private static byte[] deserialize(byte[] bytes) {
        var byteArrayInputStream = new ByteArrayInputStream(bytes);
        try (var gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             var objectInputStream = new ObjectInputStream(gzipInputStream)) {
            var length = objectInputStream.readInt();
            var buffer = new byte[length];
            objectInputStream.readFully(buffer);
            return buffer;
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't deserialize bytes", e);
        }
    }
}
