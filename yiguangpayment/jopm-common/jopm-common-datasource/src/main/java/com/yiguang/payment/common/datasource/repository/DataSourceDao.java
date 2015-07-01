package com.yiguang.payment.common.datasource.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yiguang.payment.common.datasource.entity.DataSource;

public interface DataSourceDao extends PagingAndSortingRepository<DataSource, Long>,
		JpaSpecificationExecutor<DataSource>
{

}
