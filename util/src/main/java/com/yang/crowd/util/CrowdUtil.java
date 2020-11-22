package com.yang.crowd.util;

import com.yang.crowd.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CrowdUtil {

    /**
     *
     * @param host 短信接口url "https://smssend.shumaidata.com"
     * @param path 具体发送短信的功能的地址  "/sms/send"
     * @param method 请求方式  "POST"
     * @param phoneNum  电话号
     * @param appCode  调用API的AppCode  "b6b2db022be1461ea79df1bdeb0b1ffa"
//     * @param sign  签名编号(我使用的API不需要该参数)
     * @param skin  模板编号
     * @return
     */
    public static ResultEntity<String> sendCodeByShortMessage(
            String host,
            String path,
            String method,
            String phoneNum,
            String appCode,
//            String sign,
            String skin
    ){
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        StringBuilder builder=new StringBuilder();
        for (int i=0;i<4;i++){
            int random= (int) (Math.random()*10);
            builder.append(random);
        }
        String code=builder.toString();
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("receive", phoneNum);
        querys.put("tag", code);
        querys.put("templateId", skin);
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //获取response的body
            //200	成功	成功
            //400	参数错误	参数错误
            //404	请求资源不存在	请求资源不存在
            //500	系统内部错误，请联系服务商	系统内部错误，请联系服务商
            //501	第三方服务异常	第三方服务异常
            //604	接口停用	接口停用
            //1001	其他，以实际返回为准	其他，以实际返回为准
            StatusLine statusLine=response.getStatusLine();
            int resultCode=statusLine.getStatusCode();
            String message=statusLine.getReasonPhrase();
            if (resultCode==200){
                return ResultEntity.successWithData(code);
            }
            return ResultEntity.failed(message);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


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