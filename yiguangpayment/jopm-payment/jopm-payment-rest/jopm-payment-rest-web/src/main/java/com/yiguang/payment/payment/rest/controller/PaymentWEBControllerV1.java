package com.yiguang.payment.payment.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yiguang.payment.common.errorcode.service.ErrorCodeService;

@Controller
@RequestMapping(value = "/payment/web/1.0/")
public class PaymentWEBControllerV1
{
	@Autowired
	private ErrorCodeService errorCodeService;

	private static Logger logger = LoggerFactory.getLogger(PaymentWEBControllerV1.class);
	
}
