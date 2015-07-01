package com.yiguang.payment.common.logging.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yiguang.payment.common.logging.entity.LoginAndLogoutLog;

public interface LoginAndLogoutLogDAO extends PagingAndSortingRepository<LoginAndLogoutLog, Long>, JpaSpecificationExecutor<LoginAndLogoutLog>
{

}
