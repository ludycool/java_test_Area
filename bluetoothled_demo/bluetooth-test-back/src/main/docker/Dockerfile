# 基于哪个镜像
FROM  zhangyalong/jdk1.8-ubuntu18.04:v1
MAINTAINER zhougs <zhougs@topband.com.cn>
USER root

ENV JAR_NAME=@project.build.finalName@.jar
ENV WORK_HOME="/"


#设置工作目录
WORKDIR ${WORK_HOME}


# 拷贝文件到容器
COPY $JAR_NAME  app.jar
COPY docker-entrypoint.sh $WORK_HOME/
# 给入口脚本添加可执行权限
RUN chmod u+x docker-entrypoint.sh

# 声明需要暴露的端口
EXPOSE 18200

# 配置容器启动后执行的命令
ENTRYPOINT ["/docker-entrypoint.sh"]