using System;
using System.Collections.Generic;
using System.IO.Ports;
using System.Text;
using System.Threading;
using System.Windows.Forms;

namespace SocketServer
{
    public partial class FormSocketServer : Form
    {
        //串口设置
        private SerialPort _comm = new SerialPort();

        private readonly StringBuilder _builder = new StringBuilder(); //避免在事件处理方法中反复的创建，定义到外面。

        public FormSocketServer()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            CheckForIllegalCrossThreadCalls = false;
            //初始化下拉串口名称列表框
            var ports = SerialPort.GetPortNames();
            Array.Sort(ports);
            comboPortName.Items.AddRange(ports);
            comboPortName.SelectedIndex = comboPortName.Items.Count > 0 ? 0 : -1;
            comboBaudrate.SelectedIndex = comboBaudrate.Items.IndexOf("115200");
            //初始化SerialPort对象
            _comm.NewLine = "\r\n";
            _comm.RtsEnable = true; //根据实际情况吧。

            //添加事件注册
            _comm.DataReceived += comm_DataReceived;
        }

        private void comm_DataReceived(object sender, SerialDataReceivedEventArgs e)
        {
            Thread.Sleep(10);
            var n = _comm.BytesToRead; //先记录下来，避免某种原因，人为的原因，操作几次之间时间长，缓存不一致
            var buf = new byte[n]; //声明一个临时数组存储当前来的串口数据
            _comm.Read(buf, 0, n); //读取缓冲数据
            _builder.Clear(); //清除字符串构造器的内容
            //因为要访问ui资源，所以需要使用invoke方式同步ui。
            Invoke((EventHandler) (delegate
            {
                var strings = new string[13];
                for (int i = 0; i < buf.Length; i++)
                {
                    var s = Convert.ToString(buf[i], 16);
                    if (s.Length == 1) s = "0" + s;
                    if (i < 13)
                    {
                        strings[i] = s;
                    }
                    _builder.Append(s + " ");
                }
                //追加的形式添加到文本框末端，并滚动到最后。
                communicateMessageShow(_builder.ToString());

                successReceiveFrame(strings);
            }));
        }

        private int _countDeviceId;
        private int _countName;
        private int _countDepartment;
        private int _countDepartment2;
        private int _countCardNumber;
        private int _countFreeCard;
        private int _countRemainTimes;
        private int _countRemainTimes2;
        private int _countUnLock;
        private int _countChargeStatus;

        private void successReceiveFrame(IList<string> strs)
        {
            var frameType = strs[1];

            if ("45".Equals(frameType)) updateLabel(LabelUpdateDeviceId, ref _countDeviceId);
            if ("46".Equals(frameType)) updateLabel(labelUpdateName, ref _countName);
            if ("47".Equals(frameType)) updateLabel(labelUpdateDepartment, ref _countDepartment);
            if ("48".Equals(frameType)) updateLabel(labelUpdateDepartment2, ref _countDepartment2);
            if ("49".Equals(frameType)) updateLabel(labelUpdateCardNumber, ref _countCardNumber);
            if ("80".Equals(frameType)) updateLabel(labelUpdateFreeCard, ref _countFreeCard);
            if ("41".Equals(frameType))
            {
                if (_countRemainTimes <= _countRemainTimes2)
                    updateLabel(labelRemainTimes, ref _countRemainTimes);
                else
                    updateLabel(labelRemainTimes2, ref _countRemainTimes2);
            }
            if ("4a".Equals(frameType)) updateLabel(labelUpdateUnLock, ref _countUnLock);

            if ("40".Equals(frameType))
            {
                var type = strs[3];
                if ("01".Equals(type)) updateLabel(labelUpdateChargeStatus, ref _countChargeStatus, "使用中");
                if ("02".Equals(type)) updateLabel(labelUpdateChargeStatus, ref _countChargeStatus, "充电中");
                if ("03".Equals(type)) updateLabel(labelUpdateChargeStatus, ref _countChargeStatus, "已充满");
                if ("04".Equals(type)) updateLabel(labelUpdateChargeStatus, ref _countChargeStatus, "已过流");
                if ("06".Equals(type)) updateLabel(labelUpdateChargeStatus, ref _countChargeStatus, "未放好");
                if ("50".Equals(type)) updateLabel(labelUpdateChargeStatus, ref _countChargeStatus, "严重故障");
            }
        }


        private void updateLabel(Control label, ref int value, string extra = null)
        {
            if (extra == null)
            {
                label.Text = @"成功「" + ++value + @"」";
            }
            else
            {
                label.Text = @"成功「" + ++value + @"」" +"\r\n"+ extra;
            }
        }

        private void buttonOpenClose_Click(object sender, EventArgs e)
        {
            //根据当前串口对象，来判断操作
            if (_comm.IsOpen)
            {
                //打开时点击，则关闭串口
                _comm.Close();
                labelServerStartOrEnd.Text = @"串口已关闭";
                controlMessageShow(@"串口已关闭");
                MessageIPAndPort.Text = @"无状态信息";
            }
            else
            {
                //关闭时点击，则设置好端口，波特率后打开
                _comm.PortName = comboPortName.Text;
                _comm.BaudRate = int.Parse(comboBaudrate.Text);
                try
                {
                    _comm.Open();
                    labelServerStartOrEnd.Text = @"串口已打开";
                    controlMessageShow(@"串口已打开");
                    MessageIPAndPort.Text = comboPortName.SelectedItem.ToString();
                }
                catch (Exception ex)
                {
                    //捕获到异常信息，创建一个新的comm对象，之前的不能用了。
                    _comm = new SerialPort();
                    //现实异常信息给客户。
                    MessageBox.Show(ex.Message);
                }
            }
            //设置按钮的状态
            btnSerialPortOpenClose.Text = _comm.IsOpen ? @"关闭串口" : @"打开串口";
        }

        private void controlMessageShow(string message)
        {
            lstControlText.Items.Add(message);
            lstControlText.SetSelected(lstControlText.Items.Count - 1, true);
        }

        private void communicateMessageShow(string message)
        {
            lstServerCommunicationText.Items.Add(message);
            lstServerCommunicationText.SetSelected(lstServerCommunicationText.Items.Count - 1, true);
        }

        private void btnClearText_Click(object sender, EventArgs e)
        {
            _countDeviceId = 0;
            _countName = 0;
            _countDepartment = 0;
            _countDepartment2 = 0;
            _countCardNumber = 0;
            _countFreeCard = 0;
            _countRemainTimes = 0;
            _countRemainTimes2 = 0;
            _countUnLock = 0;
            _countChargeStatus = 0;

            lstServerCommunicationText.Items.Clear();
            LabelUpdateDeviceId.Text = "";
            labelUpdateName.Text = "";
            labelUpdateDepartment.Text = "";
            labelUpdateDepartment2.Text = "";
            labelUpdateCardNumber.Text = "";
            labelUpdateFreeCard.Text = "";
            labelRemainTimes.Text = "";
            labelRemainTimes2.Text = "";
            labelUpdateUnLock.Text = "";
            labelUpdateChargeStatus.Text = "";
        }

        private void btnClearControlText_Click(object sender, EventArgs e)
        {
            lstControlText.Items.Clear();
        }

        private void btnUpdateUnLock_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 1a 00 00 00 00 00 00 00 00 00 b9";
            sendFrame(str);
        }

        private void btnUpdateDeviceId_Click(object sender, EventArgs e)
        {
            const string str = "fc 01 15 00 12 00 00 00 00 00 00 00 3c";
            sendFrame(str);
        }

        private void sendFrame(string str)
        {
            string[] strs = str.Split(' ');
            byte[] bytes = new byte[13];
            for (var i = 0; i < 13; i++)
            {
                bytes[i] = Convert.ToByte(strs[i], 16);
            }
            _comm.Write(bytes, 0, 13);
        }

        private void btnUpdateName_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 16 00 d0 d5 c3 fb 00 00 00 00 71";
            sendFrame(str);
        }

        private void btnUpdateDepartment_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 17 00 b5 a5 ce bb 00 00 00 00 78";
            sendFrame(str);
        }

        private void btnUpdateDepartment2_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 18 00 00 00 00 00 00 00 00 00 3c";
            sendFrame(str);
        }

        private void btnUpdateCardNumber_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 19 08 01 00 00 01 5F 91 9D 55 4e";
            sendFrame(str);
        }

        private void btnUpdateFreeCard_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 70 08 01 00 00 01 83 40 8C B9 d4";
            sendFrame(str);
        }

        private void btnRemainTimes_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 11 01 11 00 00 00 00 00 00 00 67";
            sendFrame(str);
        }

        private void btnRemainTimes2_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 11 01 58 00 00 00 00 00 00 00 47";
            sendFrame(str);
        }

        private void btnUpdateChargeStatus_Click(object sender, EventArgs e)
        {
            const string str = "fc 12 10 00 00 00 00 00 00 00 00 00 1a";
            sendFrame(str);
        }
    }
}