package com.Ebook.catalogue.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.Ebook.catalogue.artifactsApplication;
import com.Ebook.catalogue.db.RepoCalls;
import com.Ebook.catalogue.models.BooksModel;

import jdk.internal.org.jline.utils.Log;

@Path("/book")
@Produces({"application/json"})
public class CurdOperations {

	public BooksModel bookModel = new BooksModel();
	public RepoCalls repo = new RepoCalls();
	public List<BooksModel> booklist = new ArrayList<BooksModel>();
	
	@GET
	@Path("")
	public List<BooksModel> GetBooks() {
		return repo.getBooks();
	}
	
//	@POST
//	@Path("/edit")
//	public Object EditBookDetails(BooksModel book) {
//		Integer ID = repo.EditBook(book);
//		if(ID != 404) {
//			return Response.status(HttpStatus.CREATED_201).entity("success created with id: ID").build();
//		} else {
//			return Response.status(HttpStatus.BAD_REQUEST_400).entity("error").build();
//		}
//	}
	
	@POST
	@Path("/register")
	public Response RegisterBook(BooksModel book) {
		return repo.RegisterBook(book);
	}
	
	@POST
	@Path("/loaner/{bookId}")
	public Response loanedBook(@PathParam("bookId") int id ) {
		return repo.LoanaBook(id);
	}
	
//	@POST
//	@Path("/delete")
//	public Response DeleteBook(BooksModel book) {		
//		return repo.DeleteBook(book);
//	}	
}
