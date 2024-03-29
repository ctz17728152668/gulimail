package com.ctz.gulimail.search.web;

import com.ctz.gulimail.search.service.MallSearchService;
import com.ctz.gulimail.search.vo.SearchParam;
import com.ctz.gulimail.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {


    @Autowired
    private MallSearchService mallSearchService;


    @GetMapping("/list.html")
    public String list(SearchParam param, Model model, HttpServletRequest request){
        param.set_queryString(request.getQueryString());
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
