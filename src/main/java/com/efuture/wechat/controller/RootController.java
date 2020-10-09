package com.efuture.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.common.ServiceVersion;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;

/**
 * @title: RootController
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/17
 */
@RestController
public class RootController {
    @RequestMapping("/")
    public String onRoot(HttpServletRequest request, @Param("status") String status) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/") || uri.startsWith("\\"))
            uri = uri.substring(1);
        if (uri.endsWith("/") || uri.endsWith("\\"))
            uri = uri.substring(0, uri.length() - 1);

        if ("check".equalsIgnoreCase(status)) {
            return uri + " success\n";
        } else if ("info".equalsIgnoreCase(status)) {
            JSONObject json = new JSONObject();
            RuntimeMXBean run = ManagementFactory.getRuntimeMXBean();
            json.put("pid", run.getName().split("@")[0]);
            json.put("server", run.getName().split("@")[1]);
            json.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(run.getStartTime()));
            return json.toJSONString().concat("\n");
        } else if ("statis".equalsIgnoreCase(status)) {
            try {
                return ServiceVersion.getInstance().printElapsed();
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if ("get".equalsIgnoreCase(status)) {
            try {
                return ServiceVersion.getInstance().getElapsed().toString();
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if ("clear".equalsIgnoreCase(status)) {
            try {
                ServiceVersion.getInstance().cleanElapsed();
                return "ok";
            } catch (Exception e) {
                return e.getMessage();
            }
        } else {
            ServiceVersion sv = ServiceVersion.getInstance();

            if ("openlog".equalsIgnoreCase(status))
                sv.setLogstatus(true);
            if ("closelog".equalsIgnoreCase(status))
                sv.setLogstatus(false);

            return uri + " version " + sv.getVer() + " (LogStatus: " + (sv.isLogstatus() ? "Y" : "N") + ")\n";
        }
    }
}
