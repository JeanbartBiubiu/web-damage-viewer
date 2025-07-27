package xyz.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.game.interceptor.SchemaInterceptor;
import xyz.game.util.RedisUtil;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    private final RedisUtil redisUtil;

    public MyMvcConfig(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SchemaInterceptor())
                .addPathPatterns("/**") //拦截所有请求，包括静态资源文件
                .excludePathPatterns("/", "/login", "/index.html", "/user/login", "/css/**", "/images/**", "/js/**", "/fonts/**"); //放行登录页，登陆操作，静态资源
        registry.addInterceptor(new TokenInterceptor(redisUtil))
               .addPathPatterns("/**") //拦截所有请求，包括静态资源文件
               .excludePathPatterns("/", "/login", "/index.html", "/login/huawei/token", "/css/**", "/images/**", "/js/**", "/fonts/**"); //放行登录页，登陆操作，静态资源
    }   
}