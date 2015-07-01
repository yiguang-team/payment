package com.yiguang.payment.common.datasource.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yiguang.payment.common.datasource.entity.DataSourceOption;

public interface DataSourceOptionDao extends PagingAndSortingRepository<DataSourceOption, Long>,
		JpaSpecificationExecutor<DataSourceOption>
{

}
