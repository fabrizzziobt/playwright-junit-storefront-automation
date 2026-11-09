package com.rfbtinc.playwrightstorefrontautomation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfbtinc.playwrightstorefrontautomation.model.ProductItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class ProductDataLoader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ProductDataLoader() {
    }

    public static List<ProductItem> loadProducts() {
        String filePath = LocaleResolver.productDataFile();

        try (InputStream inputStream =
                     ProductDataLoader.class.getClassLoader().getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new IllegalStateException("Product data file not found on classpath: " + filePath);
            }

            return OBJECT_MAPPER.readValue(inputStream, new TypeReference<List<ProductItem>>() {});
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to parse product data file: " + filePath + " | Root cause: " + e.getMessage(), e);
        }
    }
}
