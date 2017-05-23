package cc.bitky.clustermanage.db.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import cc.bitky.clustermanage.db.bean.routineinfo.DutyInfo;
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

    //用于更新考勤表，可删除
    private void updateDutyInfo(TcpMsgResponseStatus tcpMsgResponseStatus, List<DutyInfo> dutyInfos) {
        switch (tcpMsgResponseStatus.getStatus()) {

            case 1://设备状态变为使用中，进入上班状态
                DutyInfo dutyInfo = new DutyInfo(tcpMsgResponseStatus.getTime());
                dutyInfos.add(dutyInfo);
                break;

            case 2://设备状态变为充电中，进入下班状态
                if (dutyInfos.size() != 0) {
                    DutyInfo beforeInfo = dutyInfos.get(dutyInfos.size() - 1);
                    if (beforeInfo.getOffTime() == null) {
                        beforeInfo.setOffTime(new Date(tcpMsgResponseStatus.getTime()));
                        return;
                    }
                }
                DutyInfo newDutyInfo = new DutyInfo();
                newDutyInfo.setOffTime(new Date(tcpMsgResponseStatus.getTime()));
                dutyInfos.add(newDutyInfo);
                break;
        }
    }
}
