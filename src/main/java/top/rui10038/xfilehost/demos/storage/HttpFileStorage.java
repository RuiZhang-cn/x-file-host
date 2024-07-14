package top.rui10038.xfilehost.demos.storage;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.InputStreamPlus;
import org.dromara.x.file.storage.core.UploadPretreatment;
import org.dromara.x.file.storage.core.platform.FileStorage;
import top.rui10038.xfilehost.demos.pretreatment.FileUploadPretreatment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class HttpFileStorage implements FileStorage {
    private String platform;
    private String url;
    private Method method;
    private Map<String, String> headerMap;
    private Map<String, String> argMap;
    private String filename;
    private Integer timeout;
    private String resultJsonUrlPath;


    @SneakyThrows
    @Override
    public boolean save(FileInfo fileInfo, UploadPretreatment pre) {
        HttpRequest httpRequest = HttpRequest.of(url);
        httpRequest.setMethod(method);
        headerMap.forEach(httpRequest::header);
        if (pre instanceof FileUploadPretreatment) {
            FileUploadPretreatment fileUploadPretreatment = (FileUploadPretreatment) pre;
            File file = fileUploadPretreatment.getFile();
            httpRequest.form(filename, file);
        } else {
            try (InputStreamPlus in = pre.getInputStreamPlus(false)) {
                httpRequest.form(filename, new InputStreamResource(in));
            }
        }
        argMap.forEach(httpRequest::form);
        httpRequest.timeout(timeout * 1000);
        HttpResponse httpResponse = httpRequest.execute();
        String body = httpResponse.body();
        log.debug("body:{}", body);
        if (JSONUtil.isTypeJSON(body)) {
            JSON json = JSONUtil.parse(body);
            String byPath = json.getByPath(resultJsonUrlPath).toString();
            if (byPath != null) {
                fileInfo.setUrl(byPath);
                return true;
            }
        }
        log.error("上传失败,返回值为:{}", body);
        return false;
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return false;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return false;
    }

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {

    }

    @Override
    public void downloadTh(FileInfo fileInfo, Consumer<InputStream> consumer) {

    }
}
