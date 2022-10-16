package com.ctz.gulimail.search.service;

import com.ctz.gulimail.search.vo.SearchParam;
import com.ctz.gulimail.search.vo.SearchResult;

public interface MallSearchService {
    SearchResult search(SearchParam param);
}
