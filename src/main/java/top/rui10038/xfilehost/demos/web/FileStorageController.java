package top.rui10038.xfilehost.demos.web;

import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fileStorage")
@Slf4j
public class FileStorageController {
    @Autowired
    private FileStorageService fileStorageService;//注入实列

    @PostMapping()
    public String addStorage() {
        //增加
        return "upload";
    }

}
