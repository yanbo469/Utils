# **Utils**
**的安卓中常用的辅助类**
[GitHub主页](https://github.com/yanbo469/Utils）



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

