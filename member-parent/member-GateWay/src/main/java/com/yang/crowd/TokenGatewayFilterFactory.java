package com.yang.crowd;

import com.yang.crowd.constant.AccessPassResources;
import com.yang.crowd.constant.CrowdConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class TokenGatewayFilterFactory  implements GlobalFilter, Ordered {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request=exchange.getRequest();
        ServerHttpResponse response=exchange.getResponse();
        String servletPath=request.getPath().value();
        if(AccessPassResources.PASS_RES_SET.contains(servletPath)){
            log.info("可通过路径");
            return chain.filter(exchange);
        }else{
            if (AccessPassResources.judgeCurrentServletPathWetherStaticResource(servletPath)){
                log.info("静态资源");
                return chain.filter(exchange);
            }else {
                String member=redisTemplate.opsForValue().get(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
                log.info(member);
                if (member==null){
                    log.info("未登录");
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().set("Location","/auth/member/to/login/page");
                    return exchange.getResponse().setComplete();
                }
                return chain.filter(exchange);
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
