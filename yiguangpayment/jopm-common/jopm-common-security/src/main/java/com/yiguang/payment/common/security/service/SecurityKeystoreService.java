package com.yiguang.payment.common.security.service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @ClassName: SecurityKeystoreService
 * @Description: 访问Keystore
 * @author 肖进
 * @date 2014年9月15日 下午1:55:53
 */

public interface SecurityKeystoreService
{
	public static final String KEY_MD5_1 = "422AE147E3DAEBEB725C04E0752966C6E536CAFD08E5970E352C00C1C525C28872A00CE39E13DE2B";
	public static final String KEY_MD5_2 = "EE4E425D347F9BE0ED3937CC3BF31C23E5836A69FE8B508D5CA4B4E2C3809FA472A00CE39E13DE2B";
	public static final String KEY_3DES = "3A8F3D3E8894C6F966A2ACCDBEC09A36AC28C3D30DBA892C93D42E37FEF28511C3E3A101E07B3AC236E0DDBBC4E115F952A3DB2F71C067680D6602667C5FD10BF0AA519E31D9B46C4E2B20CB5B16DB94091E70C86E3261AA3E772D406E85643F1DF7F70C75DF687092CB3F8E7A244C7D6844AC25256D3CD09DA7C78BBC842447";
	/**
	 * 通过别名得到公钥对象
	 * 
	 * @param alias
	 * @return
	 */
	public RSAPublicKey getRSAPublicKey(String alias);

	/**
	 * 通过别名得私钥对象：RSAPrivateKey对象
	 * 
	 * @param alias
	 * @return
	 */
	public RSAPrivateKey getRSAPrivateKey(String alias);

	/**
	 * 通过系统名称取公私钥Map对象
	 * 
	 * @param constantname
	 * @return
	 */
	Map<String, Object> getKeyObjectToMap(String constantname);

	
	/**
	 * 根据登录密码和系统MD5Key进行组装加密
	 * 
	 * @param pwd
	 *            登录密码源串
	 * @return
	 */

	public String getLoginEncryptKey(String pwd, Long identityId);

	/**
	 * 根据js加密密码进行解密并组装加密
	 * 
	 * @param jsRsaKey
	 *            js加密密码
	 * @return
	 */
	public String getEncryptKeyByJSRSAKey(String jsRsaKey, Long identityId);

	/**
	 * 公共加密方法
	 * 
	 * @param key
	 *            待加密字符
	 * @param encryptType
	 *            加密类型
	 * @return
	 */
	public String encrypt(String key, String encryptType);

	/**
	 * 公共解密方法
	 * 
	 * @param key
	 *            待解密字符
	 * @param encryptType
	 *            解密类型
	 * @return
	 */
	public String decrypt(String key, String encryptType);
}
