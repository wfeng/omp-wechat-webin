package com.efuture.ocp.common.log4j;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.status.StatusLogger;

import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Log4j2 设置实例ID
 * 1.如果路径中含有数字,内网IP地址最后段(3位)+取最后的数字字符串(3位) 
 * 2.如果路径中不含有数字,内网IP地址倒数2段(3位)+内网IP地址最后段(3位)
 * 3.<configuration status="WARN" monitorInterval="30" packages="com.efuture.ocp.common.log4j">
 * 4.配置中使用环境变量OCMOMP_APPID指定,获取通过ip前缀指定OCMOMP_APPID_PREFIX
 * 然后会附加在日志文件后面。 
 * @author 肖志文 
 */
@Plugin(name = "efuture", category = StrLookup.CATEGORY)
public class EfutureLookup extends AbstractLookup {
	private static final Logger LOGGER = StatusLogger.getLogger();
	private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
	private static Map<String, String> IPS = new HashMap<String, String>();

	private static String port    = "";
	private static String ipAddr0 = "000";
	private static String ipAddr1 = "000";
	private static String ip = "";
	private static Map<String, String> efutureEnv = new HashMap<String, String>();

	public EfutureLookup() {
	}

	public String lookup(final LogEvent event, final String key) {
		try {
			if (key == null || "".equals(key)) return "";
			if (!efutureEnv.containsKey(key)) {
				synchronized(efutureEnv) {
					if (!efutureEnv.containsKey(key)) {
						if (key.startsWith("appId")) {
							getPort();
							String skey = key.substring(5).trim();
							String[] addr = getIpAddr(skey);
							String _appId = "";
							if ("".equals(port)) {
								_appId = addr[1] + addr[2];
							} else {
								_appId = addr[2] + port;
							}
							LOGGER.warn("Efuture [{}] LogId [{}]", key, _appId);
							efutureEnv.put(key, _appId);
						} else if (key.equals("OCMOMP_APPID") || key.equals("EFUTURE_APPID")) {
							getPort();
							String val = System.getProperty(key);
							if (val == null) {
								val = System.getenv(key);
							}
							LOGGER.warn("env [{}] [{}]", key, val);
							String prefix = null, _appId = "";
							if (val == null || "".equals(val)) {
								prefix = System.getProperty(key + "_PREFIX");
								if (prefix == null) {
									prefix = System.getenv(key + "_PREFIX");
								}
								LOGGER.warn("env [{}_PREFIX] [{}]", key, prefix);
								String skey = prefix == null?"":prefix;
								String[] addr = getIpAddr(skey);
								
								if ("".equals(port)) {
									_appId = addr[1] + addr[2];
								} else {
									_appId = addr[2] + port;
								}
							} else {
								_appId = val;
							}
							LOGGER.warn("Efuture [{}] LogId [{}]", key, _appId);
							efutureEnv.put(key, _appId);
						} else {
					        try {
					        	String val = System.getProperty(key);
								if (val == null) {
									val = System.getenv(key);
								}
								efutureEnv.put(key, val);
								LOGGER.warn("Efuture [{}] value [{}]", key, val);
					        } catch (final Exception ex) {
					            LOGGER.warn(LOOKUP, "Error while getting system property [{}].", key, ex);
					            return null;
					        }
						}
					}
				}
			}
			return efutureEnv.get(key);
		} catch (final Exception ex) {
			LOGGER.warn(LOOKUP, "Error while getting property [{}].", ex);
			return null;
		}
	}

	private static void getPort() {
		String path = "";
		try {
			path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			LOGGER.warn("获取log4j运行路径失败[{}]", path, e);
			path = "";
		}
		LOGGER.warn("efuture path:[{}]", path);
		// LOGGER.warn("efuture path:[{}]",
		// Thread.currentThread().getClass().getResource(""));
		// LOGGER.warn("efuture path:[{}]",
		// EfutureLookup.class.getResource("").getPath());
		String regex = "\\d+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(path);
		while (matcher.find()) {
			String s = matcher.group();
			if (s.length() > 3) {
				port = matcher.group();// 匹配最后的数字
			}
		}
		if (port != null && port.length() > 3) {
			port = port.substring(port.length() - 3);
		} else {
			port = "";
		}
		LOGGER.warn("efuture port:[{}]", port);
	}

	private static String[] getIpAddr(String prefix) {
		try {
			// InetAddress.getLocalHost().getHostAddress();
			/* 获取所有IP地址 如果存在所需要内网ip就退出了*/ 
			InetAddress lo = InetAddress.getLocalHost();
			if (!lo.isAnyLocalAddress() && !lo.isLoopbackAddress() && lo instanceof Inet4Address) {
				String key = lo.getHostAddress();
				IPS.put(key, lo.getHostName());
				if (isIpv4(key) && key.startsWith(prefix)) {// IPV4 同时内网 
					ip = key; 
				}
			}
		} catch(UnknownHostException  e) {
			LOGGER.warn("警告:getLocalHost系统获取IP地址异常", e);
		}
		if ("".equals(ip)) {
			try {
				// InetAddress.getLocalHost().getHostAddress();
				/* 获取所有IP地址 如果存在所需要内网ip就退出了*/ 
				Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		        for (NetworkInterface netint : Collections.list(nets)) {
		        	//去掉虚拟网卡
					if (netint.isLoopback() || !netint.isUp() || netint.isVirtual() || netint.isPointToPoint()) { // || netint.isPointToPoint() (VPN也可以包含在里面)
						continue;
					}
					List<InterfaceAddress> netips = netint.getInterfaceAddresses();
					for(InterfaceAddress addr : netips) {
						InetAddress ia = addr.getAddress();
						if (ia instanceof Inet4Address) {
							String key = ia.getHostAddress();
							IPS.put(key, ia.getHostName());
							if (isIpv4(key) && isInternalIp(key) && key.startsWith(prefix)) {// IPV4 同时内网 
								ip = key; break;
							}
						}
					}
				}
			} catch (java.net.SocketException e) {
				LOGGER.warn("警告:系统获取IP地址异常", e);
			}
		}
		String[] rtn = null, address = null;
		if ("".equals(ip)) {
			IPS.put("127.0.0.1", "localhost");
			IPS.put("0.0.0.0", "localhost");
			
			for (String key : IPS.keySet()) {
				if (isIpv4(key) && isInternalIp(key) && key.startsWith(prefix)) {// IPV4 同时内网
					ip = key; break;
				}
			}
			if ("".equals(ip) || "0.0.0.0".equals(ip)) {
				for (String key : IPS.keySet()) {// 只要是IPV4
					if (isIpv4(key) && key.startsWith(prefix)) {
						ip = key; break;
					}
				}
			}
			if ("".equals(ip)) {
				ip = "127.0.0.1";
			}
		}
		address = ip.split("\\.");
		ipAddr0 = address[3]; ipAddr1= address[2];
		
		rtn = new String[]{ip, String.format("%03d", Integer.parseInt(ipAddr1)), String.format("%03d", Integer.parseInt(ipAddr0))};
		LOGGER.warn("efuture ip 地址[{}], [{}], [{}]", rtn[0], rtn[1], rtn[2]);
		return rtn;
	}
	

	// ipv4是32位地址，分成4段，每段之间都有 "." 分开，而每段之间有8位，从 0 - 255.
	// pv6是128位地址，每个数目等于4位（0-F）16位进制，4个一组，每段之间由 ":"隔开，共有8段
	private static boolean isIpv4(String ip) {
		if (ip.indexOf('.') != -1)
			return true;
		else
			return false;
	}

	private static boolean isInternalIp(String ip) {
		byte[] addr = textToNumericFormatV4(ip);
		return isInternalIp(addr);
	}

	/**
	 * tcp/ip协议中，专门保留了三个IP地址区域作为私有地址，其地址范围如下：
	 * 
	 * 10.0.0.0/8：10.0.0.0～10.255.255.255  172.16.0.0/12：172.16.0.0～172.31.255.255 
	 * 192.168.0.0/16：192.168.0.0～192.168.255.255
	 * 
	 * @param addr
	 * @return
	 */
	private static boolean isInternalIp(byte[] addr) {
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
		case SECTION_1:
			return true;
		case SECTION_2:
			if (b1 >= SECTION_3 && b1 <= SECTION_4) {
				return true;
			}
		case SECTION_5:
			switch (b1) {
			case SECTION_6:
				return true;
			}
		default:
			return false;
		}
	}

	private final static int INADDR4SZ = 4;

	public static byte[] textToNumericFormatV4(String src) {
		if (src.length() == 0) {
			return null;
		}
		byte[] res = new byte[INADDR4SZ];
		String[] s = src.split("\\.", -1);
		long val;
		try {
			switch (s.length) {
			case 1:
				val = Long.parseLong(s[0]);
				if (val < 0 || val > 0xffffffffL)
					return null;
				res[0] = (byte) ((val >> 24) & 0xff);
				res[1] = (byte) (((val & 0xffffff) >> 16) & 0xff);
				res[2] = (byte) (((val & 0xffff) >> 8) & 0xff);
				res[3] = (byte) (val & 0xff);
				break;
			case 2:
				val = Integer.parseInt(s[0]);
				if (val < 0 || val > 0xff)
					return null;
				res[0] = (byte) (val & 0xff);
				val = Integer.parseInt(s[1]);
				if (val < 0 || val > 0xffffff)
					return null;
				res[1] = (byte) ((val >> 16) & 0xff);
				res[2] = (byte) (((val & 0xffff) >> 8) & 0xff);
				res[3] = (byte) (val & 0xff);
				break;
			case 3:
				for (int i = 0; i < 2; i++) {
					val = Integer.parseInt(s[i]);
					if (val < 0 || val > 0xff)
						return null;
					res[i] = (byte) (val & 0xff);
				}
				val = Integer.parseInt(s[2]);
				if (val < 0 || val > 0xffff)
					return null;
				res[2] = (byte) ((val >> 8) & 0xff);
				res[3] = (byte) (val & 0xff);
				break;
			case 4:
				for (int i = 0; i < 4; i++) {
					val = Integer.parseInt(s[i]);
					if (val < 0 || val > 0xff)
						return null;
					res[i] = (byte) (val & 0xff);
				}
				break;
			default:
				return null;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	public static void main(String[] args) {
		try {
			System.out.println("本机IPv4为：" + InetAddress.getLocalHost().getHostAddress());
			EfutureLookup look = new EfutureLookup();
			System.out.println(look.lookup(null, "appId"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
