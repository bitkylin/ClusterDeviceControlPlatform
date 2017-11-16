package cc.bitky.clustermanage.db.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cc.bitky.clustermanage.db.bean.routineinfo.HistoryInfo;
import cc.bitky.clustermanage.db.bean.routineinfo.LampStatusHistory;
import cc.bitky.clustermanage.db.repository.RoutineTableRepository;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;

@Repository
public class DbRoutinePresenter {
    private final RoutineTableRepository routineTableRepository;

    @Autowired
    public DbRoutinePresenter(RoutineTableRepository routineTableRepository) {
        this.routineTableRepository = routineTableRepository;
    }

    /**
     * 更新员工的考勤表
     *
     * @param employeeObjectId     员工的 ObjectId，该值不能为 Null
     * @param tcpMsgResponseStatus 设备状态包
     */
    void updateRoutineById(String employeeObjectId, TcpMsgResponseStatus tcpMsgResponseStatus) {
        LampStatusHistory lampStatusHistory = routineTableRepository.findOne(employeeObjectId);
        if (lampStatusHistory == null) {
            lampStatusHistory = new LampStatusHistory();
            lampStatusHistory.setId(employeeObjectId);
        }
        lampStatusHistory.getStatusList().add(new HistoryInfo(tcpMsgResponseStatus.getTime(), tcpMsgResponseStatus.getStatus()));

        //更新员工的考勤表，根据设计需求已删除
        //   updateDutyInfo(tcpMsgResponseStatus, lampStatusHistory.getDutyInfos());

        routineTableRepository.save(lampStatusHistory);
    }
}
