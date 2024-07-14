package top.rui10038.xfilehost;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.lang.model.element.VariableElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class XFileHostApplicationTests {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    void contextLoads() throws FileNotFoundException {
        File file = new File("D:\\xiangmu\\x-file-host\\1.png");
        FileInputStream fileInputStream = new FileInputStream(file);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://smms.app/api/v2/upload";
        String token = "4DyxAZwXvbD2MidiYp5pC1D52dHalCbt";
        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream){
            @Override
            public String getFilename() {
                return "test";
            }
            @Override
            public long contentLength() {
                return file.length();
            }
        };
        params.add("file", inputStreamResource);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", token);
        HttpEntity<MultiValueMap<String,Object>> requestEntity  = new HttpEntity<>(params, headers);
        String result = restTemplate.postForObject(url,requestEntity ,String.class);
        System.out.println(result);
    }

    @Test
    void hutoolHttp(){
        File file = new File("D:\\xiangmu\\x-file-host\\1.png");
        String suffix = FileUtil.getSuffix(file);
        File tempFile = FileUtil.newFile("./temp/23b0ffb44350003d394e74b9e1788ea32198497fc6a52258a701c8ff71d67ea5.png");
        FileUtil.writeBytes(FileUtil.readBytes(file), tempFile);
        String body = HttpRequest.post("http://api.tucang.cc/api/v1/upload")
                .header("Authorization", "4DyxAZwXvbD2MidiYp5pC1D52dHalCbt")
                .form("file", tempFile)
                .form("token", "1720845003184358555229c5a4fa28ce6819e3d55f0d6")
                .execute().body();
        System.out.println(body);
    }

    // @Test
    // void unirest(){
    //     File file = new File("D:\\xiangmu\\x-file-host\\1.png");
    //
    //     HttpResponse<String> response1 = Unirest.post("https://smms.app/api/v2/upload")
    //             //这里*****是指上一步获取的token
    //             .header("Authorization", "4DyxAZwXvbD2MidiYp5pC1D52dHalCbt")
    //             .field("smfile", file)
    //             .asString();
    //     System.out.println(response1);
    // }

}
