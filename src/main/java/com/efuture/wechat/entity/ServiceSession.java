/**
 * Copyright (C), 2007-2014, eFuture 北京富基融通科技有限公司
 * FileName:	SessionInfo.java
 * Author:		亮
 * Date:		2014-3-27 上午10:12:58
 * Description:	
 * History:
 * <author>		<time>			<version>		<description>
 * 
 */
package com.efuture.wechat.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * @author		亮
 * @description	服务Session信息
 * 
 */
public class ServiceSession
{
	protected long ent_id;
	protected long user_id;
	protected String user_code;
	protected String user_name;
	protected String locale;
	protected String debug_status;
	protected String token;
	protected String postid;
	protected String deptrealcode;
	protected String deptcode;
	protected String roleid;
	protected String rolecode;
	protected String dataRangeId;//数据范围id,新的portal使用

	// 日志
	protected String rootkey;//调用的【根日志序号】
	protected String parentkey;//调用的【上级日志序号】
	protected String logkey;//本级调用的【日志序号】
	protected long elapsed;//调用耗时 毫秒
	protected int orderkey;//调用顺序
	protected String method;//调用的方法
	protected String remoteaddr;//调用IP地址
	protected String url;//调用url
	protected String returncode; //返回值
	protected long starttime;

	public String getDeptrealcode()
	{
		return deptrealcode;
	}

	public void setDeptrealcode(String deptrealcode)
	{
		this.deptrealcode = deptrealcode;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getPostid()
	{
		return postid;
	}

	public void setPostid(String postid)
	{
		this.postid = postid;
	}

	/**
	 * @return ent_id
	 */
	public long getEnt_id()
	{
		return ent_id;
	}

	/**
	 * @param ent_id 要设置的 ent_id
	 */
	public void setEnt_id(long ent_id)
	{
		this.ent_id = ent_id;
	}

	/**
	 * @return user_id
	 */
	public long getUser_id()
	{
		return user_id;
	}

	/**
	 * @param user_id 要设置的 user_id
	 */
	public void setUser_id(long user_id)
	{
		this.user_id = user_id;
	}

	/**
	 * @return user_code
	 */
	public String getUser_code()
	{
		if (StringUtils.isEmpty( user_code ))
		{
			return String.valueOf( user_id );
		}
		else
		{
			return user_code;
		}
	}

	/**
	 * @param user_code 要设置的 user_code
	 */
	public void setUser_code(String user_code)
	{
		this.user_code = user_code;
	}

	/**
	 * @return user_name
	 */
	public String getUser_name()
	{
		return user_name;
	}

	/**
	 * @param user_name 要设置的 user_name
	 */
	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}

	/**
	 * @return locale
	 */
	public String getLocale()
	{
		return locale;
	}

	/**
	 * @param locale 要设置的 locale
	 */
	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	/**
	 * @return debug_status
	 */
	public String getDebug_status()
	{
		return debug_status;
	}

	/**
	 * @param debug_status 要设置的 debug_status
	 */
	public void setDebug_status(String debug_status)
	{
		this.debug_status = debug_status;
	}

	public String getDeptcode()
	{
		return deptcode;
	}

	public void setDeptcode(String deptcode)
	{
		this.deptcode = deptcode;
	}

	public String getRoleid()
	{
		return roleid;
	}

	public void setRoleid(String roleid)
	{
		this.roleid = roleid;
	}

	public String getRolecode()
	{
		return rolecode;
	}

	public void setRolecode(String rolecode)
	{
		this.rolecode = rolecode;
	}

	public String getRootkey()
	{
		return rootkey;
	}

	public void setRootkey(String rootkey)
	{
		this.rootkey = rootkey;
	}

	public String getParentkey()
	{
		return parentkey;
	}

	public void setParentkey(String parentkey)
	{
		this.parentkey = parentkey;
	}

	public String getLogkey()
	{
		return logkey;
	}

	public void setLogkey(String logkey)
	{
		this.logkey = logkey;
	}

	public long getElapsed()
	{
		return elapsed;
	}

	public void setElapsed(long elapsed)
	{
		this.elapsed = elapsed;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getRemoteaddr()
	{
		return remoteaddr;
	}

	public void setRemoteaddr(String remoteaddr)
	{
		this.remoteaddr = remoteaddr;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getReturncode()
	{
		return returncode;
	}

	public void setReturncode(String returncode)
	{
		this.returncode = returncode;
	}

	public long getStarttime()
	{
		return starttime;
	}

	public void setStarttime(long starttime)
	{
		this.starttime = starttime;
	}

	public String getDataRangeId()
	{
		return dataRangeId;
	}

	public void setDataRangeId(String dataRangeId)
	{
		this.dataRangeId = dataRangeId;
	}

	public int getOrderkey()
	{
		return orderkey;
	}

	public void setOrderkey(int orderkey)
	{
		this.orderkey = orderkey;
	}

	public String getCurOrderKey() { this.orderkey ++ ; return String.valueOf(this.orderkey); }
}
