package com.Ebook.catalogue.db;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELException;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.eclipse.jetty.http.HttpStatus;

import com.Ebook.catalogue.models.BooksModel;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import jdk.internal.org.jline.utils.Log;

public class RepoCalls {

	private MongoClient mongoClient = new MongoClient("localhost", 27018);

	public List<BooksModel> getBooks() {

		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> booksCollection = database.getCollection("Ebook");

		if (booksCollection != null) {

			return EbookUtill.fromDocument(booksCollection);
		}

		// We need to return ERROR in this case.
		return EbookUtill.fromDocument(booksCollection);
	}

	public Response RegisterBook(BooksModel book) {
		if (book != null) {
			MongoDatabase database = this.mongoClient.getDatabase("test");
			MongoCollection<Document> booksCollection = database.getCollection("Ebook");

			BasicDBObject queryLoginSalt = new BasicDBObject("bookID", book.getBookID());
			FindIterable<Document> DD = booksCollection.find(queryLoginSalt);

			FindIterable<Document> fi = booksCollection.find();
			MongoCursor<Document> cursor = fi.iterator();
			while (cursor.hasNext()) {
				if (cursor.next().getInteger("bookId") == book.getBookID()) {
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
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> booksCollection = database.getCollection("Ebook");

		if (booksCollection != null) {

			BasicDBObject queryLoginSalt = new BasicDBObject("bookID", Id);

			FindIterable<Document> fi = booksCollection.find();
			MongoCursor<Document> cursor = fi.iterator();
			while (cursor.hasNext()) {
				if (cursor.next().getInteger("bookId") == Id) {
					Integer quan = cursor.next().getInteger("quantity");
//					cursor.next().put("quantity", quan - 1);
//					booksCollection.updateOne(queryLoginSalt, cursor.next());
//					return Response.status(HttpStatus.OK_200).entity("successfully loaned " + quan).build();
					return Response.status(HttpStatus.BAD_REQUEST_400).entity("book details not goven" + quan).build();
				}
			}
			return Response.status(HttpStatus.BAD_REQUEST_400).entity("book details not goven" + cursor).build();
		}
		return Response.status(HttpStatus.BAD_REQUEST_400).entity("book details not goven" + Id).build();
	}

//	public Integer EditBook(BooksModel book) {
//		MongoDatabase database = this.mongoClient.getDatabase("test");
//		MongoCollection<Document> booksCollection = database.getCollection("Ebook");
//
//		if (book != null) {
//			BasicDBObject queryLoginSalt = new BasicDBObject("bookID", book.getBookID());
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
//	public Response DeleteBook(BooksModel book) {
//		MongoDatabase database = this.mongoClient.getDatabase("test");
//		MongoCollection<Document> booksCollection = database.getCollection("Ebook");
//
//		if (booksCollection != null) {
//			BasicDBObject queryLoginSalt = new BasicDBObject("bookID", book.getBookID());
//			Document doc = booksCollection.find(queryLoginSalt).first();
//			if (doc != null) {
//				try {
//					booksCollection.findOneAndDelete(doc);
//					return Response.status(HttpStatus.OK_200)
//							.entity("successfully deleted" + booksCollection + database).build();
//				} catch (Exception e) {
//					return Response.status(HttpStatus.BAD_REQUEST_400).entity("not deleted coz book not found").build();
//				}
//			}
//		}
//		return Response.status(HttpStatus.BAD_REQUEST_400)
//				.entity("not deleted coz collection is " + booksCollection + database).build();
//	}
//
//	public Response LoanABook(BooksModel book) {
//
//		MongoDatabase database = mongoClient.getDatabase("test");
//		MongoCollection<Document> booksCollection = database.getCollection("Ebook");
//
//		if (booksCollection != null) {
//			
//			BasicDBObject queryLoginSalt = new BasicDBObject("bookID", book.getBookID());
//
//			FindIterable<Document> fi = booksCollection.find();
//			MongoCursor<Document> cursor = fi.iterator();
//			while (cursor.hasNext()) {
//				if (cursor.next().getInteger("bookId") == book.getBookID()) {
//					Integer quan = cursor.next().getInteger("quantity");
//        			cursor.next().put("quantity", quan-1);        			
//            		booksCollection.updateOne(queryLoginSalt, cursor.next());
//            		return Response.status(HttpStatus.OK_200).entity("successfully loaned " + quan).build();
//				}
//			}
//		}
//		return Response.status(HttpStatus.OK_200).entity("successfully loaned" + booksCollection).build();
//	}
}
