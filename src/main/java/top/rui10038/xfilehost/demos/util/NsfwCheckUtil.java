package top.rui10038.xfilehost.demos.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import top.rui10038.xfilehost.demos.config.properties.NsfwProperties;

import java.io.File;
import java.util.Optional;

/**
 * @author by Rui
 * @Description
 * @Date 2024/8/24 下午5:07
 */
@Slf4j
public class NsfwCheckUtil {
    public static boolean isNsfw(NsfwProperties nsfwProperties, File file) {
        boolean enable = nsfwProperties.isEnable();
        if (!enable) {
            return false;
        }
        String url = nsfwProperties.getUrl();
        String nsfwRes = HttpRequest.post(url)
                .form(nsfwProperties.getFilename(), file)
                .timeout(nsfwProperties.getTimeout())
                .execute()
                .body();
        Optional<Boolean> verificationResults = Optional.ofNullable(nsfwRes)
                .map(JSONUtil::parse)
                .map(json -> json.getByPath(nsfwProperties.getResultJsonUrlPath()))
                .map(Object::toString)
                .map(Double::parseDouble)
                .map(res -> res > nsfwProperties.getThreshold());
        return verificationResults.orElseGet(() -> {
            if (nsfwProperties.getDefaultValue() == null) {
                log.error("nsfw检查失败,请求url为：{},返回值为:{}！", url, nsfwRes);
                throw new RuntimeException("nsfw检查失败！请查看日志！");
            }
            return nsfwProperties.getDefaultValue();
        });
    }
}
