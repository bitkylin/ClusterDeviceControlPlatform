using System;
using SocketServer.message;
using SocketServer.message.@base;

namespace SocketServer.utils
{
    class CanBuilder
    {
        private static TcpMsgBuilder _tcpMsgBuilder=new TcpMsgBuilder();

        public static byte[] buildByMessage(IMessage message)
        {
            switch (message.getMsgId())
            {
//                case MsgType.SERVER_REQUSET_STATUS:
//                    WebMsgObtainDeviceStatus obtainDeviceStatus = (WebMsgObtainDeviceStatus) message;
//                    Console.WriteLine("生成帧：请求设备当前状态");
//                    return tcpMsgBuilder.buildRequestDeviceStatus(obtainDeviceStatus);
//
//                case MsgType.SERVER_SET_REMAIN_CHARGE_TIMES:
//                    WebMsgDeployRemainChargeTimes remainChargeTimes = (WebMsgDeployRemainChargeTimes) message;
//                    Console.WriteLine("生成帧：设置设备剩余充电次数: " + remainChargeTimes.getTimes());
//                    return tcpMsgBuilder.buildRemainChargeTimes(remainChargeTimes);
//
//                case MsgType.SERVER_SET_DEVICE_ID:
//                    WebMsgDeployEmployeeDeviceId deployEmployeeDeviceId = (WebMsgDeployEmployeeDeviceId) message;
//                    Console.WriteLine("生成帧：设置设备 ID: " + deployEmployeeDeviceId.getUpdatedDeviceId());
//                    return tcpMsgBuilder.buildDeviceId(deployEmployeeDeviceId);
//
//                case MsgType.SERVER_SET_EMPLOYEE_NAME:
//                    WebMsgDeployEmployeeName deployEmployeeName = (WebMsgDeployEmployeeName) message;
//                    Console.WriteLine("生成帧：设置员工姓名: " + deployEmployeeName.getValue());
//                    return tcpMsgBuilder.buildEmployeeName(deployEmployeeName);
//
//                case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
//                    WebMsgDeployEmployeeDepartment deployEmployeeDepartment = (WebMsgDeployEmployeeDepartment) message;
//                    Console.WriteLine("生成帧：设置员工单位: " + deployEmployeeDepartment.getValue());
//                    return tcpMsgBuilder.buildEmployeeDepartment(deployEmployeeDepartment);
//
//                case MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER:
//                    WebMsgDeployEmployeeCardNumber deployEmployeeCardNumber = (WebMsgDeployEmployeeCardNumber) message;
//                    Console.WriteLine("生成帧：设置员工卡号: " + deployEmployeeCardNumber.getCardNumber());
//                    return tcpMsgBuilder.buildEmployeeCardNumber(deployEmployeeCardNumber);

                case MsgType.ServerRemoteUnlock:
                    Console.WriteLine("生成帧：解锁单个设备");
                    return _tcpMsgBuilder.buildWebUnlock((WebMsgOperateBoxUnlock) message);

//                case MsgType.SERVER_SET_FREE_CARD_NUMBER:
//                    Console.WriteLine("生成帧：设置万能卡号");
//                    return tcpMsgBuilder.buildFreeCardNumber((WebMsgDeployFreeCardNumber) message);
//
//                case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_RESPONSE:
//                    WebMsgInitMarchConfirmCardResponse marchConfirmCard = (WebMsgInitMarchConfirmCardResponse) message;
//                    Console.WriteLine("生成初始化帧：匹配确认卡号状态：" + marchConfirmCard.isSuccessful());
//                    return tcpMsgBuilder.buildInitMarchConfirmCardSuccessful(marchConfirmCard);
//
//                case MsgType.INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE:
//                    WebMsgInitClearDeviceStatus clearDeviceStatus = (WebMsgInitClearDeviceStatus) message;
//                    Console.WriteLine("生成帧：清除设备的初始化状态");
//                    return tcpMsgBuilder.buildClearDeviceStatus(clearDeviceStatus);

                default:
                    Console.WriteLine("未匹配功能位「" + message.getMsgId() + "」，无法生成 CAN 帧");
                    break;
            }
            return new byte[0];
        }
    }
}