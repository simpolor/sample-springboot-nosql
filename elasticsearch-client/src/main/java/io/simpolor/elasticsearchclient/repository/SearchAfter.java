package io.simpolor.elasticsearchclient.repository;

import java.util.List;

public class SearchAfter<T> {

    private List<T> results;
    private String nextSearchAfter;
    private int totalCount;

    public SearchAfter(List<T> results, String nextSearchAfter, int totalCount) {
        this.results = results;
        this.nextSearchAfter = nextSearchAfter;
        this.totalCount = totalCount;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public String getNextSearchAfter() {
        return nextSearchAfter;
    }

    public void setNextSearchAfter(String nextSearchAfter) {
        this.nextSearchAfter = nextSearchAfter;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
