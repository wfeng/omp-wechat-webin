package com.efuture.wechat.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.constant.ResponseCode;
import com.efuture.wechat.exception.ServiceException;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataUtils
{

    public static String nvl(String BeanVal, String... DefaultVal)
    {
        if (StringUtils.isEmpty(BeanVal))
        {
            for (String val : DefaultVal)
            {
                if (!StringUtils.isEmpty(val))
                {
                    return val;
                }
            }
            return null;
        }
        else
        {
            return BeanVal;
        }
    }

    public static double nvl(double BeanVal, double... DefaultVal)
    {
        if (StringUtils.isEmpty(BeanVal) || BeanVal == -1.0)
        {
            // return DefaultVal;
            for (double val : DefaultVal)
            {
                if (!(StringUtils.isEmpty(val) || val == -1.0))
                {
                    return val;
                }
            }
            return 0D;
        }
        else
            return BeanVal;
    }

    public static int nvl(int BeanVal, int... DefaultVal)
    {
        if (StringUtils.isEmpty(BeanVal) || BeanVal == 0)
        {
            // return DefaultVal;
            for (int val : DefaultVal)
            {
                if (!(StringUtils.isEmpty(val) || val == 0))
                {
                    return val;
                }
            }
            return 0;
        }
        else
            return BeanVal;
    }

    public static long nvl(long BeanVal, long... DefaultVal)
    {
        if (StringUtils.isEmpty(BeanVal) || BeanVal == 0L)
        {
            // return DefaultVal;
            for (long val : DefaultVal)
            {
                if (!(StringUtils.isEmpty(val) || val == 0L))
                {
                    return val;
                }
            }
            return 0L;
        }
        else
            return BeanVal;
    }

    public static Object checkNull(Object obj, String colName) throws Exception
    {
        if (StringUtils.isEmpty(obj))
            throw new Exception(colName.concat(" 必须传入!"));
        if (obj instanceof JSONArray)
        {
            if (((JSONArray) obj).size() <= 0)
                throw new Exception(colName.concat(" 必须包含有效内容!"));
        }
        return obj;
    }

    public static Object checkValid(Object obj, String colName, String validVals) throws Exception
    {
        if (StringUtils.isEmpty(obj))
            throw new Exception(colName.concat(" 必须传入!"));
        if (StringUtils.isEmpty(validVals))
            return obj;

        if (">0".equals(validVals))
        {
            if (PrecisionUtils.doubleCompare(Double.parseDouble(obj.toString()), 0, 4) <= 0)
            {
                throw new Exception(colName.concat(" 不合法，必须 >0 !"));
            }
        }
        else if (">=0".equals(validVals))
        {
            if (PrecisionUtils.doubleCompare(Double.parseDouble(obj.toString()), 0, 4) < 0)
            {
                throw new Exception(colName.concat(" 不合法，必须 >=0 !"));
            }
        }
        else
        {
            List<String> vals = Arrays.asList(validVals.split(","));
            if (!vals.contains(obj))
            {
                throw new Exception(colName.concat(" 不合法，必须在 [" + validVals + "] 范围内!"));
            }
        }
        return obj;
    }

    public static void setDefaultValue(Object obj, String defaultValue) throws Exception
    {

    }

    public static String rpad(String src, int len, char ch)
    {
        int diff = len - src.length();
        if (diff <= 0)
        {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++)
        {
            charr[i] = ch;
        }
        return new String(charr);
    }

    public static String lpad(String src, int len, char ch)
    {
        int diff = len - src.length();
        if (diff <= 0)
        {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++)
        {
            charr[i] = ch;
        }
        return new String(charr);
    }

    public static JSONArray AddToArray(String key, JSONArray arr)
    {
        JSONArray result = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(key, arr);
        result.add(jsonObj);
        return result;
    }

    public static JSONArray AddToArray(JSONObject obj)
    {
        JSONArray result = new JSONArray();
        result.add( obj );
        return result;
    }

    public static String Add_Days(JSONObject json, String key, int days)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(json) || !json.containsKey(key))
        {
            return null;
        }

        try
        {
            return sdf.format(DateUtils.addDays(sdf.parse(json.getString(key)), days));
        } catch (java.text.ParseException e)
        {
            return null;
        }

    }
    
    public static Map<String,Object> cloneMap(Map<String,Object> map)
    {
        Map<String,Object> result = new ConcurrentHashMap<String,Object>();
    
        for (Map.Entry entry : map.entrySet())
        {
            result.put(entry.getKey().toString(), entry.getValue());
        }

        return result;
    }

    public static String getJsonData(JSONObject param, String key, boolean checknull, String defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getString(key);
    }

    public static int getJsonData(JSONObject param, String key, boolean checknull, int defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getInteger(key);
    }

    public static long getJsonData(JSONObject param, String key, boolean checknull, long defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getLongValue(key);
    }

    public static double getJsonData(JSONObject param, String key, boolean checknull, double defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getDoubleValue(key);
    }

}
