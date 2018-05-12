using System;

namespace ServerPreSetting
{
    //服务器总体设置
    //该配置文件不可随意修改，以免失效
    public class KySetting
    {
        /// <summary>
        /// 已连接设备组未响应持续的时间间隔
        /// </summary>
        public int 通道未响应时间 = 5 * 60;

        /// <summary>
        /// 已连接设备组指定时间无响应检测
        /// </summary>
        public bool 通道无响应监测 = true;

        /// <summary>
        ///服务器发送消息对象后，是否需要设置检错重发任务「要求客户端回复确认」 
        /// </summary>
        public bool 帧送达监测 = true;

        /// <summary>
        /// 是否开启调试信息输出
        /// </summary>
        public bool 调试模式 = false;

        /// <summary>
        /// 是否开启web调用返回随机数据
        /// </summary>
        public bool 随机Web数据模式 = false;

        /// <summary>
        /// 数据库是否已启动用户鉴权特性
        /// </summary>
        public bool 数据库认证模式 = false;

        /// <summary>
        /// 部署员工信息时，若数据库中无指定的相关信息，而部署的默认员工默认信息
        /// </summary>
        public string 员工默认卡号 = "0";

        public string 员工默认姓名 = "备用";

        public string 员工默认部门 = "默认单位";

        /// <summary>
        /// 当剩余充电次数小于该阈值时，服务器下发剩余充电次数
        /// </summary>
        public int 部署剩余充电次数阈值 = 20;

        /// <summary>
        /// 服务器下发数据帧时，帧发送间隔「单位：ms」
        /// </summary>
        public int 帧发送间隔 = 200;

        /// <summary>
        /// 检错重发最大次数，服务器向 TCP 通道发送 CAN 帧，最大重复发送次数
        /// </summary>
        public int 检错重发最大重复次数 = 5;

        /// <summary>
        /// 服务器 TCP 客户端的端口号
        /// </summary>
        public int 服务器端口号 = 30232;

        /// <summary>
        /// 数据库服务器的主机名或者IP地址
        /// </summary>
        public string 数据库服务器的主机名或IP = "localhost";

        /// <summary>
        /// 数据库服务器的鉴权用户名
        /// </summary>
        public string 数据库用户名 = "未设置";

        /// <summary>
        /// 数据库服务器的鉴权密码
        /// </summary>
        public string 数据库密码 = "未设置";


    }
}