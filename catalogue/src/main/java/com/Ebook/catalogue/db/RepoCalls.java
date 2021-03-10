package com.Ebook.catalogue.db;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELException;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.jetty.http.HttpStatus;

import com.Ebook.catalogue.models.BooksModel;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import jdk.internal.org.jline.utils.Log;

public class RepoCalls {

	private MongoClient mongoClient = new MongoClient("localhost", 27017);

	public List<BooksModel> getBooks() {

		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> booksCollection = database.getCollection("Ebook");
		List<BooksModel> booksList = new ArrayList<BooksModel>();

		if (booksCollection != null) {
			return EbookUtill.fromDocument(booksCollection);
//			return Response.status(HttpStatus.OK_200).entity("registered " + booksList).build();
		}

		// We need to return ERROR in this case;
		return EbookUtill.fromDocument(booksCollection);
//		return Response.status(HttpStatus.OK_200).entity("registered " + booksList).build();
	}

	public Response RegisterBook(BooksModel book) {
		if (book != null) {
			MongoDatabase database = this.mongoClient.getDatabase("test");
			MongoCollection<Document> booksCollection = database.getCollection("Ebook");

			BasicDBObject queryLoginSalt = new BasicDBObject("bookId", book.getBookId());
			FindIterable<Document> DD = booksCollection.find(queryLoginSalt);

			FindIterable<Document> fi = booksCollection.find();
			MongoCursor<Document> cursor = fi.iterator();
			while (cursor.hasNext()) {
				if (cursor.next().getInteger("bookId") == book.getBookId()) {
					return Response.status(HttpStatus.BAD_REQUEST_400)
							.entity("book already existed" + fi + cursor + "  " + DD).build();
				}
			}
			Document document = EbookUtill.ToDocument(book);
			booksCollection.insertOne(document);
			return Response.status(HttpStatus.OK_200).entity("registered").build();
		}
		return Response.status(HttpStatus.BAD_REQUEST_400).entity("book details not goven").build();
	}

	public Response LoanaBook(int Id) {
		System.out.println(Id);
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> booksCollection = database.getCollection("Ebook");

		if (booksCollection != null) {

			BasicDBObject queryLoginSalt = new BasicDBObject("bookId", Id);

			Document fi = booksCollection.find(queryLoginSalt).first();
			if (fi != null) {
				Integer quan = fi.getInteger("quantity");
				if (quan > 0) {
					quan = quan - 1;
					fi.put("quantity", quan);
					booksCollection.updateOne(queryLoginSalt, new Document("$set", new Document("quantity", quan)));
					return Response.status(HttpStatus.OK_200).entity("successfully loaned left over quantity:" + quan)
							.build();
				} else {
					return Response.status(HttpStatus.BAD_REQUEST_400).entity("book out of stock").build();
				}
			} else {
				return Response.status(HttpStatus.BAD_REQUEST_400).entity("book not found ").build();
			}
		}
		return Response.status(HttpStatus.BAD_REQUEST_400).entity("collection is empty").build();
	}

//	public Integer EditBook(BooksModel book) {
//		MongoDatabase database = this.mongoClient.getDatabase("test");
//		MongoCollection<Document> booksCollection = database.getCollection("Ebook");
//
//		if (book != null) {
//			Document doc = booksCollection.find(queryLoginSalt).first();
//			if (doc != null) {
//				try {
//					booksCollection.insertOne(EbookUtill.ToDocument(book));
//				} catch (Exception e) {
//					return 404;
//				}
//				return 1;
//			}
//		}
//		return 404;
//	}
//
	public Response DeleteBook(Integer id) {
		MongoDatabase database = this.mongoClient.getDatabase("test");
		MongoCollection<Document> booksCollection = database.getCollection("Ebook");

		if (booksCollection != null) {
			BasicDBObject queryLoginSalt = new BasicDBObject("bookId", id);
			Document doc = booksCollection.find(queryLoginSalt).first();
			if (doc != null) {
				try {
					booksCollection.findOneAndDelete(queryLoginSalt);
					return Response.status(HttpStatus.OK_200)
							.entity("successfully deleted" + booksCollection + database).build();
				} catch (Exception e) {
					return Response.status(HttpStatus.BAD_REQUEST_400).entity("not deleted coz book not found").build();
				}
			}
		}
		return Response.status(HttpStatus.BAD_REQUEST_400)
				.entity("not deleted coz collection is " + booksCollection + database).build();
	}
}
