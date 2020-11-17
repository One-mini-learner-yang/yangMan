package crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CrowdMainClass3000 {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass3000.class,args);
    }
}
