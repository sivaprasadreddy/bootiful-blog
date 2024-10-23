package com.sivalabs.blog.utils;

public class Utilities {
    public static String atoSlug(String value) {
        String title = value.trim().toLowerCase();
        String slug = title.replaceAll("\\s+", "-");
        slug = slug.replaceAll("[^A-Za-z0-9]", "-");
        slug = slug.replaceAll("-+", "-");
        return slug;
    }
}
