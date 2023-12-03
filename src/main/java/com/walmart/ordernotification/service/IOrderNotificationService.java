package com.walmart.ordernotification.service;

import org.springframework.http.ResponseEntity;

import com.walmart.ordernotification.exception.EmailNotFoundException;
import com.walmart.ordernotification.exception.EmailNotSendException;
import com.walmart.ordernotification.exception.OrderNotFoundException;
import com.walmart.ordernotification.model.OrderNotificationResponse;

public interface IOrderNotificationService {

	public ResponseEntity<String> OrderNotification()
			throws OrderNotFoundException, EmailNotFoundException, EmailNotSendException;

	public String sendSimpleMail(OrderNotificationResponse details) throws EmailNotSendException;
}
