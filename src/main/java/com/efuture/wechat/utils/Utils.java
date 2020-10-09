package com.efuture.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.util.TypeUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @title: Utils
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/16
 */

public class Utils
{
    private final static String CR = " <br>          at ";
    private static boolean isDetailException = false;
    static {
        WebApplicationContext cont =  ContextLoader.getCurrentWebApplicationContext();
        if (cont != null)
        {
            ServletContext sc = cont.getServletContext();
            if (sc != null)
            {
                if ("true".equalsIgnoreCase(sc.getInitParameter("detailException"))) isDetailException = true;
            }
        }
    }

    public static String getLastExceptionMessage(Exception e)
    {
        return getLastExceptionMessage(e,isDetailException);
    }

    protected static String getLastExceptionMessage(Exception e,boolean all)
    {
        // 找到最后一个引起异常的原因
        Throwable t = e;
        if (e instanceof InvocationTargetException)
        {
            t = ((InvocationTargetException)e).getTargetException();
        }
        else
        {
            do {
                t = t.getCause();
            } while(t != null && t.getCause() != null);
            if (t == null) t = e;
        }
        String errmsg = t.getClass().getName() + ": " + t.getMessage();
        StackTraceElement[] stes = t.getStackTrace();
        if (stes != null && stes.length > 0)
        {
            errmsg += CR+stes[0].toString();
            if (all)
            {
                StringBuffer sb = new StringBuffer();
                for (int i=1;i<stes.length;i++) sb.append(CR + stes[i].toString());
                errmsg += sb.toString();
            }
            else
            {
                // 找到由com.efuture引起的异常点,进行省略提示
                String privmsg = null;
                String currmsg = null;
                for (int i=1;i<stes.length;i++)
                {
                    privmsg = stes[i-1].toString();
                    if (stes[i].toString().indexOf("com.efuture.omd") >= 0)
                    {
                        if (i == 1) privmsg = null;
                        currmsg = stes[i].toString();
                        break;
                    }
                }
                if (currmsg != null)
                {
                    if (privmsg != null) errmsg += CR + "..." + CR + privmsg;
                    errmsg += CR + currmsg;
                }
            }
        }

        return errmsg;
    }

    public static Object toNormalJSONObject(Object data) {
        return toNormalJSONObject(data,null);
    }

    public static Object toNormalJSONObject(Object data, Map<String,Object> config)
    {
        JSONObject jsondata = null;

        // 先把对象转为JSONObject
        if (data instanceof JSONObject) jsondata =  ((JSONObject)data);
        else
        {
            try
            {
                if (config==null || config.size()<1) {
                    jsondata = JSON.parseObject(JSON.toJSONStringWithDateFormat(data,"yyyy-MM-dd HH:mm:ss"));
                } else {
                    final Map<String, DateFormat> params = new HashMap<String,DateFormat>();
                    Set<String> keySet = config.keySet();
                    for(String field:keySet) {
                        DateFormat format = new SimpleDateFormat(config.get(field).toString());
                        params.put(field, format);
                    }
                    jsondata = JSON.parseObject(JSON.toJSONString(data,new ValueFilter(){
                        @Override
                        public Object process(Object object, String name,Object value) {
                            if (params.containsKey(name)) {
                                DateFormat format = params.get(name);
                                return format.format(value);
                            }
                            return value;
                        }
                    }, SerializerFeature.WriteDateUseDateFormat));
                }
            }
            catch(Exception ex)
            {
                return data;
            }
        }

        // 再把Long型数据转为String,供前端JS使用,否则JS会丢失数据
        Set<String> keys = jsondata.keySet();
        for (String key : keys)
        {
            if (jsondata.get(key) instanceof Long)
            {
                jsondata.put(key, String.valueOf(jsondata.getLong(key)));
            }
            else
            if (jsondata.get(key) instanceof Double || jsondata.get(key) instanceof Float || jsondata.get(key) instanceof BigDecimal)
            {
                //对于数字类型转换做处理 ，
                //例如实际值是8.9,但显示值是8.899999999999999; 对于836.141显示成836.1410000000001

                String strValue = jsondata.getString(key);
                int index  = strValue.indexOf('.');
                if (index < 0 || (!strValue.endsWith("99999") && !strValue.endsWith("00001")) )  continue;
                String xiaoShuValue2 = strValue.substring(index+1);
                if ("".equals(xiaoShuValue2) || xiaoShuValue2.length() < 6){
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("0.");
                for (int i=0 ; i < xiaoShuValue2.length()-1 ; i++){
                    sb.append("0");
                }
                sb.append("1");
                //如果转成bigdecimal,数字会从8.899999999999999变成8.900000000000000

                double value  ;
                if (strValue.endsWith("99999"))
                {
                    value =	Double.parseDouble(strValue) +  Double.parseDouble(sb.toString()) ;
                    jsondata.put(key, Double.valueOf(value));
                }else {
                    value =	Double.parseDouble(strValue) -  Double.parseDouble(sb.toString()) ;
                }
                jsondata.put(key,value);
            }
            else
            if (jsondata.get(key) instanceof JSONObject)
            {
                jsondata.put(key, toNormalJSONObject(jsondata.getJSONObject(key)));
            }
            else
            if (jsondata.get(key) instanceof JSONArray)
            {
                JSONArray array = jsondata.getJSONArray(key);
                for (int i=0;i<array.size();i++)
                {
                    Object o = array.get(i);
                    if (o instanceof Long || o instanceof Float || o instanceof Double) array.set(i, String.valueOf(o));
                    else
                    if (o instanceof Double || o instanceof Float || o instanceof BigDecimal )
                    {
                        //对于数字类型转换做处理 ，
                        //例如实际值是8.9,但显示值是8.899999999999999; 对于836.141显示成836.1410000000001
                        String strValue = o.toString();
                        int index  = strValue.indexOf('.');
                        if (index < 0 ||  (!strValue.endsWith("99999") && !strValue.endsWith("00001")) )  continue;
                        String xiaoShuValue2 = strValue.substring(index+1);
                        if ("".equals(xiaoShuValue2) || xiaoShuValue2.length() < 6){
                            continue;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("0.");
                        for (int j=0 ; j < xiaoShuValue2.length()-1 ; j++){
                            sb.append("0");
                        }
                        sb.append("1");
                        //如果转成bigdecimal,数字会从8.899999999999999变成8.900000000000000
                        double value ;

                        if (strValue.endsWith("99999")){
                            value=	Double.parseDouble(strValue) +  Double.parseDouble(sb.toString()) ;
                        }else{
                            value=	Double.parseDouble(strValue) -  Double.parseDouble(sb.toString()) ;
                        }

                        array.set(i, value);
                    }else if(jsondata.get(key) instanceof String){
                        if(jsondata.get(key).equals("true")||jsondata.get(key).equals("false")){
                            jsondata.put(key, TypeUtils.castToBoolean(jsondata.get(key)));
                        }
                    }
                    else
                    if (o instanceof JSONObject) array.set(i, toNormalJSONObject(o));
                }
            }else if(jsondata.get(key) instanceof String){
                if(jsondata.get(key).equals("true")||jsondata.get(key).equals("false")){
                    jsondata.put(key, TypeUtils.castToBoolean(jsondata.get(key)));
                }
            }
        }
        return jsondata;
    }

    public static boolean stringArrayContainsKey(String[] array,String key,boolean ignoreCase)
    {
        if (array == null) return false;

        for (String k : array)
        {
            if (ignoreCase)
            {
                if (key.equalsIgnoreCase(k)) return true;
            }
            else
            {
                if (key.equals(k)) return true;
            }
        }

        return false;
    }
}