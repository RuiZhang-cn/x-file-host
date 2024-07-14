package top.rui10038.xfilehost.demos.init;

import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.rui10038.xfilehost.demos.config.HttpFileStorageConfig;
import top.rui10038.xfilehost.demos.config.properties.XFileHostProperties;
import top.rui10038.xfilehost.demos.storage.HttpFileStorage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InitCommandLineRunner implements CommandLineRunner {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private XFileHostProperties fileHostProperties;

    @Override
    public void run(String... args) throws Exception {
        CopyOnWriteArrayList<FileStorage> fileStorageList = fileStorageService.getFileStorageList();
        List<HttpFileStorageConfig> httpFileStorage = fileHostProperties.getHttpFileStorage();
        httpFileStorage.forEach(httpFileStorageConfig -> {
            HttpFileStorage storage = new HttpFileStorage(
                    httpFileStorageConfig.getPlatform(),
                    httpFileStorageConfig.getUrl(),
                    httpFileStorageConfig.getMethod(),
                    httpFileStorageConfig.getHeaderMap(),
                    httpFileStorageConfig.getArgMap(),
                    httpFileStorageConfig.getFilename(),
                    httpFileStorageConfig.getTimeout(),
                    httpFileStorageConfig.getResultJsonUrlPath());
            fileStorageList.add(storage);
        });
    }
}
