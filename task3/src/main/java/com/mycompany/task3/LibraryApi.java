package com.mycompany.task3;

import io.swagger.annotations.Api;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.plexus.util.StringUtils;

@Api
@Path("/library")
public class LibraryApi
{
    @UserAuth
    @GET
    @Path("/bookshelf/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookshelves()
    {
        List<Bookshelf> bookshelves = Library.getBookshelves();
        
        return Response.status(200)
                    .entity(new DataResponse(false, 0, bookshelves)).build();
    }
    
    @AdminAuth
    @GET
    @Path("/bookshelf/floating/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFloatingBooks()
    {
        if(Library.floatingBooks.isEmpty())
            return Response.status(200)
                    .entity(new BasicResponse(true, 220)).build();
        else
            return Response.status(200)
                    .entity(new DataResponse(false, 0, Library.floatingBooks)).build();
    }
    
    @AdminAuth
    @POST
    @Path("/bookshelf/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBookshelf(@FormParam("booktype") String booktype)
    {
        if(StringUtils.isBlank(booktype))
            return Response.status(200)
                    .entity(new BasicResponse(true, 100)).build();
        
        if(Library.addBookshelf(booktype))
        {
            return Response.status(200)
                    .entity(new BasicResponse(false, 0, "Bookshelf added!"))
                    .build();
        }
        else
        {
            return Response.status(200)
                    .entity(new BasicResponse(true, 210)).build();
        }
    }
    
    @AdminAuth
    @POST
    @Path("/bookshelf/book/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBookToShelf(
            @FormParam("shelf") final String shelf,
            @FormParam("isbn") String isbn, 
            @FormParam("cover") String cover, 
            @FormParam("content") String content, 
            @FormParam("pages") int pages, 
            @FormParam("author") String author, 
            @FormParam("published") String published
    )
    {
        if(StringUtils.isBlank(shelf))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(isbn))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(cover))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(content))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(author))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(published))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(pages == 0)
            return Response.status(200).entity(new BasicResponse(true, 100, "Book cannot have zero pages")).build();
        
        boolean addedShelf = false;
        
        if(!Library.hasBookshelfOfType(shelf))
        {
            Library.addBookshelf(shelf);
            addedShelf = true;
        }
        
        final Book b = new Book(isbn, cover, content, pages, author, published);
      
        Library.addBookToShelf(shelf, b);

        if(addedShelf)
        {
            return Response.status(200)
                .entity(new DataResponse(
                        false,
                        0,
                        "Book created and added to a new shelf",
                        new HashMap<String, Object>() // Hack
                        {{
                            put("Shelf", shelf);
                            put("Book", b);
                        }}
                )).build();
        }
        else
        {
            return Response.status(200)
                .entity(new DataResponse(false, 0, "Book created and added to existing shelf", new Object[] {shelf, b}))
                .build();
        }
    }
    
    @AdminAuth
    @POST
    @Path("/book/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newBook(
            @FormParam("isbn") String isbn, 
            @FormParam("cover") String cover, 
            @FormParam("content") String content, 
            @FormParam("pages") int pages, 
            @FormParam("author") String author, 
            @FormParam("published") String published
    )
    {
        if(StringUtils.isBlank(isbn))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(cover))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(content))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(author))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(published))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(pages == 0)
            return Response.status(200).entity(new BasicResponse(true, 100, "Book cannot have zero pages")).build();
        
        Book b = new Book(isbn, cover, content, pages, author, published);
      
        Library.floatingBooks.add(b);

        return Response.status(200)
                .entity(new DataResponse(false, 0, "Book created", b))
                .build();
    }
   
    @AdminAuth
    @DELETE
    @Path("/book/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") String id)
    {
        if(StringUtils.isBlank(id))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(Library.getBookshelves().isEmpty())
            return Response.status(200).entity(new BasicResponse(true, 230, "No bookshelves exist")).build();
        
        boolean wasRemoved = false;
        
        UUID uuid;
        
        try
        {
            uuid = UUID.fromString(id);
        }
        catch(IllegalArgumentException iae)
        {
            return Response.status(200).entity(new BasicResponse(true, 100, "Book id `" + id + "` is not valid")).build();
        }
        
        
        breakto:
        for(Bookshelf bs : Library.getBookshelves())
        {
            for(Book b : bs.getBooks())
            {
                if(b.getId().compareTo(uuid) == 0)
                {
                    bs.removeBook(b);
                    wasRemoved = true;
                    break breakto;
                }
            }
        }
        
        if(wasRemoved)
        {
            return Response.status(200).entity(new BasicResponse(false, 0, "Book with id `" + id + "` removed")).build();
        }
        else
        {
            return Response.status(200).entity(new BasicResponse(true, 230, "No book could be found")).build();
        }
    }
    
    @UserAuth
    @GET
    @Path("/book/{searchstring}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchBook(@PathParam("searchstring") String searchstring)
    {
        if(StringUtils.isBlank(searchstring))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(Library.getBookshelves().isEmpty())
            return Response.status(200).entity(new BasicResponse(true, 230, "No bookshelves exist")).build();
       
        List<Book> foundBooks = new ArrayList<Book>();
        
        breakto:
        for(Bookshelf bs : Library.getBookshelves())
        {
            for(Book b : bs.getBooks())
            {
                if(b.getAuthor().contains(searchstring) ||
                   b.getCover().contains(searchstring) ||
                   b.getIsbn().contains(searchstring))
                {
                    foundBooks.add(b);
                }
            }
        }
        
        if(foundBooks.isEmpty())
        {
            return Response.status(200).entity(new BasicResponse(true, 230, "No books found")).build();
        }
        else
        {
            return Response.status(200)
                .entity(new DataResponse(false, 0, "Found book(s)", foundBooks))
                .build();
        }
    }
    
    @AdminAuth
    @PUT
    @Path("/book/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(
            @PathParam("id") String id,
            @FormParam("isbn") String isbn, 
            @FormParam("cover") String cover, 
            @FormParam("content") String content, 
            @FormParam("pages") int pages, 
            @FormParam("author") String author, 
            @FormParam("published") String published
    )
    {
        if(StringUtils.isBlank(id))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        // Ugly, but I want to make these optional
        boolean edit_isbn = false;        
        boolean edit_cover = false;
        boolean edit_content = false;
        boolean edit_author = false;
        boolean edit_published = false;
        boolean edit_pages = false;
        
        if(!StringUtils.isBlank(isbn))
            edit_isbn = true;
        if(!StringUtils.isBlank(cover))
            edit_cover = true;
        if(!StringUtils.isBlank(content))
            edit_content = true;
        if(!StringUtils.isBlank(author))
            edit_author = true;
        if(!StringUtils.isBlank(published))
            edit_published = true;
        if(pages != 0)
            edit_pages = true;
        
        UUID uuid;
        
        try
        {
            uuid = UUID.fromString(id);
        }
        catch(IllegalArgumentException iae)
        {
            return Response.status(200).entity(new BasicResponse(true, 100, "Book id `" + id + "` is not valid")).build();
        }
        
        boolean wasEdited = false;
        
        breakto:
        for(Bookshelf bs : Library.getBookshelves())
        {
            for(Book b : bs.getBooks())
            {
                if(b.getId().compareTo(uuid) == 0)
                {
                    if(edit_isbn)    
                        b.setIsbn(isbn);
                    if(edit_cover)
                        b.setCover(cover);
                    if(edit_content)
                        b.setContent(content);
                    if(edit_author)
                        b.setAuthor(author);
                    if(edit_published)
                        b.setPublished(published);
                    if(edit_pages)
                        b.setPages(pages);
                    
                    wasEdited = true;
                    break breakto;
                }
            }
        }
        
        if(wasEdited)
        {
            return Response.status(200).entity(new BasicResponse(false, 0, "Book edited")).build();
        }
        else
        {
            return Response.status(200).entity(new BasicResponse(true, 230, "No book found")).build();
        }
    }
    
    @CreatorAuth
    @POST
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("email") String email,
            @FormParam("role") List<User.ROLE> roles)
    {
        if(StringUtils.isBlank(firstname))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(lastname))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(username))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(password))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(StringUtils.isBlank(email))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(roles == null || roles.isEmpty())
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        if(UserRepository.addUser(firstname, lastname, username, password, email, roles))
        {
            return Response.status(200)
                    .entity(new BasicResponse(false, 0, "User added!"))
                    .build();
        }
        else
        {
            return Response.status(200)
                    .entity(new BasicResponse(true, 210)).build();
        }
    }
    
    @AdminAuth
    @GET
    @Path("/user/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers()
    {
        return Response.status(200)
                    .entity(new DataResponse(false, 0, UserRepository.getUsers())).build();
    }
    
    @AdminAuth
    @GET
    @Path("/user/uuid/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUUID(
            @PathParam("userid") String userid
    )
    {
        if(StringUtils.isBlank(userid))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();

        UUID uuid;
        
        try
        {
            uuid = UUID.fromString(userid);
        }
        catch(IllegalArgumentException iae)
        {
            return Response.status(200).entity(new BasicResponse(true, 100, "User id `" + userid + "` is not valid")).build();
        }
        
        User user = UserRepository.getUserByUUID(uuid);
        
        if(user == null)
        {
            return Response.status(200).entity(new BasicResponse(true, 230, "No user found")).build();
        }
        else
        {
            return Response.status(200)
                .entity(new DataResponse(false, 0, "Found user", user))
                .build();
        }
    }
    
    @UserAuth
    @GET
    @Path("/user/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(
            @PathParam("username") String username
    )
    {
        if(StringUtils.isBlank(username))
            return Response.status(200).entity(new BasicResponse(true, 100)).build();
        
        User user = UserRepository.getUserByUsername(username);
        
        if(user == null)
        {
            return Response.status(200).entity(new BasicResponse(true, 230, "No user found")).build();
        }
        else
        {
            return Response.status(200)
                .entity(new DataResponse(false, 0, "Found user", user))
                .build();
        }
    }
}
