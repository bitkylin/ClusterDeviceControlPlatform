package cc.bitky.clustermanage.db.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cc.bitky.clustermanage.db.bean.routineinfo.HistoryInfo;
import cc.bitky.clustermanage.db.bean.routineinfo.RoutineTables;
import cc.bitky.clustermanage.db.repository.RoutineTableRepository;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceStatus;

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
     * @param employeeObjectId           员工的 ObjectId
     * @param tcpMsgResponseDeviceStatus 设备状态包
     */
    void updateRoutineById(String employeeObjectId, TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus) {
        RoutineTables routineTables = routineTableRepository.findOne(employeeObjectId);
        if (routineTables == null) {
            routineTables = new RoutineTables();
            routineTables.setId(employeeObjectId);
        }
        routineTables.getHistoryInfos().add(HistoryInfo.newInstance(tcpMsgResponseDeviceStatus.getTime(), tcpMsgResponseDeviceStatus.getStatus()));
        routineTableRepository.save(routineTables);
    }
}
