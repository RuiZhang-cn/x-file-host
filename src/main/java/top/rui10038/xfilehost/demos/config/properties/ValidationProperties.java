package top.rui10038.xfilehost.demos.config.properties;

import cn.hutool.http.Method;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author by Rui
 * @Description
 * @Date 2024/7/14 上午12:24
 */
@Data
@ConfigurationProperties(
        prefix = "x-file-host.image.validation"
)
@Configuration
public class ValidationProperties {
    private boolean enable;
    private Method method = Method.HEAD;
}
