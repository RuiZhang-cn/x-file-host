package top.rui10038.xfilehost;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFileStorage
public class XFileHostApplication {

    public static void main(String[] args) {
        SpringApplication.run(XFileHostApplication.class, args);
    }

}
