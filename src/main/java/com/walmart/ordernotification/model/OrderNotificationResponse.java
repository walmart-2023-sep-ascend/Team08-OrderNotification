package com.walmart.ordernotification.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderNotificationResponse {

	private String orderid;
	private String dateOfOrder;
	private String statusOfOrder;
	private String email;
	private String firstName;
	private String lastName;

}
