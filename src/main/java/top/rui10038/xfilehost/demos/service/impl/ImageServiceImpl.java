package top.rui10038.xfilehost.demos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.rui10038.xfilehost.demos.entity.Image;
import top.rui10038.xfilehost.demos.mapper.ImageMapper;
import top.rui10038.xfilehost.demos.service.ImageService;

/**
 * @author by Rui
 * @Description
 * @Date 2024/7/13 下午5:58
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
}
