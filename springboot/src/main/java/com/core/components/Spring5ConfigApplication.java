package com.core.components;

import org.junit.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by chenxun.
 * Date: 2018/5/14 上午12:44
 * Description:
 */
@SpringBootApplication
//@ComponentScan
//@Configuration
public class Spring5ConfigApplication implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(Spring5ConfigApplication.applicationContext == null) {
            Spring5ConfigApplication.applicationContext = applicationContext;
        }
    }

    public static void main(String[] args) {
        //ApplicationContext ac=new AnnotationConfigApplicationContext(Spring5ConfigApplication.class);
        SpringApplication.run(Spring5ConfigApplication.class, args);

        Assert.assertNotNull("the BarService should not be null.",applicationContext.getBean(BarService.class));
//        Assert.assertNotNull("the FooService should not be null.",applicationContext.getBean(FooService.class));

    }

}
//@Component
class BarService{

}

//@Component
class FooService{
    private  final  BarService barService;

    public FooService(BarService barService){
        this.barService=barService;
    }
}

//通过实现BeanDefinitionRegistryPostProcessor来注册bean
@Component
class  MyBRPP implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        //bar service
        beanDefinitionRegistry
                .registerBeanDefinition("barService1", BeanDefinitionBuilder.genericBeanDefinition(BarService.class)
                        .getBeanDefinition());

        /**
         * 高版本
         * foo service
         */
/*        beanDefinitionRegistry.registerBeanDefinition("fooService", genericBeanDefinition(FooService.class, () -> {
            BeanFactory beanFactory= BeanFactory.class.cast(beanDefinitionRegistry);
            return new FooService(beanFactory.getBean(BarService.class));
        }).getBeanDefinition());*/

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}

/**
 * 高版本
 */

 class ProgrammaticBeanDefinitionInitializr implements ApplicationContextInitializer<GenericApplicationContext> {

    @Override
    public void initialize(GenericApplicationContext genericApplicationContext) {
        //System.err.println("Hello ,initializr");
        genericApplicationContext.registerBean(BarService.class);
        genericApplicationContext.registerBean(
                FooService.class,
                ()-> new FooService(genericApplicationContext.getBean(BarService.class))
        );
    }
}
