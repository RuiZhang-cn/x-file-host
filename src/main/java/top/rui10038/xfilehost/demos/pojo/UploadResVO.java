package top.rui10038.xfilehost.demos.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by Rui
 * @Description
 * @Date 2024/7/14 下午7:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResVO {
    private String url;
    private String filename;
}
