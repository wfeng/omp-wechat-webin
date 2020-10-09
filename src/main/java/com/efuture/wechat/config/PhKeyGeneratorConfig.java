package com.efuture.wechat.config;

import com.efuture.wechat.utils.UniqueID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @title: PhKeyGeneratorConfig
 * @description: PhKey主键生成
 * @author: wangf
 * @date: 2020/07/13
 */

public class PhKeyGeneratorConfig implements IdentifierGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PhKeyGeneratorConfig.class);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
//        UniqueID uniqueID = new UniqueID();
//        Long id = uniqueID.getId();
        Long id = UniqueID.getInstance().getId();
        logger.info("id -> [{}]", id);
        return id;
    }
}
