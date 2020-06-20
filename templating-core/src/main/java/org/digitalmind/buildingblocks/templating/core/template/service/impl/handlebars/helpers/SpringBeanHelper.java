package org.digitalmind.buildingblocks.templating.core.template.service.impl.handlebars.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;

public class SpringBeanHelper implements Helper<String>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object apply(String value, Options options) throws IOException {
        return this.applicationContext.getBean(value);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

