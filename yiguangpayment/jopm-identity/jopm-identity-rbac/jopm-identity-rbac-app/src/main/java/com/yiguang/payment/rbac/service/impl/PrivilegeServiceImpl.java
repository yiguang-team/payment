package com.yiguang.payment.rbac.service.impl;

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
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.rbac.entity.Privilege;
import com.yiguang.payment.rbac.repository.PrivilegeDao;
import com.yiguang.payment.rbac.service.PrivilegeService;
import com.yiguang.payment.rbac.vo.PrivilegeVO;
@Service("privilegeService")
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {
	
	private static Logger logger = LoggerFactory.getLogger(PrivilegeServiceImpl.class);

	@Autowired
	DataSourceService dataSourceService;
	
	@Autowired
	private PrivilegeDao privilegeDao;

//	@Override
//	@Cacheable(value = "privilegeCache")
//	public YcPage<PrivilegeVO> queryPrivilegeList(Map<String, Object> searchParams,
//			int pageNumber, int pageSize, String sortType) {
//		logger.debug("queryPrivilegeList start");
//		try {
//			Map<String, SearchFilter> filters = SearchFilter
//					.parse(searchParams);
//			YcPage<Privilege> ycPage = PageUtil.queryYcPage(privilegeDao, filters,
//					pageNumber, pageSize, new Sort(Direction.DESC, "id"),
//					Privilege.class);
//
//			YcPage<PrivilegeVO> result = new YcPage<PrivilegeVO>();
//			result.setPageTotal(ycPage.getPageTotal());
//			result.setCountTotal(ycPage.getCountTotal());
//			List<Privilege> list = ycPage.getList();
//			List<PrivilegeVO> voList = new ArrayList<PrivilegeVO>();
//			PrivilegeVO vo = null;
//			for (Privilege temp : list) {
//				vo = copyPropertiesToVO(temp);
//				voList.add(vo);
//			}
//
//			result.setList(voList);
//			logger.debug("queryPrivilegeList end");
//			return result;
//		} catch (RpcException e) {
//			throw e;
//		} catch (Exception e) {
//			logger.error("queryPrivilegeList failed");
//			logger.error(e.getLocalizedMessage(), e);
//			throw new RpcException(ErrorCodeConst.ErrorCode99998);
//		}
//	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "privilegeCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Privilege updatePrivilegeStatus(Privilege privilege) {
		try {
			logger.debug("updatePrivilegeStatus start, privilege [" + privilege.toString()
					+ "]");
			Privilege queryUser = privilegeDao.findOne(privilege.getId());
			if (queryUser == null) {
				// 权限不存在
				logger.error("updatePrivilegeStatus failed, privilege ["
						+ privilege.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			queryUser.setStatus(privilege.getStatus());
			queryUser = privilegeDao.save(queryUser);
			logger.debug("updatePrivilegeStatus end, privilege [" + privilege.toString() + "]");
			return queryUser;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("updatePrivilegeStatus error, privilege [" + privilege.toString()
					+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "privilegeCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deletePrivilege(Privilege privilege) {
		logger.debug("deletePrivilege start, privilege [" + privilege.toString() + "]");
		try {
			Privilege queryProduct = privilegeDao.findOne(privilege.getId());
			if (queryProduct != null) {
				privilegeDao.delete(queryProduct);
			} else {
				// 准备删除的菜单不存在！
				logger.error("deleteMenu failed, privilege [" + privilege.toString()
						+ "]");
				throw new RpcException(19409);
			}
			logger.debug("deletePrivilege end, privilege [" + privilege.toString() + "]");
			return Constant.Common.SUCCESS;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("deletePrivilege failed, privilege [" + privilege.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "privilegeCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Privilege savePrivilege(Privilege privilege) {
		logger.debug("savePrivilege start, privilege [" + privilege.toString() + "]");
		try {

			Privilege queryProduct = privilegeDao.queryPrivilegeByName(privilege.getName());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && privilege.getId() == 0) {

				logger.error("savePrivilege failed, privilege [" + privilege.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			} else {
				privilege = privilegeDao.save(privilege);
			}

			logger.debug("savePrivilege end, privilege [" + privilege.toString() + "]");
			return privilege;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("savePrivilege failed, privilege [" + privilege.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "privilegeCache", key = "#root.methodName+#id")
	public Privilege queryPrivilege(long id) {
		logger.debug("queryPrivilege start, id [" + id + "]");
		try {
			Privilege user = privilegeDao.findOne(id);
			logger.debug("queryPrivilege end, id [" + id + "]");
			return user;
		} catch (Exception e) {
			logger.error("queryPrivilege error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	private Specification<Privilege> getPageQuerySpec(final PrivilegeVO vo)
	{
		Specification<Privilege> spec = new Specification<Privilege>(){
			@Override
			public Predicate toPredicate(Root<Privilege> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (vo.getPrivilegeLevel() != -1)
				{
					predicateList.add(cb.equal(root.get("privilegeLevel").as(Integer.class), vo.getPrivilegeLevel()));  
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
	public List<Privilege> findPrivilegeByLevel(int level) {
		logger.debug("findPrivilegeByLevel start");
		try {
			PrivilegeVO conditionVO = new PrivilegeVO();
			conditionVO.setStatus(CommonConstant.CommonStatus.OPEN);
			conditionVO.setPrivilegeLevel(level);
			Specification<Privilege> spec = getPageQuerySpec(conditionVO);
		
			List<Privilege> list = privilegeDao.findAll(spec, new Sort(Direction.ASC, "displayOrder"));
			logger.debug("findPrivilegeByLevel end");
			return list;
		} catch (Exception e) {
			logger.error("findPrivilegeByLevel failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	public Privilege queryPrivilegeByUrl(String url) {
		logger.debug("queryPrivilegeByUrl start, url [" + url + "]");
		try {
			Privilege user = privilegeDao.queryPrivilegeByUrl(url);
			logger.debug("queryPrivilegeByUrl end, url [" + url + "]");
			return user;
		} catch (Exception e) {
			logger.error("queryPrivilegeByUrl error, url [" + url + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public PrivilegeVO copyPropertiesToVO(Privilege temp) {
		logger.debug("copyPropertiesToVO start");
		try {
			PrivilegeVO vo = new PrivilegeVO();
			vo.setId(temp.getId());
			vo.setName(temp.getName());
			vo.setParentId(temp.getParentId());
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());
			vo.setUrl(temp.getUrl());
			vo.setStatusLabel(dataSourceService.findOptionVOById(
					CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			return vo;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("copyPropertiesToVO failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
}
