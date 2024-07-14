package top.rui10038.xfilehost.demos.storage;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;

/**
 * @author by Rui
 * @Description
 * @Date 2024/7/14 上午12:04
 */
public class ImageUrlValidationUtil {
    public static boolean validationImageUrl(String imageUrl, Method method) {
        switch (method) {
            case GET:
                return validationImageUrlByGet(imageUrl);
            case OPTIONS:
                return validationImageUrlByOptions(imageUrl);
            case HEAD:
                return validationImageUrlByHead(imageUrl);
            default:
                throw new IllegalArgumentException("不支持的请求方法");
        }

    }

    public static boolean validationImageUrlByGet(String imageUrl) {
        HttpResponse execute = HttpRequest.get(imageUrl)
                .header("Range", "bytes=0-1")
                .execute();
        return execute.isOk();
    }

    public static boolean validationImageUrlByOptions(String imageUrl) {
        HttpResponse execute = HttpRequest.options(imageUrl)
                .execute();
        return execute.isOk();
    }

    public static boolean validationImageUrlByHead(String imageUrl) {
        HttpResponse execute = HttpRequest.head(imageUrl)
                .execute();
        return execute.isOk();
    }
}
