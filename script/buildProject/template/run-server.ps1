chcp 65001
java -jar -'Dfile.encoding'=UTF-8 .\ClusterDevicePlatform-server-1.8.9-release.jar

$x = $host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")