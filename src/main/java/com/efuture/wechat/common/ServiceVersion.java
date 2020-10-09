/**
 * Copyright (C), 2007-2014, eFuture 北京富基融通科技有限公司
 * FileName:	ServiceVersion.java
 * Author:		亮
 * Date:		2014-10-31 上午9:42:23
 * Description:	
 * History:
 * <author>		<time>			<version>		<description>
 * 
 */
package com.efuture.wechat.common;

import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.utils.DataUtils;
import com.efuture.wechat.utils.DateUtils;
import com.efuture.wechat.utils.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author		亮
 * @description	
 * 
 */
public class ServiceVersion
{
    private static Logger logger = LoggerFactory.getLogger(ServiceVersion.class);

    boolean logstatus;
    
    String ver;    
    
    String logformat;

	public String getLogFormat() {
		if (logformat == null) {
			return "default";
		}
		return logformat;
	}

	public void setLogFormat(String logFormat) {
		this.logformat = logFormat;
	}

    public ServiceVersion(String version, String openlog, String logformat)
    {
        this.ver = version;
        this.logstatus = openlog.equalsIgnoreCase("y");
        this.logformat = logformat;
    }
    public ServiceVersion(String version, String openlog)
    {
        ver = version;
        logstatus = openlog.equalsIgnoreCase("y");
    }
    public ServiceVersion(String version)
    {
        ver = version;
    }
    
    public String getVer()
    {
        return ver;
    }

    public boolean isLogstatus()
    {
        return logstatus;
    }

    public void setLogstatus(boolean logstatus)
    {
        this.logstatus = logstatus;
    }

    public void debugLog(Object message)
    {
        if (logstatus) logger.info("", message);
    }

    String sinceTime = DateUtils.getNowStr();
    Map<String,JSONObject> elapsed = new HashMap<String,JSONObject>();
    long[] elapsedStep = new long[]{200,300,500,1000,3000,5000};
    String[] elapsedKeys = new String[]{"<=200","200-300","300-500","500-1000","1000-3000","3000-5000",">5000"};

    public Map<String, JSONObject> getElapsed()
    {
        return elapsed;
    }
    public void setElapsed(Map<String, JSONObject> elapsed)
    {
        this.elapsed = elapsed;
    }

    public String printElapsed() throws Exception
    {
        StringBuilder sb = new StringBuilder(  );
        sb.append("<style>" );
        sb.append("table{border:1px solid #cad9ea;text-align:center;cellspacing:0px;cellpadding:50px} ");
        sb.append("table th{border:1px solid #cad9ea;text-align:center;cellspacing:0px;cellpadding:50px} " );
        sb.append("table tr td{border:1px solid #cad9ea;text-align:center;cellspacing:0px;cellpadding:50px} ");
        sb.append("</style>");
        sb.append( "IpAddr: " ).append( NetworkUtils.getLocalIP() ).append( "<br>" );
        sb.append( "Port: " ).append( NetworkUtils.getLocalPort() ).append( "<br>" );
        sb.append( "Since: " ).append( sinceTime ).append( "<br>" );
        sb.append( "<TABLE>" );
        boolean printHeader = false;
        for (Map.Entry<String, JSONObject> entry : elapsed.entrySet())
        {
            if (!printHeader)
            {
                sb.append( "<tr style='background-color: #CCE8EB;'>" );
                sb.append("<td rowspan=2>方法</td>");
                sb.append("<td rowspan=2>调用次数</td>");
                sb.append("<td rowspan=2>累计耗时</td>");
                sb.append("<td rowspan=2>平均耗时</td>");
                sb.append("<td colspan="+String.valueOf(elapsedKeys.length)+">分段统计</td>");
                sb.append("</tr><tr style='background-color: #CCE8EB'>" );
                for (String s : elapsedKeys)
                {
                    sb.append("<td>");
                    sb.append( s );
                    sb.append("</td>");
                }
                sb.append( "</tr><tr>" );
                printHeader = true;
            }
            else
            {
                sb.append( "<tr>" );
            }
            sb.append("<td style='text-align:left'>" + entry.getKey() + "</td>");
            sb.append( "<td>" + DataUtils.getJsonData(entry.getValue(), "num", false, "0") + "</td>");
            sb.append("<td>" + DataUtils.getJsonData(entry.getValue(), "tot", false, "0") + "</td>");
            sb.append("<td>" + DataUtils.getJsonData(entry.getValue(), "avg", false, "0") + "</td>");
            for (String s : elapsedKeys)
            {
                sb.append("<td>");
                sb.append( DataUtils.getJsonData(entry.getValue(), s, false, "&nbsp;") );
                sb.append("</td>");
            }
            sb.append( "</tr>" );
        }
        sb.append( "<TABLE>" );
        return sb.toString();
    }

    public synchronized void writeElapsed(String key,long time)
    {
        try
        {
            // 0耗时的不记录
            if (time <= 0) return;

            // 总次数,总耗时,平均耗时
            JSONObject json = elapsed.get(key);
            if (json == null)
            {
                json = new JSONObject();
                json.put("num", 1);
                json.put("tot", time);
                json.put("avg", time);
                this.elapsed.put(key,json);
            }
            else
            {
                json.put("num", json.getLong("num")+1);
                json.put("tot", json.getLong("tot")+time);
                json.put("avg", json.getLong("tot") / json.getLong("num"));
            }

            // 分档耗时
            boolean exist = false;
            for (int i=0;i<elapsedStep.length;i++)
            {
                if (time <= elapsedStep[i])
                {
                    String str = (i > 0 ? elapsedStep[i-1] + "-" + elapsedStep[i] : "<=" + elapsedStep[i]);
                    if (json.containsKey(str)) json.put(str, json.getLong(str)+1);
                    else json.put(str, 1);

                    exist = true;
                    break;
                }
            }
            if (!exist)
            {
                String str = ">" + elapsedStep[elapsedStep.length-1];
                if (json.containsKey(str)) json.put(str, json.getLong(str)+1);
                else json.put(str, 1);
            }

            //System.out.println(json.toJSONString());
        }
        catch(Exception ex)
        {
        }
    }

    public synchronized void cleanElapsed() throws Exception
    {
        elapsed.clear();
    }
    
    public static ServiceVersion getInstance()
    {
        ServiceVersion sv = SpringBeanFactory.getBean("ServiceVersion", ServiceVersion.class);
        return sv;
    }
    
    public static String getVersion()
    {
        return getInstance().getVer();
    }
    
}
