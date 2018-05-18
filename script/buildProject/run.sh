#!/bin/bash
# 集群设备管理系统工程的 web 端、模拟客户端、服务器端等的整体清理、构建、打包、发布

PROJECT_PATH=/mnt/d/project/github/ClusterDevicePlatform; # 工程所在目录
WEB_PATH=/mnt/c/developer/cluster-device-platform-web;    # Web 模块所在目录
SH_PATH=$(cd `dirname $0`; pwd)                           # 脚本所在目录
BUILD_TIME=`date "+%Y-%m-%d_%H-%M"`                       # 脚本运行时间
UTIL_JAR_PATH=$PROJECT_PATH/messageUtils;                 # 公共 Jar 模块所在目录
SERVER_PATH=$PROJECT_PATH/ClusterDevicePlatform-server;   # 服务器模块所在目录
CLIENT_PATH=$PROJECT_PATH/ClusterDevicePlatform-client;   # 硬件模拟客户端模块所在目录
TARGET_NAME=serverRelease_$BUILD_TIME;                    # 编译打包目录名
TARGET_PATH=$PROJECT_PATH/publish/release/$TARGET_NAME;   # 编译打包目录

# 项目已编译历史文件的清理
cd $UTIL_JAR_PATH;
rm -rf ./build;
cd $CLIENT_PATH;
rm -rf ./build;
cd $SERVER_PATH;
rm -rf ./build;
rm -rf ./src/main/resources/static;
cd $WEB_PATH;
rm -rf ./dist;

# 调整 node_modules 目录
if [ -e node_modulesLinux ] && [ -e node_modules ]
then
    mv node_modules node_modulesWin
    mv node_modulesLinux node_modules
fi

# Web 编译并将静态页面文件移入服务器项目中
npm run build
if [ ! $? -eq 0 ]
then echo "Web 编译出错"
    exit 1
fi

mv node_modules node_modulesLinux
mv node_modulesWin node_modules

echo Web 编译完毕
mkdir $SERVER_PATH/src/main/resources/static
cp -r ./dist/* $SERVER_PATH/src/main/resources/static

# Client、Server 的编译
cd $UTIL_JAR_PATH;
gradle build

cd $CLIENT_PATH;
gradle build

cd $SERVER_PATH;
gradle build

# 组织并集中编译生成的待发布文件
mkdir -p $TARGET_PATH
cd $TARGET_PATH
cp $CLIENT_PATH/build/libs/* .
cp $SERVER_PATH/build/libs/* .
cp $SH_PATH/template/* .

# 组装 Client、Server 的运行脚本
CLIENT_NAME=`ls $CLIENT_PATH/build/libs`
SERVER_NAME=`ls $SERVER_PATH/build/libs`

cat $SH_PATH/script-template/client-template | sed "s/{clientName}/$CLIENT_NAME/g" > run-client.ps1
cat $SH_PATH/script-template/server-template | sed "s/{serverName}/$SERVER_NAME/g" > run-server.ps1

cd ..
tar czvf $TARGET_NAME.tar.gz $TARGET_NAME


