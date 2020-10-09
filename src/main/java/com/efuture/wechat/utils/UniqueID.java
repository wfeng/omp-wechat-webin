package com.efuture.wechat.utils;

import com.efuture.wechat.common.SpringBeanFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * //生成唯一序号19位，格式：32位二进制时间(LONG型)  + 20位二进制序号(0~999999循环的整数) + 3位十进制整数(机器号)
 * 生成唯一序号19位，格式：32位二进制时间(LONG型)  + 20位二进制序号(0~999999循环的整数) + 10位十进制整数(服务器IP)
 * 
 * @author 亮
 * 
 */
public class UniqueID
{
	/** 序号。范围（0-999999） */
	private static Integer number = new Integer(0);
	
	/** 机器号 */
	private String hostId = "";

	/**
	 * 获取ID
	 * 
	 * @return BigDecimal 唯一ID
	 */
	public long getId()
	{
		synchronized (UniqueID.class)
		{
			return createId();
		}
	}

	/**
	 * 获取一组ID
	 * 
	 * @param number
	 *            ID个数
	 * @return List<String> 唯一ID
	 */
	public List<Long> getIdArray(int number)
	{
		List<Long> retArray = new ArrayList<Long>();
		synchronized (UniqueID.class)
		{
			for (int i = 0; i < number; i++)
			{
				retArray.add(createId());
			}
		}

		return retArray;
	}

    public List<String> getNoArray(int number,int len)
    {
        List<String> retArray = new ArrayList<String>();
        synchronized (UniqueID.class)
        {
            // 时间到毫秒占13位
            StringBuffer sb = new StringBuffer();
            len = len - 13;
            String fmt = "%0"+len+"d";
            for (int i=0;i<len;i++) sb.append("9");
            int sed = Integer.parseInt(sb.toString());
            int sedmod = sed + 1;
            
            java.security.SecureRandom rand = new java.security.SecureRandom();
            //Random rand = new Random();
            for (int i = 0; i < number; i++)
            {
                long uid = 0;
                Date dt = new Date();
                uid = dt.getTime();
                
                Formatter format = new Formatter();
                String suff = format.format(fmt,rand.nextInt(sed)%sedmod).toString();                
                retArray.add(String.valueOf(uid) + suff);
            }
        }

        return retArray;
    }	
	

	/**
	 * 创建ID
	 * 
	 * @return BigDecimal ID
	 */
	private long createId()
	{
	    long uid = 0;
        Date dt = new Date();
/*        
        // 最大ID(7643726453097023999)
        dt.setYear(300);
        dt.setMonth(11);
        dt.setDate(31);
        dt.setHours(23);
        dt.setMinutes(59);
        dt.setSeconds(59);
        number = 999999;
        hostId = "999";
*/        
	    // 时间去掉毫秒(32位二进制整数,可表示到2200/12/31 23:59:59年)
	    uid   = dt.getTime() / 1000;
	    uid <<= 20;
	    
		// 加上序号(20位二进制整数,最大可表示整数999999)
        number = (number + 1) % 1000000;
		uid += number;

		// 固定用随机数避免重复
        // 使用ThreadLocalRandom.current()防止多线程重复问题
        int n = ThreadLocalRandom.current().nextInt(99)%1000;
        Formatter format = new Formatter();
        String suff = format.format("%02d",n).toString();
//        return Long.parseLong(String.valueOf(uid) + suff);

        //2019-11-19 集群部署增加IP防止重复问题
        String hostIP = getHostIP();
        if (!StringUtils.isEmpty(hostIP)) {
            return Long.parseLong(String.valueOf(uid) + suff) + ipToLong(hostIP);
        } else {
            return Long.parseLong(String.valueOf(uid) + suff);
        }

/*        
		// 加上机器号(3位10进制,最大整数999)
		return Long.parseLong(String.valueOf(uid)+hostId);
*/		
	}

	public String getHostId()
	{
		return hostId;
	}

    public void setHostId(String hostId)
    {
        // 从属性文件读取
        if (!StringUtils.isEmpty(hostId) && (hostId.startsWith("/") || hostId.startsWith("\\")))
        {
            try
            {
                hostId.replace("\\", "/");
                int pos = hostId.lastIndexOf("/");
                String file = hostId.substring(0,pos); 
                String key  = hostId.substring(pos+1);
                
                File input = new File(WebPathUtils.getWebRootPath() + file);
                InputStream is = new FileInputStream(input);
                Properties prop = new Properties();
                prop.load(is);
                is.close();
                hostId = prop.get(key).toString().trim();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
            
        // 设置
        this.hostId = hostId;
        
        // 取IP组合成HostId
        if (!StringUtils.isEmpty(hostId) && hostId.length() < 3)
        {
            String ip = getHostIP();
            if (ip == null) ip = ".000";
            String suff = new Formatter().format("%03d",Integer.parseInt(ip.substring(ip.lastIndexOf(".")+1))).toString();
            this.hostId += suff.substring(hostId.length());
        }
    }
    
    public static String getHostIP()
    {
        try
        {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) 
            {
                NetworkInterface nif = netInterfaces.nextElement();
                Enumeration<InetAddress> iparray = nif.getInetAddresses();
                while (iparray.hasMoreElements()) 
                {
                    InetAddress ip = iparray.nextElement();
                    if (ip != null)
                    {
                        //System.out.println("IP: "+ ip.getHostAddress());
                        if (ip instanceof Inet4Address) 
                        {
                            String localip = ip.getHostAddress();
                            if (!"127.0.0.1".equals(localip)) return ip.getHostAddress();
                        }
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param ipAddress
     * @return
     */
    public static long ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            // left shifting 24,16,8,0 and bitwise OR
            // 1. 192 << 24
            // 1. 168 << 16
            // 1. 1 << 8
            // 1. 2 << 0
            result |= ip << (i * 8);
        }
        return result;
    }

    /**
     * @param ip
     * @return
     */
    public static String longToIp(long ip) {
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0,Long.toString(ip & 0xff));
            if (i < 3) {
                result.insert(0,'.');
            }
            ip = ip >> 8;
        }
        return result.toString();
    }
	
    public static UniqueID getInstance()
    {
        UniqueID uniqueID = SpringBeanFactory.getBean("UniqueID", UniqueID.class);
        return uniqueID;
    }
    
	public static long getUniqueID()
	{
		return getInstance().getId();
	}

	public static List<Long> getUniqueIDArray(int size)
	{
		return getInstance().getIdArray(size);
	}
	
    public static List<String> getUniqueNoArray(int size,int len)
    {
        return getInstance().getNoArray(size,len);
    }
}
