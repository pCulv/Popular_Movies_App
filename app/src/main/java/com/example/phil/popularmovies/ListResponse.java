package com.example.phil.popularmovies;

import java.util.List;

/**
 * Created by phil on 4/13/17.
 */

public class ListResponse {
    List<Movie> results;
    int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
