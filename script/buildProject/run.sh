#!/bin/bash
# 集群设备管理系统工程的web端、模拟客户端、服务器端等的整体清理、构建、打包、发布

SH_PATH=$(cd `dirname $0`; pwd)                           # 脚本所在目录
BUILD_TIME=`date "+%Y-%m-%d_%H-%M"`                       # 脚本运行时间
PROJECT_PATH=/mnt/d/project/github/ClusterDevicePlatform; # 工程所在目录 
UTIL_JAR_PATH=$PROJECT_PATH/messageUtils;                 # 公共 Jar 模块所在目录 
SERVER_PATH=$PROJECT_PATH/ClusterDevicePlatform-server;   # 服务器模块所在目录 
CLIENT_PATH=$PROJECT_PATH/ClusterDevicePlatform-client;   # 硬件模拟客户端模块所在目录 
WEB_PATH=/mnt/c/developer/cluster-device-platform-web;    # Web 模块所在目录 

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

# Web 编译并将静态页面文件移入服务器项目中
npm run build
if [ ! $? -eq 0 ]
  then echo "Web 编译出错"
  exit 1
fi
echo Web 编译完毕
mkdir $SERVER_PATH/src/main/resources/static
cp -r ./dist $SERVER_PATH/src/main/resources/static

# Client、Server 的编译
cd $UTIL_JAR_PATH; 
gradle build

cd $CLIENT_PATH; 
gradle build

cd $SERVER_PATH; 
gradle build

# 组织并集中编译生成的待发布文件
mkdir -p $PROJECT_PATH/publish/release/serverRelease_$BUILD_TIME
cd $PROJECT_PATH/publish/release/serverRelease_$BUILD_TIME
cp $CLIENT_PATH/build/libs/* .
cp $SERVER_PATH/build/libs/* .
cp $SH_PATH/template/* .

# 组装 Client、Server 的运行脚本
CLIENT_NAME=`ls $CLIENT_PATH/build/libs`
SERVER_NAME=`ls $SERVER_PATH/build/libs`

echo chcp 65001 >> run-client.ps1
echo java -jar -\'Dfile.encoding\'=UTF-8 .\\$CLIENT_NAME cdg-pc 100 >> run-client.ps1
echo '$x = $host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")' >> run-client.ps1

echo chcp 65001 >> run-server.ps1
echo java -jar -\'Dfile.encoding\'=UTF-8 .\\$SERVER_NAME >> run-server.ps1
echo '$x = $host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")' >> run-server.ps1
