/**
 * Copyright (C), 2007-2014, eFuture 北京富基融通科技有限公司 FileName: UrlUtil.java Author:
 * 亮 Date: 2014-6-26 下午3:43:53 Description: History: <author> <time> <version>
 * <description>
 * 
 */
package com.efuture.wechat.utils;

import java.io.File;
import java.net.URL;

/**
 * @author 亮
 * @description
 * 
 */
public class WebPathUtils
{
    /**
     * 取得当前类所在的文件
     * 
     * @param clazz
     * @return
     */
    public static File getClassFile(Class<?> clazz)
    {
        URL path = clazz.getResource(clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1) + ".classs");
        if (path == null)
        {
            String name = clazz.getName().replaceAll("[.]", "/");
            path = clazz.getResource("/" + name + ".class");
        }
        return new File(path.getFile());
    }

    /**
     * 得到当前类的路径
     * 
     * @param clazz
     * @return
     */
    public static String getClassFilePath(Class<?> clazz)
    {
        try
        {
            return java.net.URLDecoder.decode(getClassFile(clazz).getAbsolutePath(), "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 取得当前类所在的ClassPath目录，比如tomcat下的classes路径
     * 
     * @param clazz
     * @return
     */
    public static File getClassPathFile(Class<?> clazz)
    {
        File file = getClassFile(clazz);
        for (int i = 0, count = clazz.getName().split("[.]").length; i < count; i++)
        {
            file = file.getParentFile();
        }
        if (file.getName().toUpperCase().endsWith(".JAR!"))
        {
            file = file.getParentFile();
        }
        return file;
    }

    /**
     * 取得当前类所在的ClassPath路径
     * 
     * @param clazz
     * @return
     */
    public static String getClassPath(Class<?> clazz)
    {
        try
        {
            return java.net.URLDecoder.decode(getClassPathFile(clazz).getAbsolutePath(), "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWebRootClassPath()
    {
        return getWebRootPath() + "/WEB-INF/classes";
    }
    
    public static String getWebRootPath()
    {
        //因为类名为"WebPathUtils"，因此" WebPathUtils.class"一定能找到    
        String result = WebPathUtils.class.getResource("WebPathUtils.class").toString();
        
        int index = result.indexOf("WEB-INF");

        if (index == -1)
        {
            index = result.indexOf("bin");
        }
        
        if (index == -1)
        {
            return System.getProperty("user.dir");
        }
        else
        {
            result = result.substring(0, index);
    
            if (result.startsWith("jar"))
            {
                // 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径     
                result = result.substring(10);
            }
            else if (result.startsWith("wsjar:"))
            {
                // 当class文件在wsjar文件中时，返回"wsjar:file:/F:/ ..."样的路径     
                result = result.substring(12);
            }
            else if (result.startsWith("\\vfszip:\\"))
            {
                // 当class文件在wsjar文件中时，返回"\vfszip:\ ..."样的路径     
                result = result.substring(9);
            }
            else if (result.startsWith("file"))
            {
                // 当class文件在class文件中时，返回"file:/F:/ ..."样的路径     
                result = result.substring(6);
            }

            if (result.endsWith("/"))
            {
                result = result.substring(0, result.length() - 1); //不包含最后的"/"    
            }
            if (!result.startsWith("/") && !result.startsWith("\\"))
            {
                result = "/"+result;                               //最前面从"/"根开始绝对路径
            }
            
            return result;
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println(getClassFilePath(WebPathUtils.class));
        System.out.println(getClassPath(WebPathUtils.class));
        System.out.println(getWebRootPath());
    }
}
