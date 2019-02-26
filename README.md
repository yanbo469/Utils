# **Utils**
**android中常用的辅助类**

[GitHub主页](https://github.com/yanbo469/Utils)

## 功能介绍

	1.高斯模糊图片，毛玻璃处理，马赛克，水印
	2.登录注册
	3.毫秒倒计时
	4.动画帮助类
	5.敏感词过滤
	6.防止连点，仅响应设置时长的第一次操作
	7.获取版本号和版本
	8.复制到剪贴板
	9.显示相关帮助类
	10.双击退出工具类
	11.输入金额的文本框
	12.Log输出工具类
	13.字符串正则匹配
	14.获取资源文件的工具类
	15.屏幕截图的辅助类
	16.监听软键盘的打开和隐藏
	17.共享参数类
	18.设置状态栏辅助类
	19.Toast的工具类
	20.视图动画工具箱，提供简单的控制视图的动画的工具方法


## 集成方式

### 添加依赖

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```java
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

Step 2. Add the dependency

```java
dependencies {
   implementation 'com.github.yanbo469:Utils:v1.2'
}

```

Step 3. Add the Initialization

```java
public class Application {

    @Override
    public void onCreate() {
        super.onCreate();
	//初始化辅助类
        Utils.init(this);
    }
}


