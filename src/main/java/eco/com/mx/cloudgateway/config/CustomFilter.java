package eco.com.mx.cloudgateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.CustomConfigEco> {

    public CustomFilter() {
        super(CustomConfigEco.class);
    }

    @Override
    public String name() {
        return "EcoFilter";
    }

    @Override
    public GatewayFilter apply(CustomConfigEco config) {
        return ((exchange, chain) -> {
            String callerName = "Cloud_Gateway";
            exchange.getRequest().mutate().header("callerName", config.headerValue);
            //Este chain trabaja con programación reactiva
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //post filter
                exchange.getResponse().getCookies().add("response", ResponseCookie.from(config.headerKey, config.headerKey).build());
                log.info("HeaderKey" + config.headerKey);
                log.info("HeaderValue" + config.headerValue);
                log.info("[Global filter] - [filter] : pre filter");
            }));
        });
    }

    //debe ser component para que se inyecte en el contexto de spring.
    @Component
    public static class CustomConfigEco {
        private String headerKey;
        private String headerValue;
    }

}
