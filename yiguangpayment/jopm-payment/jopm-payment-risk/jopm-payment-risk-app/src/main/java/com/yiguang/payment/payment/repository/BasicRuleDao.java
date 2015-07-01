package com.yiguang.payment.payment.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yiguang.payment.payment.entity.BasicRule;

public interface BasicRuleDao extends PagingAndSortingRepository<BasicRule, Long>,
		JpaSpecificationExecutor<BasicRule>
{

}
