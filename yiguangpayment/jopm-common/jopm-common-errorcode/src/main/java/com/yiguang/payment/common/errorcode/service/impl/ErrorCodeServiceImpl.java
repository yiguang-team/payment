package com.yiguang.payment.common.errorcode.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.errorcode.entity.ErrorCode;
import com.yiguang.payment.common.errorcode.repository.ErrorCodeDAO;
import com.yiguang.payment.common.errorcode.service.ErrorCodeService;
import com.yiguang.payment.common.exception.ErrorCodeConst;

@Service("errorCodeService")
@Transactional
public class ErrorCodeServiceImpl implements ErrorCodeService
{

	private static Logger logger = LoggerFactory.getLogger(ErrorCodeServiceImpl.class);
	@Autowired
	private ErrorCodeDAO errorCodeDAO;

	@Override
	@Cacheable(value="errorCodeCache") 
	public String getErrorMsgByCode(String code)
	{
		logger.debug("getErrorMsgByCode start, error code is [" + code + "]");
		try
		{
			ErrorCode errorCode = errorCodeDAO.queryErrorMsgByCode(code);
			logger.debug("getErrorMsgByCode end, error code is [" + code + "]");
			return errorCode != null ? errorCode.getMsg() : null;

		}
		catch (Exception e)
		{
			logger.error("getErrorMsgByCode failed, error code is [" + code + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
}
