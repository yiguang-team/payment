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
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.repository.UserDao;
import com.yiguang.payment.rbac.service.UserService;
import com.yiguang.payment.rbac.vo.UserVO;
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	private UserDao userDao;
	
	private Specification<User> getPageQuerySpec(final UserVO vo)
	{
		Specification<User> spec = new Specification<User>(){
			@Override
			public Predicate toPredicate(Root<User> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getIsLock() != -1)
				{
					predicateList.add(cb.equal(root.get("isLock").as(Integer.class), vo.getIsLock()));  
				}
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getUsername()))
				{
					predicateList.add(cb.equal(root.get("username").as(String.class), vo.getUsername().trim()));  
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
	@Cacheable(value = "userCache")
	public YcPage<UserVO> queryUserList(UserVO conditionVO,
			int pageNumber, int pageSize, String sortType) {
		logger.debug("queryUserList start");
		try
		{
			Specification<User> spec = getPageQuerySpec(conditionVO);
			YcPage<User> ycPage = PageUtil.queryYcPage(userDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), User.class);

			YcPage<UserVO> result = new YcPage<UserVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<User> list = ycPage.getList();
			List<UserVO> voList = new ArrayList<UserVO>();
			UserVO vo = null;
			for (User temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}

			result.setList(voList);
			logger.debug("queryUserList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryUserList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "userCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public User updateUserStatus(User user) {
		try
		{
			logger.debug("updateUserStatus start, user [" + user.toString() + "]");
			User queryUser = userDao.findOne(user.getId());
			if (queryUser == null)
			{
				// 产品不存在
				logger.error("updateUserStatus failed, user [" + user.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			queryUser.setStatus(user.getStatus());
			queryUser = userDao.save(queryUser);
			logger.debug("updateUserStatus end, user [" + user.toString() + "]");
			return queryUser;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateUserStatus error, user [" + user.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "userCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteUser(User user) {
		logger.debug("deleteUser start, user [" + user.toString() + "]");
		try
		{
			User queryProduct = userDao.findOne(user.getId());
			if (queryProduct != null)
			{
				userDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的用户不存在！
				logger.error("deleteUser failed, user [" + user.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteUser end, user [" + user.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteUser failed, user [" + user.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "userCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public User saveUser(User user) {
		logger.debug("saveUser start, user [" + user.toString() + "]");
		try
		{

			User queryProduct = userDao.queryUserByUsername(user.getUsername());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && user.getId() == 0)
			{

				logger.error("saveUser failed, user [" + user.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			}
			else
			{
				user = userDao.save(user);
			}

			logger.debug("saveUser end, user [" + user.toString() + "]");
			return user;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveUser failed, user [" + user.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "userCache", key = "#root.methodName+#id")
	public User queryUser(long id) {
		logger.debug("queryUser start, id [" + id + "]");
		try
		{
			User user = userDao.findOne(id);
			logger.debug("queryUser end, id [" + id + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryUser error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "userCache", key = "#root.methodName+#username")
	public User queryUserByName(String username) {
		logger.debug("queryUserByName start, username [" + username + "]");
		try
		{
			User user = userDao.queryUserByUsername(username);
			logger.debug("queryUserByName end, username [" + username + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryUserByName error, username [" + username + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public UserVO copyPropertiesToVO(User temp) {
		logger.debug("copyPropertiesToVO start");
		try
		{
			UserVO vo = new UserVO();
			vo.setId(temp.getId());
			vo.setUsername(temp.getUsername());
			vo.setPassword(temp.getPassword());
			vo.setIsLock(temp.getIsLock());
			vo.setCreateTime(DATE_FORMAT.format(temp.getCreateTime()));
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());

			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			vo.setIsLockLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.IS_LOCK,
					String.valueOf(temp.getIsLock())).getText());
			logger.debug("copyPropertiesToVO end");
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
