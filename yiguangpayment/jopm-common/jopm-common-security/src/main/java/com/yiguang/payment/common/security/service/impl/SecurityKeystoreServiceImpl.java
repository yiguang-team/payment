package com.yiguang.payment.common.security.service.impl;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.Constant.Common;
import com.yiguang.payment.common.exception.ApplicationException;
import com.yiguang.payment.common.exception.ExceptionUtil;
import com.yiguang.payment.common.hopscache.HopsCacheUtil;
import com.yiguang.payment.common.security.DesUtil;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.config.CacheBeanPostProcessor;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;

/**
 * @ClassName: SecurityKeystoreServiceImpl
 * @Description: TODO
 * @author 肖进
 * @date 2014年9月15日 下午2:20:25
 */
@Service("securityKeystoreService")
public class SecurityKeystoreServiceImpl implements SecurityKeystoreService
{
	private static final Logger logger = LoggerFactory.getLogger(SecurityKeystoreServiceImpl.class);

	@Autowired
	CacheBeanPostProcessor cacheBeanPostProcessor;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public HashMap<String, Object> getKeyObjectToMap(String constantname)
	{
		try
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			logger.debug("[SecurityKeystoreServiceImpl: getKeyObjectToMap()][通过系统名称：" + constantname
					+ "取Map对象：Map<String, Object>对象:]");
			// 通过key得到公钥对象
			String publicKey = constantname + Constant.RSACacheKey.RSA_PUBLICKEY;
			String publicKeystr = (String) HopsCacheUtil.get(Common.IDENTITY_CACHE, publicKey);
			if (StringUtils.isBlank(publicKeystr))
			{
				logger.debug("重新执行init方法，获取公钥");
				cacheBeanPostProcessor.getPublicKey();
				publicKeystr = (String) HopsCacheUtil.get(Common.IDENTITY_CACHE, publicKey);
			}
			logger.debug("[SecurityKeystoreServiceImpl: getKeyObjectToMap()][getKeyObjectToMap-------publicKeystr="
					+ publicKeystr + "]");
			map.put(Constant.RSACacheKey.RSA_PUBLICKEY, publicKeystr);

			// 通过key得到私钥对象
			String privateKey = constantname + Constant.RSACacheKey.RSA_PRIVATEKEY;
			String privateKeystr = (String) HopsCacheUtil.get(Common.IDENTITY_CACHE, privateKey);
			if (StringUtils.isBlank(publicKeystr))
			{
				logger.debug("重新执行init方法，获取私钥");
				cacheBeanPostProcessor.getPrivateKey();
				publicKeystr = (String) HopsCacheUtil.get(Common.IDENTITY_CACHE, privateKey);
			}
			logger.debug("[SecurityKeystoreServiceImpl: getKeyObjectToMap()][getKeyObjectToMap-------privateKeystr="
					+ privateKeystr + "]");
			map.put(Constant.RSACacheKey.RSA_PRIVATEKEY, privateKeystr);
			return map;
		}
		catch (Exception e)
		{
			logger.error("[SecurityKeystoreServiceImpl: getKeyObjectToMap(根据别名获取公私钥Map对象失败)] [异常:"
					+ ExceptionUtil.getStackTraceAsString(e) + "]");
			String[] msgParams = new String[] { "getKeyObjectToMap" };
			ApplicationException ae = new ApplicationException("identity001096", msgParams);
			throw ExceptionUtil.throwException(ae);
		}
	}

	@Override
	public RSAPublicKey getRSAPublicKey(String constantname)
	{
		try
		{
			logger.debug("[SecurityKeystoreServiceImpl: getRSAPublicKey()][通过系统名称：" + constantname
					+ "取公钥对象：RSAPublicKey对象:]");
			// 通过key得到公钥对象
			String publicKeyName = constantname + Constant.RSACacheKey.RSA_PUBLICKEY_OBJECT;
			RSAPublicKey publicKey = (RSAPublicKey) HopsCacheUtil.get(Common.IDENTITY_CACHE, publicKeyName);
			if (BeanUtils.isNull(publicKey))
			{
				logger.debug("重新执行init方法，获取公钥对象");
				cacheBeanPostProcessor.getPublicKey();
				publicKey = (RSAPublicKey) HopsCacheUtil.get(Common.IDENTITY_CACHE, publicKeyName);
			}
			logger.debug("[SecurityKeystoreServiceImpl: getRSAPublicKey()][getRSAPublicKey--------------publicKey="
					+ publicKey + "]");
			return publicKey;
		}
		catch (Exception e)
		{
			logger.error("[SecurityKeystoreServiceImpl: getRSAPublicKey(根据别名获取公钥对象失败)] [异常:"
					+ ExceptionUtil.getStackTraceAsString(e) + "]");
			String[] msgParams = new String[] { "getRSAPublicKey" };
			ApplicationException ae = new ApplicationException("identity001094", msgParams);
			throw ExceptionUtil.throwException(ae);
		}
	}

	@Override
	public RSAPrivateKey getRSAPrivateKey(String constantname)
	{
		try
		{
			logger.debug("[SecurityKeystoreServiceImpl: getRSAPrivateKey()][通过系统名称：" + constantname
					+ "取私钥对象：RSAPrivateKey对象:]");

			// 通过key得到私钥对象
			String privateKeyName = constantname + Constant.RSACacheKey.RSA_PRIVATEKEY_OBJECT;
			RSAPrivateKey privateKey = (RSAPrivateKey) HopsCacheUtil.get(Common.IDENTITY_CACHE, privateKeyName);
			if (BeanUtils.isNull(privateKey))
			{
				logger.debug("重新执行init方法，获取私钥对象");
				cacheBeanPostProcessor.getPrivateKey();
				privateKey = (RSAPrivateKey) HopsCacheUtil.get(Common.IDENTITY_CACHE, privateKeyName);
			}
			logger.debug("[SecurityKeystoreServiceImpl: getRSAPrivateKey()][getRSAPrivateKey--------------privateKey="
					+ privateKey + "]");
			return privateKey;
		}
		catch (Exception e)
		{
			logger.error("[SecurityKeystoreServiceImpl: getRSAPrivateKey(根据别名获取私钥对象失败)] [异常:"
					+ ExceptionUtil.getStackTraceAsString(e) + "]");
			String[] msgParams = new String[] { "getRSAPrivateKey" };
			ApplicationException ae = new ApplicationException("identity001095", msgParams);
			throw ExceptionUtil.throwException(ae);
		}

	}

	@Override
	public String getLoginEncryptKey(String pwd, Long identityId)
	{
		String loginPwd = StringUtil.initString();
		logger.debug("[SecurityCredentialManagerServiceImpl:getMD5SecurityCredential(" + pwd + ")]");
		try
		{

			if (StringUtil.isNotBlank(pwd))
			{
				String sk1 = decrypt(SecurityKeystoreService.KEY_MD5_1, Constant.EncryptType.ENCRYPT_TYPE_3DES);
				String sk2 = decrypt(SecurityKeystoreService.KEY_MD5_2, Constant.EncryptType.ENCRYPT_TYPE_3DES);
				loginPwd = sk1 + pwd + sk2 + identityId;
				loginPwd = MD5Util.getMD5Sign(loginPwd);
				return loginPwd;
			}
			else
			{
				logger.error("[SecurityCredentialManagerServiceImpl:getMD5SecurityCredential(" + pwd
						+ ")] 报错:系统MD5Key为空或密码为空");
				String[] msgParams = new String[] { "getMD5SecurityCredential" };
				ApplicationException ae = new ApplicationException("identity001051", msgParams);
				throw ExceptionUtil.throwException(ae);
			}
		}
		catch (RpcException e)
		{
			throw ExceptionUtil.throwException(e);
		}
		catch (Exception e)
		{
			logger.error("[SecurityCredentialManagerServiceImpl:getMD5SecurityCredential(" + pwd + ")] "
					+ ExceptionUtil.getStackTraceAsString(e));
			String[] msgParams = new String[] { "getMD5SecurityCredential" };
			ApplicationException ae = new ApplicationException("identity001075", msgParams);
			throw ExceptionUtil.throwException(ae);
		}
	}

	@Override
	public String encrypt(String key, String encryptType)
	{
		try
		{
			if (StringUtil.isNotBlank(key) && StringUtil.isNotBlank(encryptType))
			{
				String encryptValue = StringUtil.initString();
				if (Constant.EncryptType.ENCRYPT_TYPE_MD5.equals(encryptType))
				{
					encryptValue = MD5Util.getMD5Sign(key);
				}
				else if (Constant.EncryptType.ENCRYPT_TYPE_3DES.equals(encryptType))
				{
					String des3Key = decrypt(SecurityKeystoreService.KEY_3DES, Constant.EncryptType.ENCRYPT_TYPE_RSA);
					encryptValue = DesUtil.encryptDes(des3Key, key);
				}
				else if (Constant.EncryptType.ENCRYPT_TYPE_RSA.equals(encryptType))
				{
					RSAPublicKey publicKey = getRSAPublicKey(Constant.SecurityCredential.RSA_KEY);
					encryptValue = RSAUtils.encryptByPublicKey(key, publicKey);
				}
				else
				{
					logger.error("[SecurityCredentialManagerServiceImpl: encrypt(key:" + key + ",encryptType:"
							+ encryptType + ")] ");
					String[] msgParams = new String[] { "encrypt" };
					ApplicationException ae = new ApplicationException("identity001098", msgParams);
					throw ExceptionUtil.throwException(ae);
				}
				logger.debug("[SecurityCredentialManagerServiceImpl: encrypt(" + encryptValue + ")][返回信息]");
				return encryptValue;
			}
			logger.error("[SecurityCredentialManagerServiceImpl: encrypt(key:" + key + ",encryptType:" + encryptType
					+ ")] ");
			String[] msgParams = new String[] { "encrypt" };
			ApplicationException ae = new ApplicationException("identity001097", msgParams);
			throw ExceptionUtil.throwException(ae);
		}
		catch (RpcException e)
		{
			throw ExceptionUtil.throwException(e);
		}
		catch (Exception e)
		{
			logger.error("[SecurityCredentialManagerServiceImpl: encrypt()]" + ExceptionUtil.getStackTraceAsString(e));
			String[] msgParams = new String[] { "encrypt" };
			ApplicationException ae = new ApplicationException("identity001099", msgParams);
			throw ExceptionUtil.throwException(ae);
		}
	}

	@Override
	public String decrypt(String key, String encryptType)
	{
		try
		{
			if (StringUtil.isNotBlank(key) && StringUtil.isNotBlank(encryptType))
			{
				String decryptValue = StringUtil.initString();
				if (Constant.EncryptType.ENCRYPT_TYPE_MD5.equals(encryptType))
				{
					logger.error("[SecurityCredentialManagerServiceImpl: decrypt(encryptKey:" + key + ",encryptType:"
							+ encryptType + ")] ");
					String[] msgParams = new String[] { "decrypt" };
					ApplicationException ae = new ApplicationException("identity001098", msgParams);
					throw ExceptionUtil.throwException(ae);
				}
				else if (Constant.EncryptType.ENCRYPT_TYPE_3DES.equals(encryptType))
				{
					String des3Key = decrypt(SecurityKeystoreService.KEY_3DES, Constant.EncryptType.ENCRYPT_TYPE_RSA);
					decryptValue = DesUtil.decryptDes(des3Key, key);
				}
				else if (Constant.EncryptType.ENCRYPT_TYPE_RSA.equals(encryptType))
				{
					RSAPrivateKey privateKey = (RSAPrivateKey) getRSAPrivateKey(Constant.SecurityCredential.RSA_KEY);
					decryptValue = RSAUtils.decryptByPrivateKey(key, privateKey);

				}
				else if (Constant.EncryptType.ENCRYPT_TYPE_JSRSA.equals(encryptType))
				{
					RSAPrivateKey privateKey = (RSAPrivateKey) getRSAPrivateKey(Constant.SecurityCredential.RSA_KEY);
					decryptValue = RSAUtils.decryptByJs(privateKey, key);
				}
				else
				{
					logger.error("[SecurityCredentialManagerServiceImpl: decrypt(encryptKey:" + key + ",encryptType:"
							+ encryptType + ")] ");
					String[] msgParams = new String[] { "decrypt" };
					ApplicationException ae = new ApplicationException("identity001098", msgParams);
					throw ExceptionUtil.throwException(ae);
				}
				logger.debug("[SecurityCredentialManagerServiceImpl: decrypt(" + key + ")][返回信息]");
				return decryptValue;
			}
			logger.error("[SecurityCredentialManagerServiceImpl: decrypt(encryptKey:" + key + ",encryptType:"
					+ encryptType + ")] ");
			String[] msgParams = new String[] { "decrypt" };
			ApplicationException ae = new ApplicationException("identity001097", msgParams);
			throw ExceptionUtil.throwException(ae);
		}
		catch (RpcException e)
		{
			throw ExceptionUtil.throwException(e);
		}
		catch (Exception e)
		{
			logger.error("[SecurityCredentialManagerServiceImpl: decrypt()]" + ExceptionUtil.getStackTraceAsString(e));
			String[] msgParams = new String[] { "decrypt" };
			ApplicationException ae = new ApplicationException("identity001099", msgParams);
			throw ExceptionUtil.throwException(ae);
		}
	}

	@Override
	public String getEncryptKeyByJSRSAKey(String jsRsaKey, Long identityId)
	{
		logger.debug("[SecurityCredentialManagerServiceImpl:getEncryptKeyByJSRSAKey(" + jsRsaKey + ")]");
		String key = decrypt(jsRsaKey, Constant.EncryptType.ENCRYPT_TYPE_JSRSA);
		String encryptKey = getLoginEncryptKey(key, identityId);
		logger.debug("[SecurityCredentialManagerServiceImpl:getEncryptKeyByJSRSAKey(" + encryptKey + ")][返回信息]");
		return encryptKey;
	}

}
