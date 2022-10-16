package com.ctz.gulimail.product.web;

import com.ctz.gulimail.product.entity.CategoryEntity;
import com.ctz.gulimail.product.service.CategoryService;
import com.ctz.gulimail.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 跳转首页
     * @param model
     * @return
     */
    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        //查询出所有一级分类信息
        List<CategoryEntity> categoryEntityList = categoryService.queryLevelOne();
        model.addAttribute("categories",categoryEntityList);
        return "index";
    }

    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2Vo>>  queryCatelog(){
        Map<String, List<Catelog2Vo>>  json = categoryService.getcatalogJson();
        return json;
    }

}
