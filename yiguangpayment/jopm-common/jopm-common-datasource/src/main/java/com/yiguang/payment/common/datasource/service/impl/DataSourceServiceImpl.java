package com.yiguang.payment.common.datasource.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.datasource.entity.DataSource;
import com.yiguang.payment.common.datasource.entity.DataSourceOption;
import com.yiguang.payment.common.datasource.repository.DataSourceDao;
import com.yiguang.payment.common.datasource.repository.DataSourceOptionDao;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.StringUtil;

@Service("dataSourceService")
public class DataSourceServiceImpl implements DataSourceService
{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSourceDao dataSourceDao;

	@Autowired
	private DataSourceOptionDao dataSourceOptionDao;

	private static Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode") 
	public List<OptionVO> findOpenOptions(String dataSourceCode)
	{

		logger.debug("findOptions start, dataSourceCode is [" + dataSourceCode + "]");
		try
		{
			List<DataSourceOption> optionList = getOptions(dataSourceCode);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
				logger.debug("findOptions value is [" + option.getValue() + "] text is [" + option.getText() + "]");
			}

			option = new OptionVO();
			option.setValue("-1");
			option.setText("请选择");
			options.add(0, option);
			logger.debug("findOptions end, dataSourceCode is [" + dataSourceCode + "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findOptions failed, dataSourceCode is [" + dataSourceCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode")
	public List<OptionVO> findOpenOptionsWithoutPlease(String dataSourceCode)
	{
		logger.debug("findOptionsWithoutPlease start, dataSourceCode is [" + dataSourceCode + "]");
		try
		{
			List<DataSourceOption> optionList = getOptions(dataSourceCode);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
			}
			logger.debug("findOptionsWithoutPlease end, dataSourceCode is [" + dataSourceCode + "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findOptionsWithoutPlease failed, dataSourceCode is [" + dataSourceCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	private List<DataSourceOption> getOptions(String dataSourceCode)
	{
		logger.debug("getOptions start, dataSourceCode is [" + dataSourceCode + "]");
		try
		{
			List<DataSourceOption> optionList = new ArrayList<DataSourceOption>();
			
			Specification<DataSource> spec = getDataSourceSpec(dataSourceCode);
			
			final DataSource dataSource = dataSourceDao.findOne(spec);
			
			if (null != dataSource)
			{
				if (dataSource.getType() == CommonConstant.DataSourceType.COMMON)
				{
					
					Specification<DataSourceOption> optionSpec = getOptionSpec(dataSource.getCode());
					optionList = dataSourceOptionDao.findAll(optionSpec,new Sort(Direction.ASC, "optionId"));
				}
				else if (dataSource.getType() == CommonConstant.DataSourceType.SQL)
				{
					List<Map<String, Object>> list = jdbcTemplate.queryForList(dataSource.getOptionSql()
							+ " and t.status=" + CommonConstant.CommonStatus.OPEN);
					for (Map<String, Object> map : list)
					{
						DataSourceOption dso = new DataSourceOption();
						dso.setOptionId(String.valueOf(map.get("optionId")));
						dso.setOptionLabel(String.valueOf(map.get("optionLabel")));
						optionList.add(dso);
					}
				}
			}

			logger.debug("getOptions end, dataSourceCode is [" + dataSourceCode + "]");
			return optionList;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("getOptions failed, dataSourceCode is [" + dataSourceCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode")
	public List<OptionVO> findAllOptions(String dataSourceCode)
	{

		logger.debug("findAllOptions start, dataSourceCode is [" + dataSourceCode + "]");
		try
		{
			List<DataSourceOption> optionList = getAllOptions(dataSourceCode);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
				logger.debug("findAllOptions value is [" + option.getValue() + "] text is [" + option.getText() + "]");
			}

			option = new OptionVO();
			option.setValue("-1");
			option.setText("请选择");
			options.add(0, option);
			logger.debug("findAllOptions end, dataSourceCode is [" + dataSourceCode + "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findAllOptions failed, dataSourceCode is [" + dataSourceCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode")
	public List<OptionVO> findAllOptionsWithoutPlease(String dataSourceCode)
	{
		logger.debug("findAllOptionsWithoutPlease start, dataSourceCode is [" + dataSourceCode + "]");
		try
		{
			List<DataSourceOption> optionList = getAllOptions(dataSourceCode);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
			}
			logger.debug("findAllOptionsWithoutPlease end, dataSourceCode is [" + dataSourceCode + "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findAllOptionsWithoutPlease failed, dataSourceCode is [" + dataSourceCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	private List<DataSourceOption> getAllOptions(String dataSourceCode)
	{
		logger.debug("getAllOptions start, dataSourceCode is [" + dataSourceCode + "]");
		try
		{
			List<DataSourceOption> optionList = new ArrayList<DataSourceOption>();
			Specification<DataSource> spec = getDataSourceSpec(dataSourceCode);
			DataSource dataSource = dataSourceDao.findOne(spec);
			if (null != dataSource)
			{
				if (dataSource.getType() == CommonConstant.DataSourceType.COMMON)
				{
					Specification<DataSourceOption> optionSpec = getOptionSpec(dataSource.getCode());
					optionList = dataSourceOptionDao.findAll(optionSpec);
				}
				else if (dataSource.getType() == CommonConstant.DataSourceType.SQL)
				{
					List<Map<String, Object>> list = jdbcTemplate.queryForList(dataSource.getOptionSql());
					for (Map<String, Object> map : list)
					{
						DataSourceOption dso = new DataSourceOption();
						dso.setOptionId(String.valueOf(map.get("optionId")));
						dso.setOptionLabel(String.valueOf(map.get("optionLabel")));
						optionList.add(dso);
					}
				}
			}
			logger.debug("getAllOptions end, dataSourceCode is [" + dataSourceCode + "]");
			return optionList;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("getAllOptions failed, dataSourceCode is [" + dataSourceCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#objectName+#id")
	public OptionVO findOptionVOById(String objectName, String id)
	{
		logger.debug("findOptionVOById start, objectName is [" + objectName + "] id is [" + id + "]");
		try
		{
			OptionVO option = new OptionVO();
			List<DataSourceOption> optionList = getAllOptions(objectName);
			for (DataSourceOption o : optionList)
			{
				if (o.getOptionId() != null && o.getOptionId().equals(id))
				{
					option.setValue(String.valueOf(o.getOptionId()));
					option.setText(o.getOptionLabel());
				}
			}
			logger.debug("findOptionVOById end, objectName is [" + objectName + "] id is [" + id + "]");
			return option;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findOptionVOById failed, objectName is [" + objectName + "] id is [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode+#parentId")
	public List<OptionVO> findOpenOptionsByParent(String dataSourceCode, String parentId)
	{
		logger.debug("findOptionsByParent start, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
				+ "]");
		try
		{
			List<DataSourceOption> optionList = getOpenOptionsByParent(dataSourceCode, parentId, DS_OPEN);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
			}
			option = new OptionVO();
			option.setValue("-1");
			option.setText("请选择");
			options.add(0, option);
			logger.debug("findOptionsByParent end, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
					+ "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findOptionsByParent end, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
					+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode+#parentId")
	public List<OptionVO> findAllOptionsByParent(String dataSourceCode, String parentId)
	{
		logger.debug("findOptionsByParent start, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
				+ "]");
		try
		{
			List<DataSourceOption> optionList = getOpenOptionsByParent(dataSourceCode, parentId, DS_ALL);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
			}
			option = new OptionVO();
			option.setValue("-1");
			option.setText("请选择");
			options.add(0, option);
			logger.debug("findOptionsByParent end, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
					+ "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findOptionsByParent end, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
					+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	private List<DataSourceOption> getOpenOptionsByParent(String dataSourceCode, String parentId, String flag)
	{
		logger.debug("getOptionsByParent start, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
				+ "]");
		try
		{
			List<DataSourceOption> optionList = new ArrayList<DataSourceOption>();
			Specification<DataSource> spec = getDataSourceSpec(dataSourceCode);
			DataSource dataSource = dataSourceDao.findOne(spec);
			if (null != dataSource)
			{
				if (dataSource.getType() == CommonConstant.DataSourceType.COMMON)
				{
					Specification<DataSourceOption> optionSpec = getOptionSpec(dataSource.getCode());
					optionList = dataSourceOptionDao.findAll(optionSpec);
				}
				else if (dataSource.getType() == CommonConstant.DataSourceType.SQL)
				{
					if (StringUtil.isNotBlank(parentId))
					{

						String sql = null;
						if (flag.equals(DS_OPEN))
						{
							sql = dataSource.getOptionSql() + " and t." + dataSource.getParentCode() + "='" + parentId
									+ "'" + " and t.status=" + CommonConstant.CommonStatus.OPEN;
						}
						else
						{
							sql = dataSource.getOptionSql() + " and t." + dataSource.getParentCode() + "='" + parentId
									+ "'";
						}

						List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
						for (Map<String, Object> map : list)
						{
							DataSourceOption dso = new DataSourceOption();
							dso.setOptionId(String.valueOf(map.get("optionId")));
							dso.setOptionLabel(String.valueOf(map.get("optionLabel")));
							optionList.add(dso);
						}
					}
					else
					{
						List<Map<String, Object>> list = jdbcTemplate.queryForList(dataSource.getOptionSql());
						for (Map<String, Object> map : list)
						{
							DataSourceOption dso = new DataSourceOption();
							dso.setOptionId(String.valueOf(map.get("optionId")));
							dso.setOptionLabel(String.valueOf(map.get("optionLabel")));
							optionList.add(dso);
						}
					}

				}
			}
			logger.debug("getOptionsByParent end, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
					+ "]");
			return optionList;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("getOptionsByParent failed, dataSourceCode is [" + dataSourceCode + "] parentId is ["
					+ parentId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode+#parentId")
	public List<OptionVO> findOpenOptionsByParentWithoutPlease(String dataSourceCode, String parentId)
	{
		logger.debug("findOptionsByParent start, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
				+ "]");
		try
		{
			List<DataSourceOption> optionList = getOpenOptionsByParent(dataSourceCode, parentId, DS_OPEN);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
			}

			logger.debug("findOptionsByParent end, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
					+ "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findOptionsByParent failed, dataSourceCode is [" + dataSourceCode + "] parentId is ["
					+ parentId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="dataSourceCache",key="#root.methodName+#dataSourceCode+#parentId")
	public List<OptionVO> findAllOptionsByParentWithoutPlease(String dataSourceCode, String parentId)
	{
		logger.debug("findOptionsByParent start, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
				+ "]");
		try
		{
			List<DataSourceOption> optionList = getOpenOptionsByParent(dataSourceCode, parentId, DS_ALL);
			List<OptionVO> options = new ArrayList<OptionVO>();
			OptionVO option = null;
			for (DataSourceOption dsOption : optionList)
			{
				option = new OptionVO();
				option.setValue(String.valueOf(dsOption.getOptionId()));
				option.setText(dsOption.getOptionLabel());
				options.add(option);
			}

			logger.debug("findOptionsByParent end, dataSourceCode is [" + dataSourceCode + "] parentId is [" + parentId
					+ "]");
			return options;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("findOptionsByParent failed, dataSourceCode is [" + dataSourceCode + "] parentId is ["
					+ parentId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	
	private Specification<DataSource> getDataSourceSpec (final String dataSourceCode)
	{
		Specification<DataSource> spec = new Specification<DataSource>(){
			@Override
			public Predicate toPredicate(Root<DataSource> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        Predicate p1 = cb.equal(root.get("status").as(Integer.class), CommonConstant.CommonStatus.OPEN);  
		        Predicate p2 = cb.equal(root.get("code").as(String.class), dataSourceCode);  
		        //把Predicate应用到CriteriaQuery中去,因为还可以给CriteriaQuery添加其他的功能，比如排序、分组啥的  
		        query.where(cb.and(p1,p2));  
		        //添加排序的功能  
		        query.orderBy(cb.desc(root.get("id").as(Integer.class)));  
		          
		        return query.getRestriction();  
			}
		};
		
		return spec;
	}
	
	private Specification<DataSourceOption> getOptionSpec (final String dataSourceCode)
	{
		Specification<DataSourceOption> optionSpec = new Specification<DataSourceOption>(){
			@Override
			public Predicate toPredicate(Root<DataSourceOption> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        Predicate p1 = cb.equal(root.get("status").as(Integer.class), CommonConstant.CommonStatus.OPEN);  
		        Predicate p2 = cb.equal(root.get("dataSourceCode").as(String.class), dataSourceCode);  
		        //把Predicate应用到CriteriaQuery中去,因为还可以给CriteriaQuery添加其他的功能，比如排序、分组啥的  
		        query.where(cb.and(p1,p2));  
		        //添加排序的功能  
		        query.orderBy(cb.asc(root.get("optionId").as(Integer.class)));  
		          
		        return query.getRestriction();  
			}
		};
		
		return optionSpec;
	}
}
