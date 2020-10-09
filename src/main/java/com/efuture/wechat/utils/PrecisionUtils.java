package com.efuture.wechat.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;


//这个类用于控制精度
public class PrecisionUtils
{
    private static final int DEF_DIV_SCALE = 10;

    public PrecisionUtils()
    {
    }

    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
                 .doubleValue();
    }
    
    // 取余数
    public static double mod(double v1, double v2)
    {
    	if (v2 == 0 ) return v1;
    	
    	double a = div(v1,v2);
    	int b = (int)a;
    	double  c = mul(b,v2);
    	return sub(Math.abs(v1),Math.abs(c));
    }

    //比较小数
    public static int doubleCompare(double f1, double f2, int dec)
    {
        int i;
        float f = 1.00F;

        for (i = 0; i <= dec; i++)
        {
            f /= 10.00;
        }

        f1 = doubleConvert(f1, dec, 1);
        f2 = doubleConvert(f2, dec, 1);

        if (Math.abs(f1 - f2) <= f)
        {
            return 0;
        }

        if (f1 > f2)
        {
            return 1;
        }

        return -1;
    }

    public static int getDoubleSign(double f)
    {
        return (doubleCompare(f, 0, 2) >= 0) ? 1 : (-1);
    }
    
    public static int getDoubleScale(double f)
    {
    	int i = 0;
    	double d;
    	
    	do {
    		d = f * Math.pow(10.0,i);
    		if (d == (long)d) return i;
    		i++;
    	} while(true);
    }
    
    public static String doubleToString(double value)
    {
    	return doubleToString(value,2,1,false,0);
    }

    public static String doubleToString(double value,int dec,int flag)
    {
    	return doubleToString(value,dec,flag,false,0);
    }
    
    public static String doubleToString(double value,int dec,int flag,boolean subdec)
    {
    	return doubleToString(value,dec,flag,subdec,0);
    }
    
    public static String doubleToString(double value,int dec,int flag,boolean subdec,int rightwidth)
    {
    	StringBuffer sb = new StringBuffer("0");

    	if (dec > 0) sb.append(".");
    	for(int i=0;i<dec;i++) sb.append("0");
    	
    	DecimalFormat df = new DecimalFormat(sb.toString());
    	
    	String s = df.format(doubleConvert(value,dec,flag));

    	// 去掉尾部0
    	if (subdec)
    	{
    		while(s.charAt(s.length()-1) == '0' || s.charAt(s.length()-1) == '.')
    		{
    			if (s.charAt(s.length()-1) == '.')
    			{
    				s = s.substring(0,s.length() - 1);
    				break;
    			}
    			else
    			{
    				s = s.substring(0,s.length() - 1);
    			}
    		}
    	}
    	
    	if (rightwidth > 0)
    	{
    		s = increaseCharForward(s,rightwidth);
    	}

    	return s;
    }
    
    public static String increaseChar(String str, int num)
    {
        return increaseChar(str, ' ',num);
    }
    
    public static String increaseChar(String str, char c,int num)
    {
        int limit;

        if (num <= 0)
        {
            limit = 16;
        }
        else
        {
            limit = num;
        }

        int len = str.length();

        for (int i = len; i < limit; i++)
        {
            str += c;
        }

        if (str.length() > limit) return str.substring(str.length() - limit);
        else return str;
    }
    
    public static String increaseCharForward(String str, int num)
    {
        return increaseCharForward(str, ' ',num);
    }
    
    public static String increaseCharForward(String str, char c,int num)
    {
        int limit;

        if (num <= 0)
        {
            limit = 16;
        }
        else
        {
            limit = num;
        }

        int len = str.length();

        for (int i = len; i < limit; i++)
        {
            str = c + str;
        }

        if (str.length() > limit) return str.substring(str.length() - limit);
        else return str;
    }
    
    //浮点运算1       = 0.999999,需要进位到两位小数再取整
    //浮点运算299/300 = 0.996666,进位取整=1,还需再乘分母用金额比较，如果大倍数要减1
    public static int integerDiv(double f1,double f2)
    {
    	double fz = Math.abs(f1);
    	double fm = Math.abs(f2);
    	if (fm == 0) fm = 1;
    	
    	int num = (int)doubleConvert(fz/fm);
    	if (doubleCompare(fm * num,fz,2) > 0) num--;
    	if (num < 0) num = 0;
    	
        // 除0至少是1倍
        if (Math.abs(f2) == 0 && Math.abs(f1) > 0 && num == 0) num = 1;
        
    	return num;
    }
    
    public static double doubleConvert(double f)
    {
    	return doubleConvert(f,2,1);
    }
    
    public static double doubleConvert(double f,int dec)
    {
        return doubleConvert(f,dec,1);
    }
    
    //转换数,f:表示传入值,dec:表示精确位数,flag:表示0就是截断,1表示四舍五入
    public static double doubleConvert(double f, int dec, int flag)
    {
        return doubleConvert(f,dec,flag,false);
    }
    
    //转换数,f:表示传入值,dec:表示精确位数,flag:表示0就是截断,1表示四舍五入
    public static double doubleConvert(double f, int dec, int flag,boolean r)
    {
        if (flag == 1)
        {	
        	double d = f;
        	
        	// 先计算数字f 精度后2位的精度 
        	if (!r) d = doubleConvert(d,dec+2,1,true);
        	if (!r && dec > 0) d = doubleConvert(d * Math.pow(10.0,dec),0,1);
        	else d = d * Math.pow(10.0,dec);
        	return (Math.round(d))/(Math.pow(10.0,dec));
        }
        else
        {
        	//BigDecimal b = new BigDecimal(Math.abs(f)).setScale(dec, BigDecimal.ROUND_FLOOR); 
        	//return b.doubleValue() * (f < 0?-1:1);
        	double d = Math.abs(f);
        	if (!r) d = doubleConvert(d,dec+2,1,true);
        	d = d * Math.pow(10.0,dec);
  		    return (Math.floor(d))/(Math.pow(10.0,dec) * (f < 0?-1:1));
        }
    }
    
    // 删除数字已外的字符
    public static String getFilterNumberNoStr(String str)
    {
    	String str1 = "";
    	
    	for (int i = 0;i < str.length();i++)
    	{
    		if (isNumber(String.valueOf(str.charAt(i))))
    		{
    			str1 = str1 + str.charAt(i);
    		}
    	}
    	
    	return str1;
    }
    
    public static boolean isNumber(String str)
    {
    	try
    	{
    		if (str.matches("^(-?\\d+)(\\d+)?$"))
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		return false;
    	}
    }
    
    public static boolean isDoubleOrNumber(String str)
    {
    	try
    	{
    		if (str.matches("^(-?\\d+)(\\.\\d+)?$"))
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		return false;
    	}
    }
    
}
