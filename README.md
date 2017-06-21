#### Decompile
>Android 恶意软件检测系统
>使用 java 语言在 windows 环境下执行 apktool 工具的反编译命令，反编译 apk 文件，提取特征集，与恶意样本集比较计算出相似度，根据相似度判断是否为恶意软件
<br>

- 数据库：MySQL<br>
- Apk Tool：ApkToolkit v3.0<br>

##### 实现

(一) 数据库预处理<br>
1. 先反编译大量恶意软件样本的 apk 文件<br>
2. 提取恶意应用在 AndroidManifest.xml 文件中申请的系统权限，保存到数据库中<br>
3. 提取和统计 smail 文件中对 Android api 的具体调用和调用次数，保存到数据库中<br>

(二) 恶意检测<br>
1. 反编译待测应用的 apk 文件，提取权限调用信息和 api 调用信息<br>
2. 与数据库中的恶意样本进行比较，计算出相似度<br>
3. 根据相似度值判断是否为恶意软件及其所属恶意类型<br>

##### 使用
1. 导入数据库文件：malware.sql ，数据库共有 172 个恶意软件的数据。<br>
2. com.duan.Main.java 是程序的执行入口，init() 方法是进行数据库预处理用的，detection() 方法会开始进行恶意检测，在此之前需要将
待测 apk 文件放到这个目录下(一次检测只能检测一个 apk 文件，所有词目录下只能放待测的 apk 文件，不能有其他无关文件)：`D:\Decompile\decompile\test_sw`

Main.java
```java
public static void main(String[] args) {
//     init();
     detection();
}
```

##### 存储结构和项目结构
<img height="500px" src="https://raw.githubusercontent.com/DuanJiaNing/Decompile/master/struct.png"/>

##### 程序运行结果
<img height="500px" src="https://raw.githubusercontent.com/DuanJiaNing/Decompile/master/result.png"/>