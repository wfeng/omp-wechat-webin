package com.efuture.wechat.common;


import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.controller.ProductReflect;
import com.efuture.wechat.entity.ServiceSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;

public class ServiceLogs {

	public static void logResponse(String logmsg, String rtncode) {
		ServiceSession sessionobj = getSessionobj();
		sessionobj.setReturncode(rtncode);
		debuglog(sessionobj, LOGTYPE.RESPONSE, logmsg, sessionobj.getStarttime());
	}

	static Logger logger = LogManager.getLogger(ServiceLogs.class);


	public static ServiceSession getSessionobj() {
		ServiceSession sessionobj = ProductReflect.getLocale().get();
		if (sessionobj == null || StringUtils.isEmpty(sessionobj.getRootkey())) {
			String tname = Thread.currentThread().getName();
			String rootkey = String.valueOf((new Date()).getTime());
			if (tname.startsWith("rootkey")) {
				rootkey = tname.substring(7);
			}
			sessionobj = new ServiceSession();
			sessionobj.setUser_name("NIN");
			sessionobj.setRootkey(rootkey);
			sessionobj.setLogkey(String.valueOf((new Date()).getTime()));
			sessionobj.setParentkey(rootkey);
			ProductReflect.getLocale().set(sessionobj);
		}
		return sessionobj;
	}

	public static void logRequest(String logmsg) {
		ServiceSession sessionobj = getSessionobj();
		sessionobj.setStarttime(System.currentTimeMillis());
		debuglog(sessionobj, LOGTYPE.REQUEST, logmsg, 0);
	}

	public static void logCallStart(String url, String method, String request) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("call_method", method);
		logmsg.put("call_url", url);
		logmsg.put("call_request", request);
		debuglog(sessionobj, LOGTYPE.CALL_REQUEST, logmsg.toString(), 0);
	}

	public static void logCallError(String url, String method, String errcode, String errmsg, long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("call_method", method);
		logmsg.put("call_url", url);
		logmsg.put("call_returncode", errcode);
		logmsg.put("call_response", errmsg);
		debuglog(sessionobj, LOGTYPE.CALL_RESPONSE, logmsg.toString(), starttime);
	}

	public static void logCallSuccess(String url, String method, long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("call_method", method);
		logmsg.put("call_url", url);
		logmsg.put("call_returncode", "0");
		debuglog(sessionobj, LOGTYPE.CALL_RESPONSE, logmsg.toString(), starttime);
	}

	private static String getDefaultFormatstr(ServiceSession sessionobj, String logtype, final Exception e, String logmsg,
											  long starttime) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(sessionobj) && !StringUtils.isEmpty(sessionobj.getRootkey())) {
			sb.append("rootkey: ");
			sb.append(sessionobj.getRootkey().concat(", "));
		}
		sb.append("orderkey: ");
		sb.append(sessionobj.getCurOrderKey().concat(", "));
		if (logtype.equalsIgnoreCase(LOGTYPE.REQUEST)) {
			sb.append("[").append(sessionobj.getRemoteaddr()).append(" - ").append(sessionobj.getLogkey()).append("]: ");
			sb.append(sessionobj.getMethod()).append(" , ");
			sb.append("REQUEST  : ");
			sb.append(logmsg);
			sb.append(" , entid : ");
			sb.append(sessionobj.getEnt_id());
		} else if (logtype.equalsIgnoreCase(LOGTYPE.RESPONSE)) {
			sb.append("[").append(sessionobj.getRemoteaddr()).append(" - ").append(sessionobj.getLogkey()).append("]: ");
			sb.append(sessionobj.getMethod()).append(" , ");
			sb.append("ELAPSED  : ").append(System.currentTimeMillis() - starttime).append(" ms , ");
			sb.append("RESPONSE : ");
			sb.append(logmsg);
		} else if (logtype.equalsIgnoreCase(LOGTYPE.INFO)) {
			if (sessionobj.getRemoteaddr() != null && sessionobj.getLogkey() != null) {
				sb.append("[").append(sessionobj.getRemoteaddr()).append(" - ").append(sessionobj.getLogkey()).append("]: ");
			}
			if (starttime > 0L) {
				sb.append("ELAPSED  : ").append(System.currentTimeMillis() - starttime).append(" ms , ");
			}
			sb.append(logmsg);
		} else {
			sb.append(logtype).append("  : ");
			if (starttime > 0L) {
				sb.append("ELAPSED  : ").append(System.currentTimeMillis() - starttime).append(" ms , ");
			}
			sb.append(logmsg);
		}
		if (e != null) {
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (int i = 0; i < stackTrace.length; i++) {
				// System.out.println("key = " + stackTrace[i]);
				if (i == 0) {
					sb.append("exception : [").append(stackTrace[i]);
				} else {
					sb.append(stackTrace[i]);
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}

	public static void logConsumeMqStart(String topic, String key, String othermsg) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		debuglog(sessionobj, LOGTYPE.MQCONSUMER, logmsg.toString(), 0);
	}

	public static void logConsumeMqError(String topic, String key, String errcode, String othermsg,
			long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		logmsg.put("mq_returncode", errcode);
		debuglog(sessionobj, LOGTYPE.MQCONSUMER, logmsg.toString(), starttime);
	}

	public static void logConsumeMqSuccess(String topic, String key,  String othermsg, long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		logmsg.put("mq_returncode", "0");
		debuglog(sessionobj, LOGTYPE.MQCONSUMER, logmsg.toString(), starttime);
	}

	public static void logSendSMSStart(String topic, String key, String othermsg) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		debuglog(sessionobj, LOGTYPE.SMSSEND, logmsg.toString(), 0);
	}

	public static void logSendSMSError(String topic, String key, String errcode, String sendresult, String othermsg,
									   long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		logmsg.put("sendresult", sendresult);
		logmsg.put("sms_returncode", errcode);
		debuglog(sessionobj, LOGTYPE.SMSSEND, logmsg.toString(), starttime);
	}

	public static void logSendSMSSuccess(String topic, String key, String sendresult, String othermsg, long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		logmsg.put("sendresult", sendresult);
		logmsg.put("mq_returncode", "0");
		debuglog(sessionobj, LOGTYPE.SMSSEND, logmsg.toString(), starttime);
	}

	public static void logSendMqStart(String topic, String key, String othermsg) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		debuglog(sessionobj, LOGTYPE.MQSEND, logmsg.toString(), 0);
	}

	public static void logSendMqError(String topic, String key, String errcode, String sendresult, String othermsg,
			long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		logmsg.put("sendresult", sendresult);
		logmsg.put("mq_returncode", errcode);
		debuglog(sessionobj, LOGTYPE.MQSEND, logmsg.toString(), starttime);
	}

	public static void logSendMqSuccess(String topic, String key, String sendresult, String othermsg, long starttime) {
		ServiceSession sessionobj = getSessionobj();
		JSONObject logmsg = new JSONObject();
		logmsg.put("topic", topic);
		logmsg.put("keyvalue", key);
		logmsg.put("othermsg", othermsg);
		logmsg.put("sendresult", sendresult);
		logmsg.put("mq_returncode", "0");
		debuglog(sessionobj, LOGTYPE.MQSEND, logmsg.toString(), starttime);
	}

	public static String format(String msg, Object... args) {
		if (args == null || args.length <= 0)
			return msg;

		// format String
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof String)
				continue;
			args[i] = StringUtils.isEmpty(args[i]) ? "" : args[i].toString();
		}

		return MessageFormat.format(msg, args);
	}


	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	private static String getJsonFormatstr(final ServiceSession sessionobj, final String logtype, final Exception e, final String logmsg,
			final long starttime) {
		JSONObject rtn = (JSONObject) JSONObject.toJSON(sessionobj);
		rtn.put("logtype", logtype);
		rtn.put("logmsg", logmsg);
		if (e != null) {
			rtn.put("exception", getStackTrace(e));
			if (null == logmsg) {
				rtn.put("logmsg", e.getMessage());
			} else {
				rtn.put("logmsg", logmsg + " errMsg:" + e.getMessage());
			}
		}
		if (starttime != 0) {
			rtn.put("elapsed", System.currentTimeMillis() - starttime);
		}
		return rtn.toString();
	}

	public static void debuglog(ServiceSession sessionobj, String logtype, String logmsg, long starttime) {
		String msg = logmsg;
		if (ServiceVersion.getInstance().getLogFormat().equalsIgnoreCase("json")) {
			msg = getJsonFormatstr(sessionobj, logtype, null, logmsg, starttime);
		} else {
			msg = getDefaultFormatstr(sessionobj, logtype, null, logmsg, starttime);
		}
		logger.info(msg);
	}
	public static void debuglog(String logtype, String logmsg, long starttime) {
        ServiceSession sessionobj = getSessionobj();
        String msg = logmsg;
        if (ServiceVersion.getInstance().getLogFormat().equalsIgnoreCase("json")) {
            msg = getJsonFormatstr(sessionobj, logtype, null, logmsg, starttime);
        } else {
            msg = getDefaultFormatstr(sessionobj, logtype, null, logmsg, starttime);
        }
        logger.info(msg);
    }

	public static void errLog(String logtype, Exception e, String logmsg, Object... arguments) {

		String msg = formatMsg(logtype, e, logmsg, 0, arguments);
		logger.error(msg);
	}

	public static void truedebuglog(String logtype, String logmsg, long starttime, Object... arguments) {
		String msg = formatMsg(logtype, null, logmsg, starttime, arguments);
		logger.debug(msg);
	}

	private static String formatMsg(String logtype, Exception e, String logmsg, long starttime, Object... arguments) {
		ServiceSession sessionobj = getSessionobj();
		String fmsg = format(logmsg, arguments);
		String msg;
		msg = getJsonFormatstr(sessionobj, logtype, e, fmsg, starttime);
		return msg;
	}

	public interface LOGTYPE {
		String INFO = "info";
		String RESPONSE = "response";
		String REQUEST = "request";
		//		String DBSTART = "db_start";
//		String DBEND = "db_end";
//		String DBSUCC = "db_succ";
//		String DBERROR = "db_error";
		String CALL_REQUEST = "call_request";
		String CALL_RESPONSE = "call_response";
		String MQSEND = "mq_send";
		String MQCONSUMER = "mq_consumer";
		String SMSSEND = "sms_send";
	}

}
