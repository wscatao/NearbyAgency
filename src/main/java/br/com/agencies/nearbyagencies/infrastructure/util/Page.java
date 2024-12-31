package br.com.agencies.nearbyagencies.infrastructure.util;

import java.util.List;

public class Page<T> {
    private List<T> items;
    private String nextPageReference;

    public Page(List<T> items, String nextPageReference) {
        this.items = items;
        this.nextPageReference = nextPageReference;
    }

    public List<T> getItems() {
        return items;
    }

    public String getNextPageReference() {
        return nextPageReference;
    }
}
