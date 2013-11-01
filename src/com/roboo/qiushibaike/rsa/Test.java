package com.roboo.qiushibaike.rsa;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

public class Test
{
	public static void main(String[] args) throws Exception {  
        HashMap<String, Object> map = RSAUtils.generateKeys();  
        //生成公钥和私钥  
        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");  
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");  
          
        //模  
        String modulus = publicKey.getModulus().toString();  
        System.out.println("   模 =  " + modulus);
        System.out.println(" 模2 = " + privateKey.getModulus().toString());
        //公钥指数  
        String public_exponent = publicKey.getPublicExponent().toString();  
        System.out.println( "公钥指数  = " +public_exponent);
        //私钥指数  
        String private_exponent = privateKey.getPrivateExponent().toString();  
        System.out.println( "私钥指数  = " + private_exponent);
        //明文  
        String ming = "123456789";  
        //使用模和指数生成公钥和私钥  
        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);  
        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);  
        //加密后的密文  
        String mi = RSAUtils.encryptByPublicKey(ming, pubKey);  
        System.err.println("密文 = " + mi);  
        //解密后的明文  
        ming = RSAUtils.decryptByPrivateKey(mi, priKey);  
        System.err.println("明文 = "+ming);  
    }  

}
