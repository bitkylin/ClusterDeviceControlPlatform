package cc.bitky.clustermanage.server.bean;

import cc.bitky.clustermanage.db.presenter.DbPresenter;
import cc.bitky.clustermanage.server.message.ChargeStatus;
import cc.bitky.clustermanage.server.message.HeartBeat;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.tcp.util.enumky.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageHandler {
  private final DbPresenter dbPresenter;
  private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
  private int groupSize = -1;

  @Autowired
  public MessageHandler(DbPresenter dbPresenter) {
    this.dbPresenter = dbPresenter;
  }

  public void handle(IMessage message) {
    dbPresenter.initDb(message.getGroupId());

    int msgId = message.getMsgId();
    switch (msgId) {
      case MsgType.HEART_BEAT:
        dbPresenter.heartBeatHandle((HeartBeat) message);
        break;
      case MsgType.CHANGE_STATUS:
        dbPresenter.chargeStatusHandle((ChargeStatus) message);
        break;
    }
  }
}
