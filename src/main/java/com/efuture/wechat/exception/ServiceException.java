/**
 * Copyright (C), 2007-2014, eFuture 北京富基融通科技有限公司
 * FileName:	ServiceException.java
 * Author:		亮
 * Date:		2014-4-14 下午5:13:03
 * Description:	
 * History:
 * <author>		<time>			<version>		<description>
 * 
 */
package com.efuture.wechat.exception;

import java.text.MessageFormat;

/**
 * @author		亮
 * @description	
 * 
 */
public class ServiceException extends Exception
{
    /**
     * @description	
     */
    private static final long serialVersionUID = 1L;

    private String errcode;
    private Object[] errargs;
    
    public ServiceException(String errcode, String errmsg, Object...args)
    {
        super(format(errmsg,args));
        
        this.errcode = errcode;
        this.errargs = args;
    }

    public String getErrorCode()
    {
        return errcode;
    }
    
    public Object[] getErrorArgs()
    {
        return errargs;
    }
    
    private static String format(String errmsg,Object...args)
    {
        if (args == null || args.length <= 0) return errmsg;
        
        // format String
        for (int i=0;args != null && i<args.length;i++)
        {
            if (args[i] instanceof String) continue;
            args[i] = args[i].toString();
        }
        
        return MessageFormat.format(errmsg, args);
    }
}
