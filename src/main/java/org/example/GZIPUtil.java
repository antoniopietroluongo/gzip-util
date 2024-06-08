package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPUtil {

    public static String compressToBase64(Object obj) throws IOException {
        try (var bos = new ByteArrayOutputStream()) {
            try {
                var gzipOutputStream = new GZIPOutputStream(bos);
                var bytes = new ObjectMapper().writeValueAsBytes(obj);
                gzipOutputStream.write(bytes);
                gzipOutputStream.close();
                return Base64.getEncoder().encodeToString(bos.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> T decompressFromBase64(String s, Class<T> clazz) throws IOException {
        try (var byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(s));
             var gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            return new ObjectMapper().readValue(gzipInputStream.readAllBytes(), clazz);
        }
    }
}
