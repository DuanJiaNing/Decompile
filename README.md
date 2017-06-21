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

##### 存储结构和项目结构
<img height="300px" src="https://raw.githubusercontent.com/DuanJiaNing/Decompile/master/struct.png"/>

##### 程序运行结果
<img height="300px" src="https://raw.githubusercontent.com/DuanJiaNing/Decompile/master/result.png"/>