package com.walmart.ordernotification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ordernotification.exception.EmailNotFoundException;
import com.walmart.ordernotification.exception.EmailNotSendException;
import com.walmart.ordernotification.exception.OrderNotFoundException;
import com.walmart.ordernotification.service.OrderNotificationServiceImpl;

@RestController
public class OrderNotificationController {

	@Autowired
	private OrderNotificationServiceImpl orderNotificationService;

	@GetMapping("/OrderNotification")
	// @Scheduled(cron = "0 */5 * * * *")
	public ResponseEntity<String> CheckOrderNotification()
			throws OrderNotFoundException, EmailNotFoundException, EmailNotSendException {
		return orderNotificationService.OrderNotification();
	}

}
