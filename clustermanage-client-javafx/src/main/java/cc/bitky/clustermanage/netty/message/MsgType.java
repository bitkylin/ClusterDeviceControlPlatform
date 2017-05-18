package cc.bitky.clustermanage.netty.message;

public class MsgType {
    public static final byte HEART_BEAT = 0x01;

    public static final byte ERROR = 0x05;

    /**
     * 成组地发送消息
     */
    public static final byte SERVER_SEND_GROUPED = 0x0A;
    /**
     * 紧急发送该消息
     */
    public static final byte SERVER_SEND_SPECIAL = 0x0B;


    //-------------------服务器下发-----------------------

    /**
     * 获取充电状态
     */
    public static final byte SERVER_REQUSET_STATUS = 0x10;
    /**
     * 设置剩余充电次数
     */
    public static final byte SERVER_SET_REMAIN_CHARGE_TIMES = 0x11;
    /**
     * 设置设备ID
     */
    public static final byte SERVER_SET_DEVICE_ID = 0x15;
    /**
     * 设置员工姓名
     */
    public static final byte SERVER_SET_EMPLOYEE_NAME = 0x16;
    /**
     * 设置员工单位第一帧
     */
    public static final byte SERVER_SET_EMPLOYEE_DEPARTMENT_1 = 0x17;
    /**
     * 设置员工单位第二帧
     */
    public static final byte SERVER_SET_EMPLOYEE_DEPARTMENT_2 = 0x18;
    /**
     * 设置员工卡号
     */
    public static final byte SERVER_SET_EMPLOYEE_CARD_NUMBER = 0x19;
    /**
     * 服务器远程开锁
     */
    public static final byte SERVER_REMOTE_UNLOCK = 0x1A;
    /**
     * 万能卡号设置
     */
    public static final byte SERVER_SET_FREE_CARD_NUMBER = 0x70;

    //-------------------设备回复-----------------------

    /**
     * 设备回复自己的状态
     */
    public static final byte DEVICE_RESPONSE_STATUS = 0x40;
    /**
     * 设置剩余充电次数的回复
     */
    public static final byte DEVICE_RESPONSE_REMAIN_CHARGE_TIMES = 0x41;
    /**
     * 设置设备ID的回复
     */
    public static final byte DEVICE_RESPONSE_DEVICE_ID = 0x45;
    /**
     * 设置员工姓名的回复
     */
    public static final byte DEVICE_RESPONSE_EMPLOYEE_NAME = 0x46;
    /**
     * 设置员工单位的回复「1」
     */
    public static final byte DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1 = 0x47;
    /**
     * 设置员工单位的回复「2」
     */
    public static final byte DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_2 = 0x48;

    /**
     * 设置员工卡号的回复
     */
    public static final byte DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER = 0x49;
    /**
     * 服务器远程开锁的回复
     */
    public static final byte DEVICE_RESPONSE_REMOTE_UNLOCK = 0x4A;
    /**
     * 万能卡号设置的回复
     */
    public static final byte DEVICE_RESPONSE_FREE_CARD_NUMBER = (byte) 0x80;

    //-------------------初始化流程信息-----------------------

    /**
     * 设备主动发送卡号,包括员工卡号和确认卡号
     */
    public static final byte INITIALIZE_DEVICE_RESPONSE_CARD = (byte) 0xAA;
    /**
     * 服务器匹配确认卡号回复
     */
    public static final byte INITIALIZE_SERVER_MARCH_CONFIRM_CARD_RESPONSE = (byte) 0xAB;

    /**
     * 服务器清除设备的初始化状态
     */
    public static final byte INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE = (byte) 0xAD;
}
