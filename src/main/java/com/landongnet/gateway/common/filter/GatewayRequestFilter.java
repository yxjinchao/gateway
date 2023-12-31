package com.landongnet.gateway.common.filter;

import com.github.snake.rock.common.constants.CommonCons;
import com.landongnet.gateway.enhance.service.RouteEnhanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author snake
 * @description
 * @since 2023/8/19 00:40
 */
@Slf4j
@Component
@Order(0)
@RequiredArgsConstructor
public class GatewayRequestFilter implements GlobalFilter {

    private final RouteEnhanceService routeEnhanceService;

    @Value("${rock.gateway.enhance:false}")
    private Boolean routeEnhance;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeEnhance) {
            Mono<Void> blackListResult = routeEnhanceService.filterBlackList(exchange);
            if (blackListResult != null) {
                routeEnhanceService.saveBlockLogs(exchange);
                return blackListResult;
            }
            Mono<Void> rateLimitResult = routeEnhanceService.filterRateLimit(exchange);
            if (rateLimitResult != null) {
                routeEnhanceService.saveRateLimitLogs(exchange);
                return rateLimitResult;
            }
            routeEnhanceService.saveRequestLogs(exchange);
        }

        byte[] token = Base64Utils.encode((CommonCons.GATEWAY_TOKEN_VALUE).getBytes());
        String[] headerValues = {new String(token)};
        ServerHttpRequest build = exchange.getRequest().mutate().header(CommonCons.GATEWAY_TOKEN_HEADER, headerValues).build();
        ServerWebExchange newExchange = exchange.mutate().request(build).build();
        return chain.filter(newExchange);
    }
}
