package cc.bitky.clustermanage.server.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clustermanage.db.presenter.DbDeviceGroupPresenter;
import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgDeviceStatus;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgHeartBeat;

@Service
public class KyTcpMessageHandler {
  private final DbDeviceGroupPresenter dbDeviceGroupPresenter;
  private Logger logger = LoggerFactory.getLogger(KyTcpMessageHandler.class);

  @Autowired
  public KyTcpMessageHandler(DbDeviceGroupPresenter dbDeviceGroupPresenter) {
    this.dbDeviceGroupPresenter = dbDeviceGroupPresenter;
  }

  public void handleTcpMsg(IMessage message) {
    int msgId = message.getMsgId();
    switch (msgId) {
      case MsgType.HEART_BEAT:
        dbDeviceGroupPresenter.handleMsgHeartBeat((TcpMsgHeartBeat) message);
        break;
      case MsgType.DEVICE_RESPONSE_STATUS:
        dbDeviceGroupPresenter.handleMsgDeviceStatus((TcpMsgDeviceStatus) message);
        break;
    }
  }
}
