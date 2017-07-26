namespace SocketServer.message.@base
{
    public class BaseMessage : IMessage
    {
        private int _msgId = -1;
        private int _groupId = -1;
        private int _deviceId = -1;

        protected BaseMessage(int groupId)
        {
            this._groupId = groupId;
        }

        protected BaseMessage(int groupId, int deviceId) : this(groupId)
        {
            this._deviceId = deviceId;
        }

        public int getMsgId()
        {
            return _msgId;
        }

        protected void setMsgId(int msgId)
        {
            this._msgId = msgId;
        }

        public int getGroupId()
        {
            return _groupId;
        }

        public void setGroupId(int groupId)
        {
            this._groupId = groupId;
        }


        public int getDeviceId()
        {
            return _deviceId;
        }


        public void setDeviceId(int deviceId)
        {
            this._deviceId = deviceId;
        }
    }
}