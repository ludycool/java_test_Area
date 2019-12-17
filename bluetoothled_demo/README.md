# 项目介绍

###### spring boot项目包含常用组件,可做为项目开发 脚手架

**orm:** mybatis-plus+ dynamic-datasource-spring-boot-starter 

支持读写分离，一主多从的环境。以及单项目多主库的特殊场景  基体配置，请查看 [开源项目文档](https://gitee.com/ChuckChan/dynamic-datasource-spring-boot-starter/blob/master/README_zh.md)

**配置中心:** Nacos

**缓存:** redis

**发布:** 通过配置pom文件，使用mavean 执行deploy 直接生成docker镜像，上传到远程仓库

**代码生成器：** 点击下载 [绿色免安装](http://192.168.3.56:4567/lud/code_producer) _如需个性化定制，可向 ludi提_

## 项目结构

**common:** 公共包 

**entity:** 实体层 

**dao:** 数据访问层(mapper层) 

**service:** 业务接口层 

**serviceImpl:** 业务实现层，可通过添加启动类，变为dubbo服务应用 

**bluetooth-test-back:**  app应用接口服务

## Nacos配置中心应用

参考文档： 

[使用Nacos作为配置中心](http://blog.didispace.com/spring-cloud-alibaba-3/) 

[Nacos配置的加载规则详解](http://blog.didispace.com/spring-cloud-alibaba-nacos-config-1/) 

[Nacos配置的多环境管理](http://blog.didispace.com/spring-cloud-alibaba-nacos-config-2/) 

[Nacos配置的多文件加载](http://blog.didispace.com/spring-cloud-alibaba-nacos-config-3/)  

**pom.xml** 添加引用  
```
      <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
```
编辑<font color="#008000">bootstrap.properties</font> 把不同环境的配置放置在Nacos的配置中，常用的在<font color="#008000">bootstrap.properties</font>中
```
    spring.application.name=bluetooth-test-back
    spring.profiles.active=dev
    spring.cloud.nacos.config.server-addr=192.168.3.58:8848
    spring.cloud.nacos.config.file-extension=properties
    spring.cloud.nacos.config.group=DEFAULT_GROUP
    spring.cloud.nacos.config.namespace=fe95ddd8-c411-447e-b9dd-73f64a6ce621
```

程序会根据:命名空间<font color="#008000">namespace</font> 组<font color="#008000">group</font>、程序名<font color="#008000">spring.application.name</font>、环境<font color="#008000">spring.profiles.active</font>、文件类型<font color="#008000">file-extension</font>来加载配置文件 

配置文件名规则:{spring.application.name}-{spring.profiles.active}.{file-extension}  
如本项目的文件名：<font color="#008000">bluetooth-test-back-dev.properties  </font>
    

程序启动后会根据环境<font color="#FF0000">profiles.active</font>读取Nacos配置文件  

## maven 打包常用指令

控制台cd 到子项目如：`cd bluetooth-test-back`

创建镜像 
`mvn clean package docker:build` 

推送镜像到Registry 
`mvn clean package docker:build -DpushImage` 

推送指定tag的镜像到Registry 
`mvn clean package docker:build -DpushImageTag` 

执行 build、tag、push 操作 
`mvn deploy` 

如果想跳过 docker 某个过程时，只需加上：
```
-DskipDockerBuild     跳过 build 镜像
-DskipDockerTag      跳过 tag 镜像 
-DskipDockerPush    跳过 push 镜像
-DskipDocker        跳过整个阶段,只生成jar包
```
##docker容器 运行、更新

通过maven将镜像上传到仓库后，在需要运行的服务的机器上，运行或更新容器 
    
<font color="#0000FF" >示例：</font>  
```
   # 一、 删除容器
    docker rm -f bluetooth-test-back
    # 二、删除镜像
    docker rmi --force `docker images | grep bluetooth-test-back | awk '{print $3}'`

    # 三、远程摘取镜像
    docker pull 192.168.3.74:5000/bluetooth-test/bluetooth-test-back
    # 五、更改标签
    docker tag 192.168.3.74:5000/bluetooth-test/bluetooth-test-back bluetooth-test-back:latest

    # 六、运行容器
    docker run -d --name=bluetooth-test-back \
    --restart always \
    -e JAVA_OPTS='  -Dspring.profiles.active=dev '  \
    -p 18200:18200 \
    -v /data/software/springboot/bluetooth_back/logs:/logs \
    bluetooth-test-back
```
可将指令保存为脚本文件 <font color="#008000">pull-update-docker.sh</font> 

直接运行`sh pull-update-docker.sh` 完成更新

<font color="#FF0000" >注意：</font> 
1、bluetooth-test-back为镜像容器名称，根据实际更改  

2、-Dspring.profiles.active=dev为容器指定环境，生产、测试各有不同  

3、-p 程序端口映射

4、-v为程序日志、文件等进行磁盘挂载，根据实际变更




