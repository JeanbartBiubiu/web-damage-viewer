package xyz.game.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.util.MapUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 自定义插件拦截器，实现字段的属性注入
 */

// 注入范围跟MybatisPlusInterceptor类的类似,去掉了原生sql和StatementHandler管理的sql的拦截
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
        }
)
public class MyBatisPlugin implements Interceptor {

    /**
     * 此函数方法是给一个map添加其他自定义参数，在这里用来增加公用的入参
     */
    private final Consumer<Map<String, Object>> consumer;

    public MyBatisPlugin(Consumer<Map<String, Object>> consumer) {
        this.consumer = consumer;
    }

    /**
     * Executor的第二个args参数转换成Map后，这个map实际上就是参数列表，往里面put你需要的数据即可
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        /*
          namespace-拦截到的mapper接口方法
          className-接口方法所在的mapper类
          methodName-方法名称
         */
        MappedStatement mappedStatement = (MappedStatement) args[0];
        String namespace = mappedStatement.getId();
        int separatorIndex = namespace.lastIndexOf(".");
        String className = namespace.substring(0, separatorIndex);
        String methodName = namespace.substring(separatorIndex + 1);

        // 原有参数列表
        Object paramMap = args[1];
        if (paramMap != null) {
            if (!(paramMap instanceof Map)) {
                System.out.println("偷偷转换了");
                try {
                    paramMap = JSON.parseObject(JSON.toJSONString(paramMap), HashMap.class);
                }catch (Exception e){
                    // 转换失败，新取自定义map，将其直接塞到自定义的map里
                    MyParamMap<Object> myParamMap = new MyParamMap<>();
                    if (args[1] instanceof Integer){
                        myParamMap.put(defaultKey,args[1]);
                    } else {
                        myParamMap.put(defaultKey,args[1].toString());
                    }
                    paramMap = myParamMap;
                }
            }
        } else {
            paramMap = new MapperMethod.ParamMap<>();
        }
        args[1] = paramMap;

        Class<?> tClass = Class.forName(className);

        Method[] methods = tClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                consumer.accept((Map<String, Object>) paramMap);
                break;
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    private static final String defaultKey = "_default_v";

    // mybatis mapper入参只有一个基本类型时，#{any}都会取这个基本类型的值替换，这个class是为了将其转换为map用的
    public static class MyParamMap<V> extends HashMap<String, V> {
        private static final long serialVersionUID = -12345324342L;

        public MyParamMap() {
        }

        public V get(Object key) {
            if (!super.containsKey(key)) {
                return super.get(defaultKey);
            } else {
                return super.get(key);
            }
        }
    }


}