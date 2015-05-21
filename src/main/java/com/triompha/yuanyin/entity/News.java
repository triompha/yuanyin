package com.triompha.yuanyin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
@Entity
@Table(name = "news" ,catalog="yuanyin")
public class News {
    
    private Integer id;
    private Integer srcId;
    private String title;
    private String content;
    private Date createTime;
    private Date modifyTime;
    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * @return the srcId
     */
    @Column(unique=true)
    public Integer getSrcId() {
        return srcId;
    }
    /**
     * @param srcId the srcId to set
     */
    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }
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
    @Type(type="text")  
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return the createTime
     */
//    @Column(columnDefinition="timestamp default now()")
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * @return the modifyTime
     */
    @Column(columnDefinition="timestamp default now()")
    public Date getModifyTime() {
        return modifyTime;
    }
    /**
     * @param modifyTime the modifyTime to set
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
