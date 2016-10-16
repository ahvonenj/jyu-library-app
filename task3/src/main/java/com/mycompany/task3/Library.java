package com.mycompany.task3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class Library
{
    private static List<Bookshelf> bookshelves;
    public static List<Book> floatingBooks; // Books that do not belong in any bookshelf
    
    static 
    {
        bookshelves = new ArrayList<Bookshelf>();
        floatingBooks = new ArrayList<Book>();
    }
    
    public static boolean addBookshelf(String booktype)
    {
        if(!hasBookshelfOfType(booktype))
        {
            bookshelves.add(new Bookshelf(booktype));
            return true;
        }
        
        return false;
    }
    
    public static boolean hasBookshelfOfType(String booktype)
    {
        if(bookshelves.isEmpty())
            return false;
        
        for (Bookshelf bs : bookshelves) 
        {
            if(bs.getBooktype().equalsIgnoreCase(booktype))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public static Bookshelf getBookshelfOfType(String booktype)
    {
        for (Bookshelf bs : bookshelves) 
        {
            if(bs.getBooktype().equalsIgnoreCase(booktype))
            {
                return bs;
            }
        }
        
        return null;
    }
    
    public static boolean addBookToShelf(String booktype, Book book)
    {
        if(!hasBookshelfOfType(booktype))
            throw new IllegalArgumentException("Shelf of type `" + booktype + "` does not exist in library");
        
        return getBookshelfOfType(booktype).addBook(book);
    }

    public static List<Bookshelf> getBookshelves()
    {
        return bookshelves;
    }
}
