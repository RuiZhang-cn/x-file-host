package top.rui10038.xfilehost.demos.pretreatment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author by Rui
 * @Description
 * @Date 2024/7/13 下午7:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FileUploadPretreatment extends org.dromara.x.file.storage.core.upload.UploadPretreatment {
    public FileUploadPretreatment(org.dromara.x.file.storage.core.UploadPretreatment pre) {
        setFileStorageService(pre.getFileStorageService());
        setPlatform(pre.getPlatform());
        setFileWrapper(pre.getFileWrapper());
        setThumbnailBytes(pre.getThumbnailBytes());
        setThumbnailSuffix(pre.getThumbnailSuffix());
        setIgnoreThumbnailException(pre.isIgnoreThumbnailException());
        setObjectId(pre.getObjectId());
        setObjectType(pre.getObjectType());
        setPath(pre.getPath());
        setSaveFilename(pre.getSaveFilename());
        setSaveThFilename(pre.getSaveThFilename());
        setThContentType(pre.getThContentType());
        setMetadata(pre.getMetadata());
        setUserMetadata(pre.getUserMetadata());
        setThMetadata(pre.getThMetadata());
        setThUserMetadata(pre.getThUserMetadata());
        setNotSupportMetadataThrowException(pre.getNotSupportMetadataThrowException());
        setNotSupportAclThrowException(pre.getNotSupportAclThrowException());
        setAttr(pre.getAttr());
        setProgressListener(pre.getProgressListener());
        setInputStreamPlus(pre.getInputStreamPlusDirect());
        setFileAcl(pre.getFileAcl());
        setThFileAcl(pre.getThFileAcl());
        setHashCalculatorManager(pre.getHashCalculatorManager());
    }
    private File file;

}
