package com.efuture.wechat;

import com.efuture.wechat.common.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class WeChatApplication {

    public static void main(String[] args) {
        log.info("启动服务");
        SpringContext.run(WeChatApplication.class, args);
    }

}
