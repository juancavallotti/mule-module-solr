package org.mule.modules.pojos;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created with IntelliJ IDEA.
 * User: juancavallotti
 * Date: 09/03/13
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public class WebPagePojo {

    @Field
    private String id;

    @Field
    private String url;

    @Field
    private String title;

    @Field
    private String content;

    public WebPagePojo() {
    }

    public WebPagePojo(String url, String title, String content) {
        this.url = url;
        this.id = url;
        this.title = title;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
