package com.yiguang.payment.security.service.impl;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.yiguang.payment.common.security.DesUtil;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.security.RSAUtils;

/*
 * 文件名：SecurityTest.java
 * 版权：Copyright by www.365haoyou.com
 * 描述：
 * 修改人：Administrator
 * 修改时间：2015年2月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

public class SecurityMain
{
    public static void main(String[] args)
    {
        
        KeyStore ks;
        try
        {
            ks = KeyStore.getInstance("PKCS12");
            String keystorefilepath = "E://Code//project//trunks//yiguangpayment//jopm-wars//identityService//src//main//resources//keystone//SERVERCA.pfx";
            FileInputStream fis = new FileInputStream(keystorefilepath);

            ks.load(fis, "yiguang_123".toCharArray());

            // 私钥
            RSAPrivateKey prikey = (RSAPrivateKey) ks.getKey("SERVERCA", "yiguang_123".toCharArray());

            Certificate cert = ks.getCertificate("SERVERCA");

            RSAPublicKey pubkey = (RSAPublicKey) cert.getPublicKey();
            
            
            System.out.println("Rsa----");
            System.out.println(RSAUtils.getKeyString(prikey));
            System.out.println(RSAUtils.getKeyString(pubkey));
            String d3s= RSAUtils.encryptByPublicKey(""
                + "", pubkey);
            System.out.println("d3s::111:"+d3s);
       //     System.out.println("d3s:::"+d3s);
            
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        String key1="Yg_2014";
        String key2="Yg_2015";
        String pwd="yiguang2015";
        key1= MD5Util.getMD5Sign(key1);
        key2= MD5Util.getMD5Sign(key2);
        String des3="431397852331519240599875";
   //     String keyString="986980F866D077F14A0F3810B79B6680F5D56DDB85E9C90AC08822376A98F872D2F4346E76CB7F0E38022FE28F58D2CA4658633C7A5273091A9D8936543200F8D9CFD7FF2A50092C27CA37DF1527F2446388D07A16D6784BE33C8D6EEA329ADEE1CDE1C63A72CB0E3C0EE0366B167A8445409640A7A08B0BE6854385AA786C19";
        String key1k=DesUtil.encryptDes(des3,key1);
        String key2k=DesUtil.encryptDes(des3,key2);
    //    3EFFF9911A38762713B793066D8CA9BE96AC6CD29A2F51F3904F374D3F4CD7E4195F04B5705E20B4
    ///    String deskey=DesUtil.encryptDes(des3,"f8d14b8e10f3b93e37e67099efe585af");
        
        
     //   System.out.println("deskey-----"+deskey);
        
        System.out.println("key1k:--"+key1k);
        System.out.println("key2k:--"+key2k);
        
        System.out.println("key1:--"+key1);
        System.out.println("key2:--"+key2);
  //      String string="A2D52F68E52BFD854CC37F58BF2BDC975685878F43D139A103973E9EB57C9290195F04B5705E20B4";
        String  loginPwd = key1 + pwd + key2+"1581";
        loginPwd = MD5Util.getMD5Sign(loginPwd);
        System.out.println("loginPwd:--"+loginPwd);
        
     //   String dkeyString=DesUtil.decryptDes(des3,string);
    //    System.out.println("dkeyString:"+dkeyString);
        
        
        String deskey=DesUtil.decryptDes(des3,"5A7C9F4A4EBB409BE05592EC18985F757DC2AFD6E658520ADC4FC60C2A7B733172A00CE39E13DE2B");
        
        System.out.println("ddd-dd--------"+deskey);
        
        

        
    }
}
