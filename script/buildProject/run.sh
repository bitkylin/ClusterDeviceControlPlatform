SH_PATH=$(cd `dirname $0`; pwd)
BUILD_TIME=`date "+%Y-%m-%d_%H-%M"`
PROJECT_PATH=/mnt/d/project/github/ClusterDevicePlatform; 
UTIL_JAR_PATH=$PROJECT_PATH/messageUtils; 
SERVER_PATH=$PROJECT_PATH/ClusterDevicePlatform-server; 
CLIENT_PATH=$PROJECT_PATH/ClusterDevicePlatform-client; 

WEB_PATH=/mnt/c/developer/cluster-device-platform-web; 

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

mkdir -p $PROJECT_PATH/publish/release/serverRelease_$BUILD_TIME
cd $PROJECT_PATH/publish/release/serverRelease_$BUILD_TIME
cp $CLIENT_PATH/build/libs/* .
cp $SERVER_PATH/build/libs/* .
cp $SH_PATH/template/* .

