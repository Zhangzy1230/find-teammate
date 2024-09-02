package com.zzy.globalFilter;


import com.zzy.result.Code;
import com.zzy.result.Result;
import com.zzy.utils.JwtGenerator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;  
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Component  
public class CustomHeaderFilter implements GlobalFilter, Ordered {
//    @Resource
//    private UserFeignController userFeignController;

    // 定义要排除的路径列表  登录和注册
    private static final HashSet<String> EXCLUDED_PATHS = new HashSet<>();
    @Value("${springdoc.api-docs.path}")
    private String DOC_PATH;
    static {
        EXCLUDED_PATHS.add("/user/login");
//        EXCLUDED_PATHS.add("/user/user/login");
        EXCLUDED_PATHS.add("/user/register");
//        EXCLUDED_PATHS.add("/user/user/register");
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        String ipAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        System.out.println("ip: "+ipAddress+" path: "+path+" doc-path: "+DOC_PATH);
        //如果是调用文档
        if(path.contains(DOC_PATH)){
            return chain.filter(exchange);
        }
        // 检查路径是否在排除列表中
        if (!EXCLUDED_PATHS.contains(path)) {
            // 如果路径不在排除列表中，则执行过滤逻辑
            try {
                String jwt = exchange.getRequest().getHeaders().getFirst("jwt");
                String username = JwtGenerator.parseUsername(jwt);
                // 获取原始的ServerHttpRequest
                ServerHttpRequest request = exchange.getRequest();
                // 创建一个ServerHttpRequestDecorator来装饰原始请求，并添加新的头信息
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
                    @Override
                    public HttpHeaders getHeaders() {
                        // 获取原始的HttpHeaders
                        HttpHeaders headers = getDelegate().getHeaders();
                        // 复制HttpHeaders，以便我们可以修改它
                        HttpHeaders mutatedHeaders = new HttpHeaders();
                        mutatedHeaders.addAll(headers);
                        // 添加新的头信息
                        mutatedHeaders.add("username", username);
                        return mutatedHeaders;
                    }
                };
                // 替换原始的ServerHttpRequest为修改后的请求
                ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                return chain.filter(mutatedExchange);
            } catch (Exception e) {
                // 阻断请求，并直接向客户端发送响应
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN); // 示例：使用403 Forbidden状态码
                // 注意：这里只是设置了状态码，没有设置响应体。如果你需要设置响应体，可以进一步操作response对象
                // 由于我们想要“不放行”，即不继续执行过滤器链，所以我们直接返回一个空的Mono<Void>
                // 实际上，这里返回的是一个已完成的Mono，表示操作已完成，不需要继续执行后续步骤
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