package com.walmart.ordernotification.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.AggregateIterable;
import com.walmart.ordernotification.dao.IorderNotificationDao;
import com.walmart.ordernotification.exception.EmailNotFoundException;
import com.walmart.ordernotification.exception.EmailNotSendException;
import com.walmart.ordernotification.exception.OrderNotFoundException;
import com.walmart.ordernotification.model.OrderNotificationResponse;

import jakarta.mail.internet.MimeMessage;

@Service
public class OrderNotificationServiceImpl implements IOrderNotificationService {

	@Autowired
	private IorderNotificationDao orderNotificationDao;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	OrderNotificationResponse orderNotificationResponse;

	List<OrderNotificationResponse> list = new ArrayList<>();

	private static final Logger log = LogManager.getLogger(OrderNotificationServiceImpl.class);

	@Override
	public ResponseEntity<String> OrderNotification()
			throws OrderNotFoundException, EmailNotFoundException, EmailNotSendException {
		// TODO Auto-generated method stub
		AggregateIterable<Document> result = orderNotificationDao.retrieveOrderStatusChange();

		List<Map<String, Object>> resultList = new ArrayList<>();

		try {

			for (Document document : result) {
				orderNotificationResponse = new OrderNotificationResponse();

				log.info("Order details found for the  status" + document);

				orderNotificationResponse.setOrderid(document.get("orderId").toString());

				orderNotificationResponse.setDateOfOrder(document.get("dateOfOrder").toString());

				orderNotificationResponse.setStatusOfOrder(document.get("statusOfOrder").toString());

				@SuppressWarnings("unchecked")
				List<Document> userDetails = (List<Document>) document.get("Orderdetails");
				for (Document details : userDetails) {

					if (details.get("email") == null) {
						log.info("Email details not retrieved");
						throw new EmailNotFoundException("Order details not retrieved");
					}

					orderNotificationResponse.setEmail(details.get("email").toString());
					Document nameDetails = (Document) details.get("name");

					orderNotificationResponse.setFirstName(nameDetails.get("firstName").toString());

					orderNotificationResponse.setLastName(nameDetails.get("lastName").toString());
				}

				resultList.add(document);
				list.add(orderNotificationResponse);
				log.info(" Response of order Notification " + orderNotificationResponse);

			}
		} catch (Exception e) {
			log.error("value must not be null");
		}

		for (OrderNotificationResponse response : list) {
			log.info("Triggering Email" + response);

			String status = sendSimpleMail(response);

			log.info("Response of Email status " + status);

		}

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse;
		try {
			jsonResponse = objectMapper.writeValueAsString(resultList);

		} catch (IOException e) {

			log.error("Error converting result to JSON");
			return ResponseEntity.status(500).body("Error converting result to JSON");
		}

		return ResponseEntity.ok().body(jsonResponse);

	}

	@Override
	public String sendSimpleMail(OrderNotificationResponse details) throws EmailNotSendException {

		String template = "<h3>Hello, " + details.getFirstName() + " </h3> <p> Order (Id:" + details.getOrderid()
				+ ") has been delivered you Successfully </p><p>Thank you for Shopping with Walmart! <p>"
				+ "<p>Got Questions? Please get in touch with our 24x7 Customer care <a href=\"http://34.125.137.138:3000/help-center\">here</a></p>";

		String result = null;
		try {

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setText(template, true);
			helper.setFrom(sender);
			helper.setTo(details.getEmail());
			helper.setSubject("Order has been delivered");
			javaMailSender.send(mimeMessage);

			log.info("Response of Email " + mimeMessage.toString());

			result = "Mail Sent Successfully...";

		} catch (Exception e) {
			log.error("Error while Sending Mail " + e);
			result = "Error while Sending Mail ";
			throw new EmailNotSendException("Error while Sending Mail");

		}
		return result;

	}

}
