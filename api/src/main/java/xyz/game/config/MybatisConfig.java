package xyz.game.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.game.util.ThreadLocalUtil;

import java.util.Map;
import java.util.function.Consumer;


/**
 * 拦截器配置
 *
 * @author xm.z
 */
@Configuration
public class MybatisConfig {

    @Bean
    public MyBatisPlugin myBatisPlugin() {
        Consumer<Map<String, Object>> consumer = map -> {
            // 基本信息
            map.putIfAbsent("whichGame", "default_table");
            map.putIfAbsent("currentSchema", "default_table");
        };
        return new MyBatisPlugin(consumer);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            if (tableName.equals("game_info")) {
                return "default_table." + tableName;
            }
            if (tableName.equals("email")) {
                return "email." + tableName;
            }
            if (tableName.equals("pv_uv")) {
                return "default_table." + tableName;
            }
            if (tableName.contains(".")) {
                return tableName;
            }
            // 邮箱和游戏管理表额外处理
            String schema = ThreadLocalUtil.getOrDefault("currentSchema","default_table");
            return schema+"."+tableName;
        });
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }

}