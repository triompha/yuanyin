package com.triompha.yuanyin.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.triompha.yuanyin.entity.News;

@Component
public class NewsService {
    
    @Autowired
    private HibernateTemplate hibernateTemplate; 
    
    
    public News getLastNews(){
       hibernateTemplate.setMaxResults(1);  
        List<News> find = hibernateTemplate.find("from News order by srcId desc limit 1");
        return  (find==null||find.size()==0)?null :find.get(0);
    }
    
    public  List<News> listUpNews(Integer srcId, int size){
       hibernateTemplate.setMaxResults(size);  
       return hibernateTemplate.find("from News where srcId>"+srcId+" order by srcId asc" ); 
    }
    
    public  List<News> listDownNews(Integer srcId, int size){
       hibernateTemplate.setMaxResults(size);  
       return hibernateTemplate.find("from News where srcId<"+srcId+" order by srcId desc"); 
    }
    
    public int insert(int srcId ,String title ,String content){
       News news = new News();
       news.setContent(content);
       news.setTitle(title);
       news.setCreateTime(new Date());
       news.setSrcId(srcId);
       Serializable save = hibernateTemplate.save(news);
       return (Integer) save; 
    }
    

}
