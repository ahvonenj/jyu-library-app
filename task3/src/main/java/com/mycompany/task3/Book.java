package com.mycompany.task3;

import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class Book
{
    private UUID id;
    private String isbn;
    private String cover;
    private String content;
    private int pages;
    private String author;
    private String published;
    
    public Book(String isbn, String cover, String content, int pages, String author, String published)
    {
        this.id = UUID.randomUUID();
        this.isbn = isbn;
        this.pages = pages;
        this.cover = cover;
        this.content = content;
        this.author = author;
        this.published = published;
    }
    
    public Book(String isbn, String cover, String content, int pages)
    {
        this.id = UUID.randomUUID();
        this.isbn = isbn;
        this.pages = pages;
        this.cover = cover;
        this.content = content;
        this.author = "Unknown";
        this.published = "Unknown";
    }

    public String getIsbn()
    {
        return isbn;
    }

    public String getCover()
    {
        return cover;
    }

    public String getContent()
    {
        return content;
    }

    public int getPages()
    {
        return pages;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getPublished()
    {
        return published;
    }

    public UUID getId()
    {
        return id;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setPages(int pages)
    {
        this.pages = pages;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setPublished(String published)
    {
        this.published = published;
    }
    
    
    
    
}
