package com.duoc.ms_recetas.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
    @Bean
    public OpenAPI configurarOpenApi() {
        // Información de contacto
        Contact contacto = new Contact()
                .name("Raúl Ferrini")
                .name("Francisco Molina")
                .email("ra.ferrini@duocuc.cl")
                .email("franci.molinap@duocuc.cl")
                .url("https://www.duoc.cl");



        // Licencia del proyecto
        License licencia = new License()
                .name("MIT")
                .url("https://opensource.org/licenses/MIT");

        // Información principal de la API
        Info informacionApi = new Info()
                .title("Microservicio de Recetas - RedSaludPatagónica")
                .description("API para la emisión y administración de recetas médicas")
                .version("1.0")
                .termsOfService("http://duoc.cl")
                .contact(contacto)
                .license(licencia);

        // Documentación externa (GitHub)
        ExternalDocumentation github = new ExternalDocumentation()
                .description("Repositorio oficial del proyecto")
                .url("https://github.com/FcoINF/FullStack-EVA3---2026.git");

        // Configuración OpenAPI
        return new OpenAPI()
                .info(informacionApi)
                .externalDocs(github);
    }
}
