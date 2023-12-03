package com.walmart.ordernotification.dao;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;

public interface IorderNotificationDao {
	public AggregateIterable<Document> retrieveOrderStatusChange();

}
