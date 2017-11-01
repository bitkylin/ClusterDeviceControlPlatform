namespace SocketServer.utils
{
    class MsgType
    {
        public const byte HeartBeat = 0x01;

        public const byte Error = 0x05;

        /**
         * 成组地发送消息
         */
        public const byte ServerSendGrouped = 0x0A;

        /**
         * 紧急发送该消息
         */
        public const byte ServerSendSpecial = 0x0B;


        //-------------------服务器下发-----------------------

        /**
         * 获取充电状态
         */
        public const byte ServerRequsetStatus = 0x10;

        /**
         * 设置剩余充电次数
         */
        public const byte ServerSetRemainChargeTimes = 0x11;

        /**
         * 设置设备ID
         */
        public const byte ServerSetDeviceId = 0x15;

        /**
         * 设置员工姓名
         */
        public const byte ServerSetEmployeeName = 0x16;

        /**
         * 设置员工单位第一帧
         */
        public const byte ServerSetEmployeeDepartment1 = 0x17;

        /**
         * 设置员工卡号
         */
        public const byte ServerSetEmployeeCardNumber = 0x19;

        /**
         * 服务器远程开锁
         */
        public const byte ServerRemoteUnlock = 0x1A;

        /**
         * 万能卡号设置
         */
        public const byte ServerSetFreeCardNumber = 0x70;

        //-------------------设备回复-----------------------

        /**
         * 设备回复自己的状态
         */
        public const byte DeviceResponseStatus = 0x40;

        /**
         * 设置剩余充电次数的回复
         */
        public const byte DeviceResponseRemainChargeTimes = 0x41;

        /**
         * 设置设备ID的回复
         */
        public const byte DeviceResponseDeviceId = 0x45;

        /**
         * 设置员工姓名的回复
         */
        public const byte DeviceResponseEmployeeName = 0x46;

        /**
         * 设置员工单位的回复「1」
         */
        public const byte DeviceResponseEmployeeDepartment1 = 0x47;

        /**
         * 设置员工单位的回复「2」
         */
        public const byte DeviceResponseEmployeeDepartment2 = 0x48;

        /**
         * 设置员工卡号的回复
         */
        public const byte DeviceResponseEmployeeCardNumber = 0x49;

        /**
         * 服务器远程开锁的回复
         */
        public const byte DeviceResponseRemoteUnlock = 0x4A;

        /**
         * 万能卡号设置的回复
         */
        public const byte DeviceResponseFreeCardNumber = (byte) 0x80;

        //-------------------初始化流程信息-----------------------

        /**
         * 设备主动发送卡号,包括员工卡号和确认卡号
         */
        public const byte InitializeDeviceResponseCard = (byte) 0xAA;

        /**
         * 服务器匹配确认卡号回复
         */
        public const byte InitializeServerMarchConfirmCardResponse = (byte) 0xAB;

        /**
         * 服务器清除设备的初始化状态
         */
        public const byte InitializeServerClearInitializeMessage = (byte) 0xAD;
    }
}