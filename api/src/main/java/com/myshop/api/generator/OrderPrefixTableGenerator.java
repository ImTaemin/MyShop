package com.myshop.api.generator;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class OrderPrefixTableGenerator extends TableGenerator {

    private final String PREFIX_KEY = "prefix_key";
    private final int DEF_NUMBER_LENGTH = 4;
    private String prefixKey = "";

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        prefixKey = ConfigurationHelper.getString(PREFIX_KEY, params);
        super.configure(StandardBasicTypes.LONG, params, serviceRegistry);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Serializable v =  super.generate(session, obj);

        Long longValue = (Long)v;

        int valueLength = String.valueOf(longValue).length();
        valueLength = valueLength > DEF_NUMBER_LENGTH ? valueLength : DEF_NUMBER_LENGTH;
        valueLength = valueLength > 9 ? 9 : valueLength;

        return String.format(prefixKey + "%0" + valueLength + "d", v);
    }
}
