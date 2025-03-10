# 简历解析系统

基于自然语言处理技术的前后端分离项目，支持对多种格式的简历文件进行关键信息的提取，分析和数据可视化

> 有问题看 issues

### 目录说明

```
├─Assets	     相关图片
├─Express	     后端（docx文件转文本）
├─FastApi	     后端（封装解析模型）
├─Frontend	     前端
├─SpringBoot	     后端（用户和简历数据管理）
├─Sql	             数据库建表
├─Train	             模型训练
│  └─classfication   分类器
│  └─ner	     命名实体识别
│  └─test	     模型测试
├─Utils	             开发依赖工具
│  └─format	     格式转换
│  └─gather	     分类数据采集数据采集
```

### 项目运行

#### 前端

```shell
pnpm install

pnpm dev
```

#### 后端

##### FastAPI

本地运行需要下载模型到`FastApi/models`目录下，然后直接运行`script.py`文件即可

> 链接：https://pan.baidu.com/s/1ppkmLlsn4joBOYHH_jsrHg?pwd=w4r9 提取码：w4r9

##### Spring Boot

`SpringbootApplication.java`中是通过`@PropertySource("classpath:secrets.txt")`注解进行相关数据的读取，在`Springboot/src/main/resources`下建立`secrets.txt`并填入键值对

比如`application-dev.yml`中有如下配置

```yml
data:
  mongodb:
    host: ${mongodb.host.dev}
    port: ${mongodb.port.dev}
```

则在`secrets.txt`中这样声明具体值

```txt
mongodb.host.dev=127.0.0.1
mongodb.port.dev=27010
```

##### Express

`Express`中使用了`mammoth`来解析`docx`文件，可以作为`docx2txt`的替代品使用，你可以在`FastApi/document.py`中自行选择其一作为解析工具

> 在某些情况下`mammoth`的解析结果更加准确，具体体现在`docx2txt`会重复读取两次文件内容（可能跟文件有关也有可能库本身有缺陷）

```py
# 方法A FastApi向Express发送请求，通过js的mammoth库解析内容
import httpx

async def get_docx_content(file):
  # 本地默认路径
  url = 'http://127.0.0.1:3010/analysis-docx-file/'
  async with httpx.AsyncClient() as client:
      response = await client.post(url, files={"file": file.file})
      if response.status_code != 400:
          return eval(response.text)
      else:
          raise Exception

# 方法B FastApi直接通过python的docx2txt库解析内容
import docx2txt

async def get_docx_content(file):
    with io.BytesIO(await file.read()) as stream:
        text = docx2txt.process(stream)
    lines = text.splitlines()
    stripped_lines = [line.strip('\t').replace('\t', ' ') for line in lines]
    new_list = [x for x in stripped_lines if x.strip() != '']
    return new_list
```

如果使用`Express`需要在该模块下执行

```shell
pnpm install

node app.js
```

### 项目构建

---

#### 深度学习

PyTorch+BERT

#### 前端

- Vue3
- Element Plus
- ECharts

#### 后端

- Spring Boot
- FastAPI
- Express

#### 数据库

- MySQL
- MongoDB
- Redis

### 项目架构

---

![](Assets/architecture.png)

### 实现功能

1. 项目的主要业务为简历解析，支持`docx`，`pdf`，`txt`格式简历的输入并进行关键信息的提取，提取的维度包括：

- 姓名，生日，年龄，电话，邮箱，毕业院校（全部），最高学历，住址
- 求职意向，工作/项目经历，获得奖项，个人能力，岗位匹配
- 简历标签：学历标签，工作标签，能力标签，工作年限

2. 在该业务的基础上，我们使用权限管理系统对其进行封装。整体分为四个权限：游客，普通用户，高级权限，超级管理员。其中游客是未登录用户，用户注册后初始权限默认为普通用户，可以通过邮件申请并经过超级管理员的审核升级为高级权限。在本项目的设计理念中，普通用户和高级权限分别对应求职者和招聘者：

- 普通用户可以借助项目中的模块对自己的简历进行分析，补充并上传，从而实现投递简历的过程
- 高级权限可以查看所有普通用户上传的简历，并支持查看摘要，收藏和下载简历等操作，实现了帮助招聘者更好的处理和管理简历的过程。高级权限还可以查看简历的可视化信息，目前支持的可视化维度有：学历信息统计，来源地信息统计，工作经历信息统计，能够有助于招聘者更好的了解求职者和简历信息的整体情况

![](Assets/function.png)

### 项目截图

- 简历解析

![](Assets/result1.png)

- 简历上传

![](Assets/result2.png)

- 数据可视化

![](Assets/result3.png)

- 数据列表

![](Assets/result4.png)
