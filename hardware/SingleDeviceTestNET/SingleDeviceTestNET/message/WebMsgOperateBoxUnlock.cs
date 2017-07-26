using SocketServer.message.@base;
using SocketServer.utils;

namespace SocketServer.message
{
    public class WebMsgOperateBoxUnlock : BaseMessage
    {
        public WebMsgOperateBoxUnlock(int groupId, int boxId) : base(groupId, boxId)
        {
            setMsgId(MsgType.ServerRemoteUnlock);
        }
    }
}