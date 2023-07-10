package me.cocos.cocosmines.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Pagination<T> {
    private final List<T> items;
    private final int pageSize;

    public Pagination(int pageSize) {
        this.pageSize = pageSize;
        this.items = new ArrayList<>();
    }

    @SafeVarargs
    public Pagination(int pageSize, T... elements) {
        this(pageSize, Arrays.asList(elements));
    }

    public Pagination(int pageSize, List<T> elements) {
        this.pageSize = pageSize;
        this.items = new ArrayList<>(elements);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        int totalItems = items.size();
        int totalPages = totalItems / pageSize;
        if (totalItems % pageSize != 0) {
            totalPages++;
        }
        return totalPages;
    }

    public boolean hasPage(int page) {
        return page >= 0 && page < getTotalPages();
    }

    public List<T> getPage(int page) {
        if (!this.hasPage(page)) {
            throw new IndexOutOfBoundsException("Unexpected page error: " + page + "/" + this.getPageSize());
        }

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, items.size());

        return new ArrayList<>(items.subList(startIndex, endIndex));
    }
}
