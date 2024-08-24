package top.rui10038.xfilehost.demos.config.properties;

import lombok.Data;

/**
 * @author by Rui
 * @Description
 * @Date 2024/7/18 下午10:41
 */
@Data
public class NsfwProperties {
    private boolean enable = false;
    private String url = "";
    private String resultJsonUrlPath = "";
    private String filename = "";
    private double threshold = 0.5;
    private int timeout = 5000;
    private Boolean defaultValue = null;
}
