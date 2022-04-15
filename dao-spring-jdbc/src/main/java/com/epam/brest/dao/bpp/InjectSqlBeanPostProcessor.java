package com.epam.brest.dao.bpp;

import com.epam.brest.dao.annotation.InjectSql;
import com.epam.brest.dao.util.DaoUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import static com.epam.brest.dao.constant.DaoConstant.DOT_DELIMITER;
import static com.epam.brest.dao.constant.DaoConstant.POSTFIX_INJECTION_FIELD;

@Component
public class InjectSqlBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(InjectSql.class)) {
            String prefixPropKey = bean.getClass().getAnnotation(InjectSql.class).prefix();
            ReflectionUtils.doWithFields(bean.getClass(), field -> {
                if (field.getType() != String.class) {
                    return;
                }
                int postfixIdx = field.getName().length() - POSTFIX_INJECTION_FIELD.length();
                int lastIdx = field.getName().length();
                String fieldPrefix = field.getName().substring(0, postfixIdx);
                String fieldPostfix = field.getName().substring(postfixIdx, lastIdx);
                if (POSTFIX_INJECTION_FIELD.equals(fieldPostfix)) {
                    String postfixPropKey = DaoUtils.convertToDotCase(fieldPrefix);
                    String propKey = prefixPropKey.length() > 0 ? prefixPropKey + DOT_DELIMITER + postfixPropKey : postfixPropKey;
                    String sql = environment.getProperty(propKey);
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, bean, sql);
                }
            });
        }
        return bean;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
