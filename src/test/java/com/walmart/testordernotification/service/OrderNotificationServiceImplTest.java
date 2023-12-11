package com.walmart.testordernotification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.walmart.ordernotification.OrdernotificationApplication;
import com.walmart.ordernotification.exception.EmailNotSendException;
import com.walmart.ordernotification.model.OrderNotificationResponse;
import com.walmart.ordernotification.service.OrderNotificationServiceImpl;


@ContextConfiguration(classes = OrdernotificationApplication.class)
@SpringBootTest
public class OrderNotificationServiceImplTest {

	@Autowired
	private OrderNotificationServiceImpl orderNotification;

	OrderNotificationResponse mockorderNotificationResponse;

	@Test
	void getOrderNotification() throws Exception {

		String expectedResult = "Mail Sent Successfully...";

		mockorderNotificationResponse = new OrderNotificationResponse();

		mockorderNotificationResponse.setOrderid("1005");

		mockorderNotificationResponse.setDateOfOrder("2023-09-29T15:30:00Z");

		mockorderNotificationResponse.setStatusOfOrder("Delivered");

		mockorderNotificationResponse.setEmail("david.clark@example.com");

		mockorderNotificationResponse.setFirstName("David");

		mockorderNotificationResponse.setLastName("Clark");

		String result = orderNotification.sendSimpleMail(mockorderNotificationResponse);

		assertEquals(expectedResult, result);

	}

	@Test
	void failnotification() throws Exception {

		String expectedResult = "Error while Sending Mail";

		mockorderNotificationResponse = new OrderNotificationResponse();

		assertThrows(EmailNotSendException.class,
				() -> orderNotification.sendSimpleMail(mockorderNotificationResponse));

	}

}
