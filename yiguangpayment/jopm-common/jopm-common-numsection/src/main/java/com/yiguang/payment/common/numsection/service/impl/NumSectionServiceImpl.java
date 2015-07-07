package com.yiguang.payment.common.numsection.service.impl;

/**
 * 号段表逻辑层
 * @author Jinger
 * @date：2013-10-15
 *
 */
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.common.numsection.repository.NumSectionDao;
import com.yiguang.payment.common.numsection.service.NumSectionService;

@Service("numSectionService")
public class NumSectionServiceImpl implements NumSectionService
{

	@Autowired
	private NumSectionDao numSectionDao;

	private static Logger logger = LoggerFactory.getLogger(NumSectionServiceImpl.class);

	@Override
	@CacheEvict(value={"numSectionCache"},allEntries=true,beforeInvocation=true)
	public NumSection saveNumSection(NumSection numSection)
	{
		logger.debug("[NumSectionServiceImpl:saveNumSection(" + numSection != null ? numSection.toString() : null
				+ ")]");
		if (numSection != null)
			numSection = numSectionDao.save(numSection);
		return numSection;
	}

	@Override
	@CacheEvict(value={"numSectionCache"},allEntries=true,beforeInvocation=true)
	public void deleteNumSection(String numSectionId)
	{
		logger.debug("[NumSectionServiceImpl:deleteNumSection(" + numSectionId + ")]");
		numSectionDao.delete(numSectionId);
	}

	@Override
	public void updateNumSection(String numSectionId, String status)
	{
		// NumSection numsection=numSectionDao.findOne(numSectionId);
	}

	@Override
	@Cacheable(value="numSectionCache",key="#root.methodName+#numSectionId")
	public NumSection findOne(String numSectionId)
	{
		logger.debug("[NumSectionServiceImpl:findOne(" + numSectionId + ")]");
		return numSectionDao.queryNumSectionById(numSectionId);
	}

	@Override
	@Cacheable(value="numSectionCache")
	public List<NumSection> selectAll()
	{
		logger.debug("[NumSectionServiceImpl:selectAll()]");
		return numSectionDao.selectAll();
	}

}
