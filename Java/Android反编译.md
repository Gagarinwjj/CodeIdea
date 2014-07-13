#Android反编译
##一、反编译方式
###1、`.apk`->`classes.dex`->`classes_dex2jar.jar`->`java`
---
####将`.apk` 文件使用*`【WinRAR】`*解压，找到 `classes.dex`，通过工具*`【dex2jar】`* 反编译为 `classes_dex2jar.jar`，使用*`【jd-gui】`*反编译为`.java`源文件查看。

###2、使用google提供的apktool
---
#### google官方工具，可以反编译出资源文件，亦可打包编译出的资源文件，可用于汉化，常用。

###3、使用图形化工具
---
1. Androidfby
2. 反编译工具包
##二、工具
1. Androidfby： 图形化反编译APK（签过名的无法反编译）
2. [apktool1.5.2](https://code.google.com/p/android-apktool/ "google code site")： google官方工具，可以反编译出资源文件，亦可打包编译出的资源文件，可用于汉化，常用。
3. [dex2jar-0.0.9.15](https://code.google.com/p/dex2jar/ "google code site")： 将解压出来的classes.dex 反编译jar
4. [JD-GUI](http://jd.benow.ca/ "官方网站")： 将jar反编译为java
5. 反编译工具： 添加到右键菜单（比Androidfby 好用 强大。效果相当于apktool，也是反编译出资源文件，不过默认反编译在当前目录，而且不用命令行，更加方便）


##三、参考链接
[Android APK反编译详解（附图）](http://blog.csdn.net/ithomer/article/details/6727581 "csdn参考")
