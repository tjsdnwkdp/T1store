package com.T1store.T1store.constant;

import java.util.Arrays;
import java.util.List;

public enum Category {
    SHOP(112, "shop"),
    TEAM_KIT(113, "team-kit"),
    COLLECTION(114, "collection"),
    COLLABORATION(42, "collaboration"),
    NEW(23,"new"),
    SALE(64, "sale");


    private final int id;
    private final String path;

    Category(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }

    // URL 문자열로부터 Enum 객체를 가져오는 메소드
    public static Category fromPath(String path) {
        for (Category category : values()) {
            if (category.getPath().equalsIgnoreCase(path)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category found with path: " + path);
    }

    public static List<Category> getAllSubCategories() {
        return Arrays.asList(TEAM_KIT, COLLECTION, COLLABORATION, NEW, SALE);
    }
}
