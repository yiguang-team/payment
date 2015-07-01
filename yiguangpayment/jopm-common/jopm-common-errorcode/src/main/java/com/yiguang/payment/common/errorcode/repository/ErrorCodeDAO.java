package com.yiguang.payment.common.errorcode.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.common.errorcode.entity.ErrorCode;

public interface ErrorCodeDAO extends PagingAndSortingRepository<ErrorCode, Long>, JpaSpecificationExecutor<ErrorCode>
{
	@Query("select errorCode from ErrorCode errorCode where errorCode.code =:code")
	public ErrorCode queryErrorMsgByCode(@Param("code") String code);
}
