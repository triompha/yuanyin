package com.triompha.yuanyin.service;




import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.triompha.yuanyin.entity.News;



@Component(value="newsWorm")
public class NewsWorm extends TimerTask {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(NewsWorm.class);
	
	private static final String searchURL = "http://www.iteye.com/news/";
	private static final Integer errorRoundSize = 8;
	
	@Autowired
	private NewsService newsService;
	
	
	public static FilterContentMap filterContent(String html){
	    if(html==null){
	        return null;
	    }
	    Document doc = Jsoup.parse(html);
	    Elements title = doc.select("title");
	    String titleText = title.text();
	    if(titleText==null || titleText.contains("404")){
	        return null;
	    }
        Element element = doc.select("div#news_content").get(0);
        String content = element.html();
        return new FilterContentMap(titleText, content); 
	}
	
	
	

	
	
	public void init() throws InterruptedException{
        News lastNews = newsService.getLastNews();
        int lastSrcId = (lastNews == null) ? 30000 : lastNews.getSrcId()+1;
        int initErrorCount = errorRoundSize;
        int maxCount = 30;

        while (initErrorCount > 0 && maxCount>0) {
            String searchContext = getHtmlByUrl(searchURL + lastSrcId);
            FilterContentMap filterContent = filterContent(searchContext);
            if (filterContent == null) {
                initErrorCount--;
            } else {
                newsService.insert(lastSrcId, filterContent.getTitle(), filterContent.getContent());
            }
            lastSrcId++;
           maxCount--; 
            //休眠1秒钟
            Thread.currentThread().sleep(2000);
        }
	}
	    
	
	
	public static void main(String[] args) {
		NewsWorm worm = new NewsWorm();
		String html = getHtmlByUrl(searchURL + 30548);
		System.out.println(filterContent(html));
	}

	

	
	/**
	 * 根据URL获得所有的html信息
	 * 
	 * @param url
	 * @return
	 */
	public static String getHtmlByUrl(String url) {
		String html = null;
		
		RequestConfig requestConfig = RequestConfig.custom().
		        setSocketTimeout(1000).setConnectTimeout(1000).build();	
		HttpClient httpClient = new  DefaultHttpClient();
		httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);	
		httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);	
		HttpGet httpget = new HttpGet(url);// 以get方式请求该URL
		try {
		    httpget.setConfig(requestConfig);
			HttpResponse responce = httpClient.execute(httpget);// 得到responce对象
			int resStatu = responce.getStatusLine().getStatusCode();// 返回码
			if (resStatu == HttpStatus.SC_OK) {// 200正常 其他就不对
				// 获得相应实体
				HttpEntity entity = responce.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity);// 获得html源代码
				}
			}
		} catch (Exception e) {
			System.out.println("访问【" + url + "】出现异常!");
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return html;
	}
	
	
	public static class FilterContentMap{
	    private String title ;
	    private String content;
        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }
        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }
        /**
         * @return the content
         */
        public String getContent() {
            return content;
        }
        /**
         * @param content the content to set
         */
        public void setContent(String content) {
            this.content = content;
        }
        public FilterContentMap(String title, String content) {
            super();
            this.title = title;
            this.content = content;
        }
        @Override
        public String toString() {
            return "FilterContentMap [title=" + title + ", content=" + content + "]";
        }
	    
        
	}

    @Override
    public void run() {
        try {
           init(); 
        } catch (Exception e) {
            LOGGER.error("error when worm news",e);
        }
        
    }
	
}
