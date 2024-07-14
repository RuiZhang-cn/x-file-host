package top.rui10038.xfilehost.demos.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author by Rui
 * @Description
 * @Date 2024/7/13 下午5:52
 */
@TableName("images")
@Data
public class Image {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String fileName;
    private String imageUrl;
    private Integer status;
    private String platform;
    private Date createdAt;
}