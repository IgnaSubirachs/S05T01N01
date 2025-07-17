package cat.itacademy.s05.t01.n01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @GetMapping("/swagger-config")
    public Mono<String> getSwaggerConfig() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/swagger-config.yaml");
        if (is == null) {
            return Mono.just("❌ not founded to JAR");
        }
        return Mono.just("✅ swagger-config.yaml founded to JAR");
    }

}
