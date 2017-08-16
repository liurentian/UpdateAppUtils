# UpdateAppUtils
App更新工具
# 效果预览图
![img](https://github.com/liurentian/UpdateAppUtils/blob/master/demo.gif)


## 集成
### 1、在build.grade(project) 添加项目源url 
``` 
maven { url 'https://jitpack.io' }
```
示例：
<br/><br/>
  ![img](https://github.com/liurentian/UpdateAppUtils/blob/master/step1.png)

### 2、在build.grade(model) 添加依赖
``` 
dependencies {
		compile 'com.github.liurentian:UpdateAppUtils:v1.2.2'
	}
```


## 使用

更新检测一般放在MainActivity或者启动页上， 在请求服务器版本检测接口获取到versionCode、versionName、最新apkPath后调用。

``` 
UpdateAppUtil.from(this)
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                .serverVersionCode(2)  //服务器versionCode
                .serverVersionName("2.0")  //服务器versionName
                .apkPath(apkPath) //最新apk下载地址
                .isForce(true) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                .update();
```
