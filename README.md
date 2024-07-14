### x-file-host
一个多源图床平台,可以把你上传的图片同时上传到多个图床,并且在获取图片的时候自动检测图片可用性,重定向到可用的图片url,提高图片的可用性。
防止出现把鸡蛋放在一个篮子里，然后篮子坏了鸡蛋全军覆没的情况。
当前为MVP版本,诸多功能还未完善,许许多多的地方没测试到,遇到问题请不要急躁,欢迎提出建议和PR。~~~给个start吧~~~
### 框架
SpringBoot + Mybatis-plus + x-file-storage + Hutool + Lombok + sqlite + ~~~我的聪明才智~~~
### 特性
基于本地/网盘/oss/webdev的文件储存服务基于 [x-file-storage](https://x-file-storage.xuyanwu.cn/#/),
而x-file-storage支持以下储存方式：
支持本地、FTP、SFTP、WebDAV、阿里云 OSS、华为云 OBS、七牛云 Kodo、腾讯云 COS、百度云 BOS、又拍云 USS、MinIO、
Amazon S3、GoogleCloud Storage、FastDFS、 Azure Blob Storage、Cloudflare R2、金山云 KS3、美团云 MSS、
京东云 OSS、天翼云 OOS、移动 云EOS、沃云 OSS、 网易数帆 NOS、Ucloud US3、青云 QingStor、平安云 OBS、
首云 OSS、IBM COS、其它兼容 S3 协议的存储平台。查看 [所有支持的存储平台](https://x-file-storage.xuyanwu.cn/#/%E5%AD%98%E5%82%A8%E5%B9%B3%E5%8F%B0)

另外在此之上还增加了对API的支持，理论上所有提供api的图床都支持，以下为测试过的图床：
[picgo](http://www.picgo.net/),[imgbb](https://imgbb.com/login),[tucang(需手机号)](http://tucang.cc)
### 安装
先阅读并按照自己需求编写[application.yml](src%2Fmain%2Fresources%2Fapplication.yml)配置文件,然后
下载最新的jar包,仓库的[identifier.sqlite](identifier.sqlite)和你修改后的的application.yml放到jar包同级
然后执行以下命令(需要java环境哦~),默认端口为8080可在application.yml中修改,图片源也在其中修改

```shell
# 临时启动测试
java -jar x-file-host-0.0.1-SNAPSHOT.jar
# 后台启动
nohup java -jar x-file-host-0.0.1-SNAPSHOT.jar > x-file-host.log 2>&1 &
```
### 使用
没有前端因为我前端菜..(欢迎提供前端页面),可以使用postman等工具调用
当前的API接口:
- 上传图片
```http request
POST /upload
Content-Type: multipart/form-data
x-file-host-token: your token #未配置则不需要

file: file
```
返回值样例:
```json
{
  "success": true,
  "message": "Success",
  "data": "23b0ffb44350003d394e74b9e1788ea32198497fc6a52258a701c8ff71d67ea5.png", 
  "timestamp": 1720954924474
}
```
- 根据文件名获取图片
```http request
GET /image/{{filename}}}
```
### 图床API配置教程
1. 选择一个图床，注册账号，获取图床的api相关信息
2. 在配置文件的`http-file-storage`属性下添加图床配置
```yml
    # 图床标志，写域名就行，或者随便起不重复即可
    - platform: "imgbb"
      # 图床的上传图片api地址
      url: "https://api.imgbb.com/1/upload"
      # 图床的上传图片api的请求方法，一般都为POST
      method: "POST"
      # 图床的上传图片api的请求头 不需要的话可以不写
      headerMap:
        content-type: "multipart/form-data"
      # 图片上传需要的参数 请求体 不需要的话可以不写
      arg-map:
        key: "your api key"
      # 上传的图片字段的参数名
      filename: "image"
      # 调用上传接口的超时时间 单位秒
      timeout: 5
      # 图床的上传图片api的返回结果中url地址的json路径
      resultJsonUrlPath: "data.image.url"
```

### 代办
-[] 更好的校验图片是否云效方法 目前tucang不管图片是否存在都返回302
-[] 测试OSS、webdev等存储服务   
-[] 测试本地储存服务   
-[] 定时任务检测图片可用性，少于指定值自动选择图床补传  
-[] 等你来提..
### 数据库
建表sql：
```sql
CREATE TABLE IF NOT EXISTS images (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      file_name TEXT NOT NULL,
                                      image_url TEXT NOT NULL,
                                      status INTEGER NOT NULL,
                                      platform TEXT NOT NULL,
                                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_file_name_status ON images (file_name, status);
```

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=RuiZhang-cn/x-file-host&type=Date)](https://star-history.com/#RuiZhang-cn/x-file-host&Date)
