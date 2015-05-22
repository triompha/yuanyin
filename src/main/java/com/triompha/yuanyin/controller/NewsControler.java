package com.triompha.yuanyin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.triompha.yuanyin.entity.News;
import com.triompha.yuanyin.service.NewsService;

@Controller
public class NewsControler {
    @Autowired
    private NewsService newsService;

    @RequestMapping("/news/up/{sid}/{size}")
    public @ResponseBody List<News> up(@PathVariable Integer sid, @PathVariable Integer size) {
        return newsService.listUpNews(sid, size);
    }
    @RequestMapping("/news/down/{sid}/{size}")
    public @ResponseBody List<News> down(@PathVariable Integer sid,@PathVariable Integer size) {
        return newsService.listDownNews(sid, size);
    }
    
    @RequestMapping("/news/down/{size}")
    public @ResponseBody List<News> top(@PathVariable Integer size) {
        return newsService.listTop(size);
    }

}
