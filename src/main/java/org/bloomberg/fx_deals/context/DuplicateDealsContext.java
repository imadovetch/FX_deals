package org.bloomberg.fx_deals.context;

import java.util.List;

public class DuplicateDealsContext {

    private static final ThreadLocal<List<String>> duplicateDeals = new ThreadLocal<>();

    public static void set(List<String> ids) {
        duplicateDeals.set(ids);
    }

    public static List<String> get() {
        return duplicateDeals.get();
    }

    public static void clear() {
        duplicateDeals.remove();
    }
}
