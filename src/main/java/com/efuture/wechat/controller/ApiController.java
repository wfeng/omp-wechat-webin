package com.efuture.wechat.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: ProductRest
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/16
 */
@RestController
public class ApiController extends ProductController {

    @RequestMapping("/rest")
    public String onRest(@RequestParam(value="method",required=false) String method,
                         @RequestParam(value="session",required=false) String session,
                         @RequestParam(value="ent_id",required=false) String ent_id,
                         @RequestParam(value="user_id",required=false) String user_id,
                         @RequestParam(value="user_name",required=false) String user_name,
                         @RequestParam(value="locale",required=false) String locale,
                         @RequestParam(value="rootkey",required=false) String rootkey,
                         @RequestBody String param) {
        return this.onRestService(method, session, ent_id, user_id, user_name, locale, rootkey, param);
    }
}
