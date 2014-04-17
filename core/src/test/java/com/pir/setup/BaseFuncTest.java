package com.pir.setup;

import com.pir.services.IBusinessService;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by pritesh on 12/19/13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-application-context-core.xml", "classpath:spring-cache.xml", "classpath:spring-datasource.xml"})
public abstract class BaseFuncTest extends TestCase implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    protected IBusinessService businessService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
