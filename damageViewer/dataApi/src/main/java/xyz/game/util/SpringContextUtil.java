package xyz.game.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    // 传入bean名称，返回spring bean工厂的bean对象
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    // 传入bean类型，返回spring bean工厂的bean对象
    public static <T> T getBean(String beanName,Class<T> beanClass) {
        return applicationContext.getBean(beanName,beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
