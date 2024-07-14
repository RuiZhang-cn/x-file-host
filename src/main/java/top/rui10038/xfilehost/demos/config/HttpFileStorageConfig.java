package top.rui10038.xfilehost.demos.config;

import cn.hutool.http.Method;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class HttpFileStorageConfig {
    private String platform;
    private String url;
    private Method method = Method.POST;
    private Map<String, String> headerMap = new HashMap<>(1);
    private Map<String, String> argMap = new HashMap<>(1);;
    private String filename = "file";
    private Integer timeout = 5;
    private String resultJsonUrlPath;
}