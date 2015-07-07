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
import com.yiguang.payment.rbac.entity.Menu;
import com.yiguang.payment.rbac.repository.MenuDao;
import com.yiguang.payment.rbac.service.MenuService;
import com.yiguang.payment.rbac.vo.MenuVO;

@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService {

	private static Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	private MenuDao menuDao;

	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "menuCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Menu updateMenuStatus(Menu menu) {
		try {
			logger.debug("updateMenuStatus start, menu [" + menu.toString()
					+ "]");
			Menu queryUser = menuDao.findOne(menu.getId());
			if (queryUser == null) {
				// 产品不存在
				logger.error("updateMenuStatus failed, menu ["
						+ menu.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			queryUser.setStatus(menu.getStatus());
			queryUser = menuDao.save(queryUser);
			logger.debug("updateMenuStatus end, menu [" + menu.toString() + "]");
			return queryUser;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("updateMenuStatus error, menu [" + menu.toString()
					+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "menuCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteMenu(Menu menu) {
		logger.debug("deleteMenu start, menu [" + menu.toString() + "]");
		try {
			Menu queryProduct = menuDao.findOne(menu.getId());
			if (queryProduct != null) {
				menuDao.delete(queryProduct);
			} else {
				// 准备删除的菜单不存在！
				logger.error("deleteMenu failed, menu [" + menu.toString()
						+ "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteMenu end, menu [" + menu.toString() + "]");
			return Constant.Common.SUCCESS;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("deleteMenu failed, menu [" + menu.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "menuCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Menu saveMenu(Menu menu) {
		logger.debug("saveMenu start, menu [" + menu.toString() + "]");
		try {

			Menu queryProduct = menuDao.queryMenuByName(menu.getMenuName());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && menu.getId() == 0) {

				logger.error("saveMenu failed, menu [" + menu.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			} else {
				menu = menuDao.save(menu);
			}

			logger.debug("saveMenu end, menu [" + menu.toString() + "]");
			return menu;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("saveMenu failed, menu [" + menu.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "menuCache", key = "#root.methodName+#id")
	public Menu queryMenu(long id) {
		logger.debug("queryMenu start, id [" + id + "]");
		try {
			Menu user = menuDao.findOne(id);
			logger.debug("queryMenu end, id [" + id + "]");
			return user;
		} catch (Exception e) {
			logger.error("queryMenu error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public Menu queryMenuByUrl(String url) {
		logger.debug("queryMenuByUrl start, id [" + url + "]");
		try {
			Menu user = menuDao.queryMenuByUrl(url);
			logger.debug("queryMenuByUrl end, id [" + url + "]");
			return user;
		} catch (Exception e) {
			logger.error("queryMenuByUrl error, id [" + url + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	private Specification<Menu> getPageQuerySpec(final MenuVO vo)
	{
		Specification<Menu> spec = new Specification<Menu>(){
			@Override
			public Predicate toPredicate(Root<Menu> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (vo.getMenulevel() != -1)
				{
					predicateList.add(cb.equal(root.get("menulevel").as(Integer.class), vo.getMenulevel()));  
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
	public List<Menu> findMenuByLevel(int level) {
		logger.debug("findMenuByLevel start");
		try {
			MenuVO conditionVO = new MenuVO();
			conditionVO.setStatus(CommonConstant.CommonStatus.OPEN);
			conditionVO.setMenulevel(level);
			Specification<Menu> spec = getPageQuerySpec(conditionVO);
			List<Menu> list = menuDao.findAll(spec, new Sort(Direction.ASC, "displayOrder"));
			
			return list;
		} catch (Exception e) {
			logger.error("findMenuByLevel failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public MenuVO copyPropertiesToVO(Menu temp) {
		logger.debug("copyPropertiesToVO start");
		try {
			MenuVO vo = new MenuVO();
			vo.setId(temp.getId());
			vo.setMenuName(temp.getMenuName());
			vo.setDisplayOrder(temp.getDisplayOrder());
			vo.setCreateTime(temp.getCreateTime());
			vo.setMenulevel(temp.getMenulevel());
			vo.setParentId(temp.getParentId());
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());
			vo.setSubModule(temp.getSubModule());
			vo.setSubSystem(temp.getSubSystem());
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
