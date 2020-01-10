# android-webrtc-ns

### Android单独抽取WebRtc-NS/NSX(音频降噪)模块

#### csdn:https://blog.csdn.net/always_and_forever_/article/details/78472450

### 使用说明
#### 1.下载源码，直接运行即可

### 工程解读
#### 1.根目录下的jni目录，是从webrtc源码中抽取出来的ns模块核心代码文件.
#### 2.libs目录下，为编译jni生成的so文件，您可以直接使用.
#### 3.webrtc-ns.apk 可直接安装到真机上快速体验.
#### 4.test_input.pcm为测试文件，启动app将自动执行ns，ns后文件路径为手机根目录/ns_out.pcm
#### 5.如您需要体验生成so这一步骤，可cd到jni目录下，执行ndk-build命令，前提是您下载了ndk且配置了环境变量，否则ndk命令无法识别

### 测试对比
#### 1.如图中所示，上面是ns前，下面ns后
![Image text](https://raw.githubusercontent.com/wangzhengcheng1994/android-webrtc-ns/master/pic/ns.png)
