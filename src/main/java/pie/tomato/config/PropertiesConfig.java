package pie.tomato.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationPropertiesScan("pie.tomato.tomatomarket.infrastructure.config.properties")
@Configuration
public class PropertiesConfig {
}
