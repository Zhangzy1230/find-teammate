package com.zzy.globalFilter;




import com.zzy.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;  
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Component  
public class CustomHeaderFilter implements GlobalFilter, Ordered {
    // 定义要排除的路径列表  登录和注册
    private static final HashSet<String> EXCLUDED_PATHS = new HashSet<>();
    @Value("${springdoc.api-docs.path}")
    private String DOC_PATH;
    static {
        EXCLUDED_PATHS.add("/user/login");
        EXCLUDED_PATHS.add("/user/register");
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        String ipAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        System.out.println("ip: "+ipAddress+" path: "+path);
        //如果是调用文档
        if(path.contains(DOC_PATH)){
            return chain.filter(exchange);
        }
        if(path.contains("test")){
            return chain.filter(exchange);
        }
        // 检查路径是否在排除列表中
        if (!EXCLUDED_PATHS.contains(path)) {
            // 如果路径不在排除列表中，则执行过滤逻辑
            try {
                String jwt = exchange.getRequest().getHeaders().getFirst("jwt");
                String username = JwtGenerator.parseUsername(jwt);
                System.out.println("username: " + username);
            } catch (Exception e) {
                // 阻断请求，并直接向客户端发送响应
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN); // 示例：使用403 Forbidden状态码
                System.out.println("抛弃了一个请求");
                return Mono.empty();
            }
        }
        // 继续过滤器链  
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 设置过滤器的执行顺序  
        return -1; // 越小越先执行  
    }
}