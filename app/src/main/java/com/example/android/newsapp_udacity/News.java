package com.example.android.newsapp_udacity;

/**
 * Created by mikem on 6/6/2017.
 */

public class News {

    private String mTitle;
    private String mSection;
    private String mDatePublished;
    private String mPreviewLink;


    public News(String title, String section, String datePublished, String previewLink){
        mTitle = title;
        mSection = section;
        mDatePublished = datePublished;
        mPreviewLink = previewLink;
    }

    public String getTitle(){ return mTitle; }
    public String getSection(){ return mSection; }
    public String getDatePublished(){ return mDatePublished; }
    public String getPreviewLink(){ return  mPreviewLink; }


}
