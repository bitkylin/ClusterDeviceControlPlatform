package cc.bitky.clustermanage.db.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cc.bitky.clustermanage.db.bean.routineinfo.HistoryInfo;
import cc.bitky.clustermanage.db.bean.routineinfo.RoutineTables;
import cc.bitky.clustermanage.db.repository.RoutimeTableRepository;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceStatus;

@Repository
public class DbRoutinePresenter {
    private final RoutimeTableRepository routimeTableRepository;

    @Autowired
    public DbRoutinePresenter(RoutimeTableRepository routimeTableRepository) {
        this.routimeTableRepository = routimeTableRepository;
    }

    /**
     * 更新员工的考勤表
     *
     * @param employeeObjectId           员工的 ObjectId
     * @param tcpMsgResponseDeviceStatus 设备状态包
     */
    public void updateRoutineById(String employeeObjectId, TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus) {
        RoutineTables routineTables = routimeTableRepository.findOne(employeeObjectId);
        if (routineTables == null) {
            routineTables = new RoutineTables();
            routineTables.setId(employeeObjectId);
        }
        routineTables.getHistoryInfos().add(HistoryInfo.newInstance(tcpMsgResponseDeviceStatus.getTime(), tcpMsgResponseDeviceStatus.getStatus()));
        routimeTableRepository.save(routineTables);
    }
}
