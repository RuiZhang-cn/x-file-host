package top.rui10038.xfilehost.demos.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.rui10038.xfilehost.demos.config.HttpFileStorageConfig;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "x-file-host")
public class XFileHostProperties {
    private String token = null;
    private List<HttpFileStorageConfig> httpFileStorage;
    private int backupCount;
    private boolean concurrent;
    private String notFindUrl = "https://github.com/RuiZhang-cn/x-file-host";
    private ImageProperties image = new ImageProperties();
    private String accessDomain;
    private NsfwProperties nsfw = new NsfwProperties();
}