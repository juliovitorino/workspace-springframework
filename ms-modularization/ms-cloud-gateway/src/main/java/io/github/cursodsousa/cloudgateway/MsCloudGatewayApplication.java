package io.github.cursodsousa.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class MsCloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCloudGatewayApplication.class, args);
	}

	/**
	 * Registra as rotas dos microserviços para que o gateway saiba como enviar
	 * para o Eurika
	 *
	 * Lei de formacao da rota r
	 * Origem: Request Mapping do controller, ex: /cliente/**
	 * Destino: lb = string hardcode loadbalancer eureka
	 * spring.application.name = ex: msclientes (dentro do arquivo yml do serviço)
	 */

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route( r -> r.path("/clientes/**").uri("lb://msclientes") )
				.route( r -> r.path("/cartoes/**").uri("lb://mscartoes") )
				.route( r -> r.path("/avaliacoes-credito/**").uri("lb://msavaliadorcredito") )
				.route( r -> r.path("/faturamento/**").uri("lb://msfaturamento") )
				.route( r -> r.path("/mensageiro/**").uri("lb://msmensageiro") )
				.build();
	}

}
