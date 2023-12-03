package com.walmart.ordernotification.dao;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.walmart.ordernotification.constant.ApplicationConstant;

@Repository
public class OrderNotificationDaoImpl implements IorderNotificationDao {

	private final MongoDatabase mongoAscendDb;

	public OrderNotificationDaoImpl(MongoDatabase mongoAscendDb) {
		this.mongoAscendDb = mongoAscendDb;

	}

	private static final Logger log = LogManager.getLogger(OrderNotificationDaoImpl.class);

	@Override
	public AggregateIterable<Document> retrieveOrderStatusChange() {

		MongoCollection<Document> orderCollection = mongoAscendDb.getCollection("Order");

		Bson matchStage = new Document("$match", new Document("statusOfOrder", ApplicationConstant.STATUS));

		// Bson matchStage = new Document("$match", new Document("orderId", 1001));

		Bson lookupStage = new Document("$lookup", new Document("from", "Users").append("localField", "userId")
				.append("foreignField", "id").append("as", "Orderdetails"));

		Bson projectStage = new Document("$project",
				new Document("_id", 0).append("orderId", 1).append("dateOfOrder", 1).append("statusOfOrder", 1)
						.append("Orderdetails.email", 1).append("Orderdetails.name.firstName", 1)
						.append("Orderdetails.name.lastName", 1));

		AggregateIterable<Document> result = orderCollection
				.aggregate(Arrays.asList(matchStage, lookupStage, projectStage

				));
		log.info("Found status changed orders");

		return result;

	}

}
