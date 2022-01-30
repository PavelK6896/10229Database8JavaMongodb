package app.web.pavelk.database8.config;

import app.web.pavelk.database8.schema.Four;
import app.web.pavelk.database8.schema.One;
import app.web.pavelk.database8.schema.Three;
import app.web.pavelk.database8.schema.Two;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MongoConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(One.class);
        config.exposeIdsFor(Two.class);
        config.exposeIdsFor(Three.class);
        config.exposeIdsFor(Four.class);
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }
}