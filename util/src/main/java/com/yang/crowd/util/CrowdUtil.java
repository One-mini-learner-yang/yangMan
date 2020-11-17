package com.yang.crowd.util;

import com.yang.crowd.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CrowdUtil {
    public static boolean judgeRequestType(HttpServletRequest httpServletRequest){

        String acceptHeader =httpServletRequest.getHeader("Accept");
        String xRequestHeader=httpServletRequest.getHeader("X-Requested-With");
        return ((acceptHeader!=null&&acceptHeader.contains("application/json"))||(xRequestHeader!=null&&xRequestHeader.contains("XMLHttpRequest")));
    }

    /**
     *
     * @param source 传入铭文
     * @return
     */
    public static String md5(String source){
        if (source==null||source.length()==0){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        String algorithm="MD5";
        try {
            MessageDigest messageDigest=MessageDigest.getInstance(algorithm);
            byte[] output = messageDigest.digest(source.getBytes());
            BigInteger bigInteger=new BigInteger(1,output);
            String encoded=bigInteger.toString(16);
            return encoded;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}