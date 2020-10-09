package com.efuture.wechat;

import com.efuture.wechat.common.SpringContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeChatApplication {

    public static void main(String[] args) {
        SpringContext.run(WeChatApplication.class, args);
    }

}
