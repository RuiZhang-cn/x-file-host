package top.rui10038.xfilehost.demos.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.hash.MessageDigestHashCalculator;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.rui10038.xfilehost.demos.config.properties.ValidationProperties;
import top.rui10038.xfilehost.demos.config.properties.XFileHostProperties;
import top.rui10038.xfilehost.demos.entity.Image;
import top.rui10038.xfilehost.demos.pojo.ApiResponse;
import top.rui10038.xfilehost.demos.pretreatment.FileUploadPretreatment;
import top.rui10038.xfilehost.demos.service.ImageService;
import top.rui10038.xfilehost.demos.storage.ImageUrlValidationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dromara.x.file.storage.core.constant.Constant.Hash.MessageDigest.SHA256;

@RestController
@Slf4j
public class FileDetailController {
    @Autowired
    private FileStorageService fileStorageService;// 注入实列
    @Autowired
    private XFileHostProperties xFileHostProperties;
    @Autowired
    private ImageService imageService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ApiResponse<String> upload(MultipartFile file, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        String xFileHostPropertiesToken = xFileHostProperties.getToken();
        if (StrUtil.isNotBlank(xFileHostPropertiesToken)) {
            String token = request.getHeader("x-file-host-token");
            if (!xFileHostPropertiesToken.equals(token)) {
                log.debug("token错误,请求token:{},配置token:{}", token, xFileHostPropertiesToken);
                return ApiResponse.error("token错误");
            }
        }
        String originalFilename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        log.info("客户端上传文件:{}", originalFilename);
        String suffix = FileUtil.getSuffix(originalFilename);

        byte[] bytes = IoUtil.readBytes(inputStream);
        File tempFile = FileUtil.createTempFile(suffix, true);
        FileUtil.writeBytes(bytes, tempFile);
        CopyOnWriteArrayList<FileStorage> fileStorageList = fileStorageService.getFileStorageList();
        List<FileStorage> randomFileStorageList = RandomUtil.randomEleList(fileStorageList, xFileHostProperties.getBackupCount());
        if (CollectionUtil.isEmpty(randomFileStorageList)) {
            log.error("没有可用的fileStorage");
            return ApiResponse.error("没有可用的fileStorage");
        }
        if (log.isDebugEnabled()) {
            randomFileStorageList.stream().map(FileStorage::getPlatform).forEach(e -> log.debug("随机选择的fileStorage:{}", e));
        }
        MessageDigestHashCalculator messageDigestHashCalculator = new MessageDigestHashCalculator(MessageDigest.getInstance(SHA256));
        messageDigestHashCalculator.update(bytes);
        String fileHashValue = messageDigestHashCalculator.getValue();
        log.info("文件hash值:{}", fileHashValue);

        final String fileNewName = fileHashValue + "." + suffix;

        Stream<FileStorage> stream = randomFileStorageList.stream();
        if (xFileHostProperties.isConcurrent()) {
            stream = stream.parallel();
            log.debug("启用并发上传");
        }

        List<Image> imageList = stream.map(fileStorage -> {
            log.debug("开始上传文件到:{}", fileStorage.getPlatform());
            UploadPretreatment uploadPretreatment = fileStorageService.of(bytes).setOriginalFilename(originalFilename);
            FileUploadPretreatment fileUploadPretreatment = new FileUploadPretreatment(uploadPretreatment);
            fileUploadPretreatment.setFile(tempFile);
            uploadPretreatment = fileUploadPretreatment;
            uploadPretreatment.setPlatform(fileStorage.getPlatform());
            FileInfo upload = null;
            try {
                upload = uploadPretreatment.upload();
            } catch (Exception e) {
                log.error("上传文件到:{}失败", fileStorage.getPlatform(), e);
                return null;
            }
            if (upload != null) {
                String fileUrl = upload.getUrl();
                log.debug("上传文件到:{}成功,文件url:{}", fileStorage.getPlatform(), fileUrl);
            }
            uploadPretreatment = null;
            return upload;
        }).filter(Objects::nonNull).map(e -> {
            Image image = new Image();
            image.setFileName(fileNewName);
            image.setImageUrl(e.getUrl());
            image.setStatus(0);
            image.setPlatform(e.getPlatform());
            image.setCreatedAt(new Date());
            return image;
        }).collect(Collectors.toList());

        tempFile.delete();
        if (imageList.isEmpty()){
            return ApiResponse.error("上传失败");
        }
        imageService.saveBatch(imageList);
        return ApiResponse.success(fileNewName);
    }

    @GetMapping("/image/{filename}")
    public void image(@PathVariable String filename, HttpServletResponse response) throws IOException {
        List<Image> list = imageService.list(
                Wrappers.<Image>lambdaQuery()
                        .eq(Image::getFileName, filename)
                        .eq(Image::getStatus, 0)
        );
        log.info("用户请求图片:{},查找到{}个图片源", filename, list.size());
        Collections.shuffle(list);
        for (Image image : list) {
            String imageUrl = image.getImageUrl();
            ValidationProperties validation = xFileHostProperties.getImage().getValidation();
            if (validation.isEnable()) {
                boolean b = ImageUrlValidationUtil.validationImageUrl(imageUrl, validation.getMethod());
                if (b) {
                    log.debug("图片源:{} 可用,返回给用户", imageUrl);
                    response.sendRedirect(imageUrl);
                    return;
                }
                log.debug("图片源:{}不可用", imageUrl);
            }
        }
        response.sendRedirect(xFileHostProperties.getNotFindUrl());
    }

}
