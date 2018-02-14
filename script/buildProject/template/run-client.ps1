chcp 65001
java -jar -'Dfile.encoding'=UTF-8 .\ClusterDevicePlatform-client-1.8.9-release.jar cdg-pc 100

$x = $host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")