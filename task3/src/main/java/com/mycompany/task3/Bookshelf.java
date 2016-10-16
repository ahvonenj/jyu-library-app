package com.mycompany.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class Bookshelf
{
    private UUID id;
    private String booktype;
    private List<Book> books;
    
    public Bookshelf(String booktype, ArrayList<Book> books)
    {
        this.id = UUID.randomUUID();
        this.books = books;
    }
    
    public Bookshelf(String booktype)
    {
        this.id = UUID.randomUUID();
        this.booktype = booktype;
        books = new ArrayList<Book>();
    }
    
    public String getBooktype()
    {
        return booktype;
    }

    public List<Book> getBooks()
    {
        return books;
    }
    
    public boolean addBook(Book book)
    {
        if(!books.contains(book))
        {
            books.add(book);
            return true;
        }
        
        return false;
    }
    
    public void removeBook(Book b)
    {
        books.remove(b);
    }
}
