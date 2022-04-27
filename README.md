#### Decompile
>Android 恶意软件检测系统
>使用 java 语言在 windows 环境下执行 apktool 工具的反编译命令，反编译 apk 文件，提取特征集，与恶意样本集比较计算出相似度，根据相似度判断是否为恶意软件
<br>

- 数据库：MySQL<br>
- Apk Tool：ApkToolkit v3.0<br>

##### 实现

(一) 数据库预处理<br>
1. 先反编译大量恶意软件样本的 apk 文件<br>
2. 提取恶意应用在 AndroidManifest.xml 文件中申请的系统权限，保存到数据库中。<br>
3. 提取和统计 smail 文件中对 Android api 的具体调用和调用次数，保存到数据库中。<br>

(二) 恶意检测<br>
1. 反编译待测应用的 apk 文件，提取权限调用信息和 api 调用信息。<br>
2. 与数据库中的恶意样本进行比较，计算出相似度。<br>
3. 根据相似度值判断是否为恶意软件及其所属恶意类型。<br>

##### 使用
1. 导入数据库文件：malware.sql ，数据库共有 172 个恶意软件的数据。<br>
2. Main.java 文件的 main 方法是程序的执行入口。<br>
3. init() 方法用于数据库预处理，如果直接导入数据库就不需执行此方法，否则需要参照上面的目录结构，将恶意 apk 文件存放到`D:\Decompile\decompile`下对应目录中，之后注释 detection()
方法，执行 init() 方法。<br>
4. 注释 init() 方法，执行 detection() 方法会开始进行恶意检测，在此之前需要将待测 apk 文件放到这个目录下：`D:\Decompile\decompile\test_sw`，检测结束后结果会显示在 idea 的控制台中。<br>
<br>
tip：<br>
1. apktool 的反编译时间由 apk 文件大小不同而有长有短。<br>
2. 如果数据库密码不是`root`，那就需要修改 com.duan.db 包下的 DBMalwareHelper.java 中的 PASSWORD 变量(数据库名 DATABASE_MALWARE 变量)。<br>
3. 一次检测只能检测一个 apk 文件，所以`D:\Decompile\testApks\test_sw`目录下只能放一个待测的 apk 文件，不能有其他无关文件<br>
4. 如果进行预处理，而恶意 apk 文件较多，那么预处理的时间也会比较长(我当初给 172 的文件做预处理花了差不多半个小时)<br>
<br>
Main.java<br>

```java
public static void main(String[] args) {
//     init();
     detection();
}
```

##### 存储结构和项目结构
存储根目录为 D:\Decompile\  

<img height="500px" src="https://raw.githubusercontent.com/DuanJiaNing/Decompile/master/struct.png"/>

##### 程序运行结果
<img height="500px" src="https://raw.githubusercontent.com/DuanJiaNing/Decompile/master/result.png"/>
