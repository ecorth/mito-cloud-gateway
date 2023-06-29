package eco.com.mx.cloudgateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilters implements GlobalFilter {

    //exchange - es como el request
    //chain -
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("[Global filter] - [filter] : pre filter");
        //Agrega un header a la petción del microservicio, usumos mutate
        //este es el prefilter
        String callerName = "Cloud_Gateway";
        exchange.getRequest().mutate().header("callerName", callerName);
        //Este chain trabaja con programación reactiva
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
          //post filter
            exchange.getResponse().getCookies().add("response", ResponseCookie.from("CALLER_NAME", callerName).build());
            log.info("[Global filter] - [filter] : pre filter");
        }));
    }
    //El cloud nos permite aplicar filtros a rutas en espécifico.

}
