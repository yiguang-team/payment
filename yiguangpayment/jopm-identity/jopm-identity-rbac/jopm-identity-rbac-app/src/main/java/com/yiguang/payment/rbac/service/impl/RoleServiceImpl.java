package com.yiguang.payment.rbac.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.repository.RoleDao;
import com.yiguang.payment.rbac.service.RoleService;
import com.yiguang.payment.rbac.vo.RoleVO;
@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	RoleDao roleDao;
	
	private Specification<Role> getPageQuerySpec(final RoleVO vo)
	{
		Specification<Role> spec = new Specification<Role>(){
			@Override
			public Predicate toPredicate(Root<Role> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (StringUtil.isNotEmpty(vo.getRoleName()))
				{
					predicateList.add(cb.equal(root.get("roleName").as(String.class), vo.getRoleName().trim()));  
				}
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getCreateTime()))
				{
					predicateList.add(cb.equal(root.get("createTime").as(String.class), vo.getCreateTime().trim()));  
				}
				
				Predicate[] p = new Predicate[predicateList.size()];  
		        query.where(cb.and(predicateList.toArray(p)));  
		        //添加排序的功能  
		        query.orderBy(cb.asc(root.get("id").as(Integer.class)));  
		          
		        return query.getRestriction();  
			}
		};
		
		return spec;
	}
	
	@Override
	@Cacheable(value = "roleCache")
	public YcPage<RoleVO> queryRoleList(RoleVO conditionVO,
			int pageNumber, int pageSize, String sortType) {
		logger.debug("queryRoleList start");
		try
		{
			Specification<Role> spec = getPageQuerySpec(conditionVO);
			YcPage<Role> ycPage = PageUtil.queryYcPage(roleDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), Role.class);

			YcPage<RoleVO> result = new YcPage<RoleVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<Role> list = ycPage.getList();
			List<RoleVO> voList = new ArrayList<RoleVO>();
			RoleVO vo = null;
			for (Role temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}

			result.setList(voList);
			logger.debug("queryRoleList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryRoleList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Role updateRoleStatus(Role role) {
		try
		{
			logger.debug("updateRoleStatus start, role [" + role.toString() + "]");
			Role queryUser = roleDao.findOne(role.getId());
			if (queryUser == null)
			{
				// 产品不存在
				logger.error("updateRoleStatus failed, role [" + role.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			queryUser.setStatus(role.getStatus());
			queryUser = roleDao.save(queryUser);
			logger.debug("updateRoleStatus end, role [" + role.toString() + "]");
			return queryUser;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateRoleStatus error, role [" + role.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteRole(Role role) {
		logger.debug("deleteRole start, role [" + role.toString() + "]");
		try
		{
			Role queryProduct = roleDao.findOne(role.getId());
			if (queryProduct != null)
			{
				roleDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的角色不存在！
				logger.error("deleteRole failed, role [" + role.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteRole end, role [" + role.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteRole failed, user [" + role.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}


	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Role saveRole(Role role) {
		logger.debug("saveRole start, role [" + role.toString() + "]");
		try
		{

			Role queryProduct = roleDao.queryRoleByName(role.getRoleName());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && role.getId() == 0)
			{

				logger.error("saveRole failed, role [" + role.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			}
			else
			{
				role = roleDao.save(role);
			}

			logger.debug("saveRole end, role [" + role.toString() + "]");
			return role;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveRole failed, role [" + role.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "roleCache", key = "#root.methodName+#id")
	public Role queryRole(long id) {
		logger.debug("queryRole start, id [" + id + "]");
		try
		{
			Role role = roleDao.findOne(id);
			logger.debug("queryRole end, id [" + id + "]");
			return role;
		}
		catch (Exception e)
		{
			logger.error("queryRole error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public RoleVO copyPropertiesToVO(Role temp) {
		logger.debug("copyPropertiesToVO start");
		try
		{
			RoleVO vo = new RoleVO();
			vo.setId(temp.getId());
			vo.setRoleName(temp.getRoleName());
			vo.setCreateTime(DATE_FORMAT.format(temp.getCreateTime()));
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());

			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			return vo;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("copyPropertiesToVO failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

}
