package xyz.game.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.game.util.RedisUtil;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    final private RedisUtil redisUtil;

    public TokenInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if( 1+1 == 2){
            return true;
        }
        // 检查请求方法是否为 POST、PUT、DELETE
        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
            // 从请求头获取 token
            String token = request.getHeader("Authorization");
            if (token == null || !isTokenValid(token)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理之后进行调用，但是在视图被渲染之前
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 整个请求完成之后被调用，也就是在 DispatcherServlet 渲染了对应的视图之后执行
    }

    private boolean isTokenValid(String token) {
        // 截取 Bearer 之后的部分作为 token
        token = token.substring(7);
        String s = redisUtil.get("token."+token);
        if (s == null) {
            return false;
        }
        return "1".equals(s.substring(0,1));
    }
}