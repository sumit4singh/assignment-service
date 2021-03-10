package com.Ebook.catalogue.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.Ebook.catalogue.models.BooksModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import jdk.internal.org.jline.utils.Log;

public class EbookUtill {

	public EbookUtill() {}
	
	public static List<BooksModel> fromDocument(MongoCollection<Document> doc) {
        List<BooksModel> booksList = new ArrayList<BooksModel>();
        
        FindIterable<Document> fi = doc.find();
		MongoCursor<Document> cursor = fi.iterator();
        while (cursor.hasNext()) {          	
        	Document book = cursor.next();
        	BooksModel book12 = new BooksModel();
        	book12.setBookId(book.getInteger("bookId"));
        	book12.setGenres(book.getString("genres"));
        	book12.setQuantity(book.getInteger("quantity"));
        	book12.setName(book.getString("name"));        	
        	booksList.add(book12);
        }
        return booksList;
    }
	
	public static Document ToDocument(BooksModel book) {
		Document doc = new Document();
		doc.put("bookId", book.getBookId());
		doc.put("genres", book.getGenres());
		doc.put("quantity", book.getQuantity());
		doc.put("name", book.getName());
        return doc;
    }
}
