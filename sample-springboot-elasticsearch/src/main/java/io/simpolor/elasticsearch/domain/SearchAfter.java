package io.simpolor.elasticsearch.domain;

import lombok.Data;

import java.util.List;

@Data
public class SearchAfter<T> {

    private String searchAfter;

    private int totalCount;

    private List<T> content;

    public SearchAfter(List<T> content, String searchAfter, int totalCount) {
        this.content = content;
        this.searchAfter = searchAfter;
        this.totalCount = totalCount;
    }

}
