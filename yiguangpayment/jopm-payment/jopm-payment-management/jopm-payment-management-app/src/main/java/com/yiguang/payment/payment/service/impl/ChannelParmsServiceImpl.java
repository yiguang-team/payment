package com.yiguang.payment.payment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.SearchFilter;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.payment.entity.ChannelParms;
import com.yiguang.payment.payment.repository.ChannelParmsDao;
import com.yiguang.payment.payment.service.ChannelParmsService;
import com.yiguang.payment.payment.vo.ChannelParmsVO;
@Service("channelParmsService")
@Transactional
public class ChannelParmsServiceImpl implements ChannelParmsService {
	private static Logger logger = LoggerFactory.getLogger(ChannelParmsServiceImpl.class);

	@Autowired
	private ChannelParmsDao channelParmsDao;
	@Autowired
	private DataSourceService dataSourceService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelParmsCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public ChannelParms updateChannelParmsStatus(ChannelParms channelParms) {
		try {
			logger.debug("updateChannelParmsStatus start, channelParms [" + channelParms.toString() + "]");
			ChannelParms new_channel = channelParmsDao.findOne(channelParms.getId());
			if (new_channel == null) {
				// 渠道不存在
				logger.error("updateChannelParmsStatus failed, channelParms [" + channelParms.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10302);
			}
			
			new_channel.setStatus(channelParms.getStatus());
			new_channel = channelParmsDao.save(new_channel);
			
			logger.debug("updateChannelParmsStatus end, channelParms [" + channelParms.toString() + "]");
			return new_channel;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("updateChannelParmsStatus failed, channelParms [" + channelParms.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelParmsCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public String deleteChannelParms(ChannelParms channelParms) {
		try {
			logger.debug("deleteChannelParms start, channelParms [" + channelParms.toString() + "]");
			ChannelParms new_channel = channelParmsDao.findOne(channelParms.getId());
			if (new_channel != null) {
				channelParmsDao.delete(new_channel);
			} else {
				// 准备删除的渠道不存在！
				logger.error("deleteChannelParms failed, channelParms [" + channelParms.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10302);
			}
			logger.debug("deleteChannelParms end, channelParms [" + channelParms.toString() + "]");
			return Constant.Common.SUCCESS;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("deleteChannelParms failed, channelParms [" + channelParms.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelParmsCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public ChannelParms saveChannelParms(ChannelParms channelParms) {
		try {
			logger.debug("saveChannelParms start, channelParms [" + channelParms.toString() + "]");
			ChannelParms new_channel = channelParmsDao.queryChannelByChannelIdAndKey(channelParms.getChannelId(),channelParms.getKey());
			// 唯一性
			if (BeanUtils.isNotNull(new_channel) && channelParms.getId() == 0) {

				logger.error("saveChannelParms failed, channelParms [" + channelParms.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10305);
			} else {
				channelParms = channelParmsDao.save(channelParms);
			}

			logger.debug("saveChannelParms end, channelParms [" + channelParms.toString() + "]");
			return channelParms;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("saveChannelParms failed, channelParms [" + channelParms.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="channelParmsCache",key="#root.methodName+#id")
	public ChannelParms queryChannelParms(long id) {
		logger.debug("queryChannelParms start, id [" + id + "]");
		try {

			ChannelParms channel = channelParmsDao.findOne(id);
			logger.debug("queryChannelParms end, id [" + id + "]");
			return channel;
		} catch (Exception e) {
			logger.error("queryChannelParms failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	@Cacheable(value="channelParmsCache",key="#root.methodName+#channelIdkey")
	public String queryChannelParms(long channelId, String key) {
		logger.debug("queryChannelParms start, channelId,key [" + channelId + key+"]");
		try {

			ChannelParms channel = channelParmsDao.queryChannelByChannelIdAndKey(channelId, key);
			String value = channel.getValue();
			logger.debug("queryChannelParms end, channelId,key [" + channelId + key+"]");
			return value;
		} catch (Exception e) {
			logger.error("queryChannelParms failed, channelId，key [" + channelId + key+"]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	@Cacheable(value="channelParmsCache")
	public YcPage<ChannelParmsVO> queryChannelParmsList(
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		logger.debug("queryChannelParmsList start");
		try {
			Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
			YcPage<ChannelParms> ycPage = PageUtil.queryYcPage(channelParmsDao, filters, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), ChannelParms.class);

			YcPage<ChannelParmsVO> result = new YcPage<ChannelParmsVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<ChannelParms> list = ycPage.getList();
			List<ChannelParmsVO> voList = new ArrayList<ChannelParmsVO>();
			ChannelParmsVO vo = null;
			for (ChannelParms temp : list) {
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			logger.debug("queryChannelParmsList end");
			result.setList(voList);

			return result;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("queryChannelParmsList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public ChannelParmsVO copyPropertiesToVO(ChannelParms temp) {
		logger.debug("copyPropertiesToVO start");
		try {
			ChannelParmsVO vo = new ChannelParmsVO();

			vo.setId(temp.getId());
			vo.setChannelId(temp.getChannelId());
			vo.setKey(temp.getKey());
			vo.setValue(temp.getValue());
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());
			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			vo.setChannelLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL,
					String.valueOf(temp.getChannelId())).getText());
			
			logger.debug("copyPropertiesToVO end");
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
