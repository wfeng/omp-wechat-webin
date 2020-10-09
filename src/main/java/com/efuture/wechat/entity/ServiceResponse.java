/**
 * Copyright (C), 2007-2014, eFuture 北京富基融通科技有限公司
 * FileName:	ServiceReturn.java
 * Author:		亮
 * Date:		2014-3-27 下午4:36:10
 * Description:	
 * History:
 * <author>		<time>			<version>		<description>
 * 
 */
package com.efuture.wechat.entity;

import com.efuture.wechat.constant.ResponseCode;
import com.efuture.wechat.language.MessageSourceHelper;

import java.util.Locale;

/**
 * @author		亮
 * @description	API应答
 *
 */
public class ServiceResponse
{
	String returncode;
	Object data;

	/**
	 * @return returncode
	 */
	public String getReturncode()
	{
		return returncode;
	}
	/**
	 * @param returncode 要设置的 returncode
	 */
	public void setReturncode(String returncode)
	{
		this.returncode = returncode;
	}

	/**
	 * @return data
	 */
	public Object getData()
	{
		return data;
	}

	/**
	 * @param data 要设置的 data
	 */
	public void setData(Object data)
	{
		this.data = data;
	}

	//////////////////////////////////////////////////////////////
	public static ServiceResponse buildSuccess(Object obj)
	{
		ServiceResponse response = new ServiceResponse();
		response.setReturncode(ResponseCode.SUCCESS);
		response.setData(obj);
		return response;
	}

	public static ServiceResponse buildFailure(ServiceSession session, String returncode)
	{
		return buildFailure(session,returncode,null);
	}

	public static ServiceResponse buildFailure(ServiceSession session, String returncode, String formatmsg, Object... args)
	{
		ServiceResponse response = new ServiceResponse();

		response.setReturncode(returncode);
		response.setData(MessageSourceHelper.getDefault().getMessage(returncode, formatmsg,
				session != null && session.getLocale() != null ? new Locale(session.getLocale()) : null,args));
		return response;
	}

	/**
	 * 2018-06-23 zhou.liu 添加，用于直接返回错误信息
	 * @param session
	 * @param returncode
	 * @param msg
	 * @return
	 */
	public static ServiceResponse buildFailure(ServiceSession session, String returncode, String msg)
	{
		ServiceResponse response = new ServiceResponse();
		response.setReturncode(returncode);
		response.setData(msg);
		return response;
	}
}
