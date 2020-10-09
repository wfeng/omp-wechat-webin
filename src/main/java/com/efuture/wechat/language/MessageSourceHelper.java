/**
 * Copyright (C), 2007-2014, eFuture 北京富基融通科技有限公司
 * FileName:	MessageSourceHelper.java
 * Author:		亮
 * Date:		2014-3-31 下午1:25:24
 * Description:	
 * History:
 * <author>		<time>			<version>		<description>
 * 
 */
package com.efuture.wechat.language;

import com.efuture.wechat.common.SpringContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * @author		亮
 * @description	
 * 
 */
public class MessageSourceHelper
{
	private ResourceBundleMessageSource messageSource;
	
	public static MessageSourceHelper getDefault()
	{
		return SpringContext.getBean("messageSourceHelper", MessageSourceHelper.class);
	}
	
	public static String getMessage(String code,String locale,Object... args)
	{
	    return getDefault().getMessage(code, code, StringUtils.isEmpty(locale)?null:new Locale(locale),args);
	}
	
    public String getMessage(String code, String defaultMessage, Locale locale,Object... args)
    {
        // format String
        for (int i=0;args != null && i<args.length;i++)
        {
            if (args[i] instanceof String) continue;
            args[i] = String.valueOf(args[i]);
        }
        
        String msg = messageSource.getMessage(code, args, defaultMessage, locale);
        
        // 通用错误码则拼接传入的具体格式消息
//        if (code.equalsIgnoreCase(ResponseCode.FAILURE) || code.equalsIgnoreCase(ResponseCode.EXCEPTION))
//        {
//            if (args != null && args.length > 0 && !StringUtils.isEmpty(defaultMessage)) defaultMessage = MessageFormat.format(defaultMessage, args);
//            msg += defaultMessage;
//        }
        
        return msg != null ? msg.trim() : msg;
    }
   
    public void setMessageSource(ResourceBundleMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
}
