package cc.bitky.clusterdeviceplatform.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceRemainChargeTimes;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.ServerSetting;
import cc.bitky.clusterdeviceplatform.server.db.DbPresenter;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.tcp.TcpPresenter;
import cc.bitky.clusterdeviceplatform.server.tcp.exception.ExceptionMsgTcp;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.ChannelOutline;

@Service
public class ServerTcpProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TcpPresenter tcpPresenter;
    private final DbPresenter dbPresenter;

    @Autowired
    public ServerTcpProcessor(TcpPresenter tcpPresenter, ServerCenterProcessor centerProcessor, DbPresenter dbPresenter) {
        this.tcpPresenter = tcpPresenter;
        this.dbPresenter = dbPresenter;
        tcpPresenter.setServer(this);
        centerProcessor.setTcpProcessor(this);
    }

    /**
     * 将指定的消息对象发送至 TCP 通道
     *
     * @param message 指定的消息对象
     */
    boolean sendMessage(BaseMsg message) {
        logger.info("发送消息：「" + message.getMsgDetail() + "」");
        return tcpPresenter.sendMessageToTcp(message);
    }

    /**
     * Netty 模块捕获到 Java 消息对象
     *
     * @param message 消息对象
     */
    public void huntMessage(BaseMsg message) {
        logger.info("捕获到「普通」消息对象：「" + message.getMsgDetail() + "」");
    }

    /**
     * Netty 模块捕获到「设备状态」消息对象
     *
     * @param message 消息对象
     */
    public void huntDeviceStatusMsg(MsgReplyDeviceStatus message) {
        logger.info("捕获到消息对象：「" + message.getMsgDetail() + "」");
        long l1 = System.currentTimeMillis();
        Device device = dbPresenter.handleMsgDeviceStatus(message);
        //部署剩余充电次数
        if (device != null) {
            deployRemainChargeTimes(device);
        }
        long l2 = System.currentTimeMillis();
        if (ServerSetting.DEBUG) {
            logger.info("处理总时间：" + (l2 - l1) + "ms");
        }
    }

    /**
     * 部署剩余充电次数
     *
     * @param device 处理后的 Device
     */
    private void deployRemainChargeTimes(Device device) {
        //当前充电状态为「充满」，并且剩余充电次数小于或等于阈值时，部署剩余充电次数
        if (device.getChargeStatus() == ChargeStatus.FULL && device.getRemainChargeTime() <= CommSetting.DEPLOY_REMAIN_CHARGE_TIMES) {
            int remainTimes = device.getRemainChargeTime();
            remainTimes = remainTimes > 0 ? remainTimes : 0;
            sendMessage(MsgCodecDeviceRemainChargeTimes.create(device.getGroupId(), device.getDeviceId(), remainTimes));
        }
    }

    /**
     * 「内部接口」发生异常时回调该接口传出异常信息
     *
     * @param msg 一场消息对象
     */
    public void touchUnusualMsg(ExceptionMsgTcp msg) {
        logger.info("捕获到异常消息：" + msg.getDetail() + "原始消息：「" + msg.getBaseMsg().getMsgDetail() + "」");
    }

    public void shutDown() {
        tcpPresenter.shutDown();
    }

    //--------------- 数据统计 ---------------

    /**
     * 统计所有 Channel 概览信息
     */
    public ChannelOutline statisticChannelLoad() {
        return tcpPresenter.statisticChannelLoad();
    }
}
