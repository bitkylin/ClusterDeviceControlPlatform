package cc.bitky.clustermanage;

public class ServerSetting {


    //-----------------------接收到充电状态帧时的处理策略----------------------------
    /**
     * 部署设备信息时，数据库中的设备和员工未初始化时，是否自动部署默认值
     */
    public static final boolean DEPLOY_DEVICES_INIT = true;
    /**
     * 默认员工卡号
     */
    public static String DEFAULT_EMPLOYEE_CARD_NUMBER = "";
    /**
     * 默认员工姓名
     */
    public static String DEFAULT_EMPLOYEE_NAME = "备用";
    /**
     * 默认员工单位
     */
    public static String DEFAULT_EMPLOYEE_DEPARTMENT = "默认单位";
    /**
     * 收到充电状态包时，自动创建相应的默认设备以及相应的员工和考勤表
     */
    public static final boolean AUTO_CREATE_DEVICE_EMPLOYEE = false;
    /**
     * 当设备中记录的剩余充电次数小于该值时，则向设备发送剩余充电次数
     */
    public static final int DEPLOY_REMAIN_CHARGE_TIMES = 20;
    /**
     * 待发送缓冲双端队列的限定容量，当队列中存在的 Message 大于该值时，使用时间轮延时向队列添加 Message
     */
    public static final int LINKED_DEQUE_LIMIT_CAPACITY = 10000;


    //------------------------待发送消息的任务调度策略------------------------------
    /**
     * 待发送缓冲队列中，帧发送间隔「单位/ms」
     */
    public static final int FRAME_SEND_INTERVAL = 20;
    /**
     * 已发送帧后，在该间隔时间后，检测接收回复帧的状态，用于检错重发功能「单位/s」
     */
    public static final int FRAME_SENT_TO_DETECT_INTERVAL = 5;
    /**
     * 待发送缓冲队列 Message 大于限定容量后，待执行指令的延迟等待执行时间「单位/s」
     */
    public static final int COMMAND_DELAY_WAITING_TIME = 30;

    //-----------------------接收到充电状态帧时的处理策略----------------------------
    /**
     * 项目版本号
     */
    public static final String VERSION = "0.8.6";
    /**
     * 主机名
     */
    public static String HOST = "lml-desktop";
    /**
     * 数据库
     */
    public static String DATABASE = "ChargeDevice";
}
