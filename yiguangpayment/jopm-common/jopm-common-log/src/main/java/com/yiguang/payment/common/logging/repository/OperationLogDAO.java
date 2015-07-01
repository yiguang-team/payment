package com.yiguang.payment.common.logging.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yiguang.payment.common.logging.entity.OperationLog;

public interface OperationLogDAO extends PagingAndSortingRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog>
{
}
