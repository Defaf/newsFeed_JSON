package com.dhaffaf.quicknews;

/**
 * Created by WIN8 on 04/01/18.
 */

public class News {
    private String title;
    private String section;
    private String authorF;//author first name
    private String date;
    private String url;
    private String type;

    public News(String type,String title, String section, String authorF, String date, String url) {
        this.type = type;
        this.title = title;
        this.section = section;
        this.authorF = authorF;
        this.date = date;
        this.url = url;
    }
    public String getType() {return type;}

    public String getTitle() { return title; }

    public String getAuthorF() {return authorF;}

    public String getSection() { return section; }

    public String getDate() { return date; }

    public String getUrl() { return url; }
}
