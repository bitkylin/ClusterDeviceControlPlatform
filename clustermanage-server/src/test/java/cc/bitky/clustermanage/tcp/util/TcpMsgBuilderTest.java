package cc.bitky.clustermanage.tcp.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TcpMsgBuilderTest {
    @Autowired
    ServerTcpMessageHandler serverTcpMessageHandler;

    @Test
    public void buildDeviceId() throws Exception {
    }

    @Test
    public void buildEmployeeName() throws Exception {
    }

    @Test
    public void buildEmployeeDepartment() throws Exception {
    }

    @Test
    public void buildEmployeeCardNumber() throws Exception {
    }

    @Test
    public void buildRemainChargeTimes() throws Exception {
    }

    @Test
    public void buildRemainChargeTimes1() throws Exception {
    }

    @Test
    public void buildWebUnlock() throws Exception {
    }



//  private TcpMsgBuilder tcpMsgBuilder = new TcpMsgBuilder();
//
//  @BeforeClass
//  public static void init() {
//    System.out.println("员工CAN帧生成测试: ");
//  }
//
//  @Test
//  public void testbuildEmployeeName() {
//    System.out.println("构建员工姓名的CAN帧测试: ");
//    WebMsgDeployEmployeeName webMsgDeployEmployeeName = new WebMsgDeployEmployeeName(11, 12, "李明亮");
//    byte[] bytes = tcpMsgBuilder.buildEmployeeName(webMsgDeployEmployeeName);
//    ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
//    KyLog.LogFrame(byteBuf);
//  }
//
//  @Test
//  public void testbuildEmployeeCardNumber() {
//    System.out.println("构建员工卡号的CAN帧测试: ");
//    WebMsgDeployEmployeeCardNumber webMsgDeployEmployeeCardNumber =
//        new WebMsgDeployEmployeeCardNumber(11, 12, -9151031864016699135L);
//    byte[] bytes = tcpMsgBuilder.buildEmployeeCardNumber(webMsgDeployEmployeeCardNumber);
//    ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
//    KyLog.LogFrame(byteBuf);
//  }
//
//  @Test
//  public void testbuildEmployeeDepartment() {
//    System.out.println("构建员工单位的CAN帧测试「1」: ");
//    WebMsgDeployEmployeeDepartment webMsgDeployEmployeeDepartment = new WebMsgDeployEmployeeDepartment(11, 12, "主席");
//    byte[] bytes = tcpMsgBuilder.buildEmployeeDepartment(webMsgDeployEmployeeDepartment);
//    ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
//    KyLog.LogFrame(byteBuf);
//
//    System.out.println("构建员工单位的CAN帧测试「2」: ");
//     webMsgDeployEmployeeDepartment = new WebMsgDeployEmployeeDepartment(11, 12, "主席的子孙是");
//     bytes = tcpMsgBuilder.buildEmployeeDepartment(webMsgDeployEmployeeDepartment);
//     byteBuf = Unpooled.copiedBuffer(bytes);
//    KyLog.LogFrame(byteBuf);
//  }
}
