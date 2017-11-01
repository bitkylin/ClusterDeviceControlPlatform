namespace SocketServer
{
    partial class FormSocketServer
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要修改
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox8 = new System.Windows.Forms.GroupBox();
            this.btnClearControlText = new System.Windows.Forms.Button();
            this.btnClearCommunicateText = new System.Windows.Forms.Button();
            this.groupBox7 = new System.Windows.Forms.GroupBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.comboBaudrate = new System.Windows.Forms.ComboBox();
            this.comboPortName = new System.Windows.Forms.ComboBox();
            this.btnSerialPortOpenClose = new System.Windows.Forms.Button();
            this.groupBox5 = new System.Windows.Forms.GroupBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.labelServerStartOrEnd = new System.Windows.Forms.Label();
            this.MessageIPAndPort = new System.Windows.Forms.Label();
            this.groupBox6 = new System.Windows.Forms.GroupBox();
            this.lstControlText = new System.Windows.Forms.ListBox();
            this.lstServerCommunicationText = new System.Windows.Forms.ListBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.btnUpdateDeviceId = new System.Windows.Forms.Button();
            this.LabelUpdateDeviceId = new System.Windows.Forms.Label();
            this.labelUpdateChargeStatus = new System.Windows.Forms.Label();
            this.btnUpdateChargeStatus = new System.Windows.Forms.Button();
            this.labelUpdateUnLock = new System.Windows.Forms.Label();
            this.btnUpdateUnLock = new System.Windows.Forms.Button();
            this.labelUpdateName = new System.Windows.Forms.Label();
            this.btnUpdateName = new System.Windows.Forms.Button();
            this.labelUpdateDepartment = new System.Windows.Forms.Label();
            this.btnUpdateDepartment = new System.Windows.Forms.Button();
            this.labelUpdateCardNumber = new System.Windows.Forms.Label();
            this.btnUpdateCardNumber = new System.Windows.Forms.Button();
            this.labelUpdateFreeCard = new System.Windows.Forms.Label();
            this.btnUpdateFreeCard = new System.Windows.Forms.Button();
            this.labelUpdateDepartment2 = new System.Windows.Forms.Label();
            this.btnUpdateDepartment2 = new System.Windows.Forms.Button();
            this.labelRemainTimes = new System.Windows.Forms.Label();
            this.btnRemainTimes = new System.Windows.Forms.Button();
            this.labelRemainTimes2 = new System.Windows.Forms.Label();
            this.btnRemainTimes2 = new System.Windows.Forms.Button();
            this.groupBox1.SuspendLayout();
            this.groupBox8.SuspendLayout();
            this.groupBox7.SuspendLayout();
            this.groupBox5.SuspendLayout();
            this.groupBox6.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.groupBox8);
            this.groupBox1.Controls.Add(this.groupBox7);
            this.groupBox1.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.groupBox1.Location = new System.Drawing.Point(12, 12);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(224, 398);
            this.groupBox1.TabIndex = 2;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "功能区";
            // 
            // groupBox8
            // 
            this.groupBox8.Controls.Add(this.btnClearControlText);
            this.groupBox8.Controls.Add(this.btnClearCommunicateText);
            this.groupBox8.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.groupBox8.Location = new System.Drawing.Point(6, 172);
            this.groupBox8.Name = "groupBox8";
            this.groupBox8.Size = new System.Drawing.Size(209, 112);
            this.groupBox8.TabIndex = 4;
            this.groupBox8.TabStop = false;
            this.groupBox8.Text = "信息清空";
            // 
            // btnClearControlText
            // 
            this.btnClearControlText.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnClearControlText.Location = new System.Drawing.Point(82, 66);
            this.btnClearControlText.Name = "btnClearControlText";
            this.btnClearControlText.Size = new System.Drawing.Size(110, 35);
            this.btnClearControlText.TabIndex = 1;
            this.btnClearControlText.Text = "清空控制信息";
            this.btnClearControlText.UseVisualStyleBackColor = true;
            this.btnClearControlText.Click += new System.EventHandler(this.btnClearControlText_Click);
            // 
            // btnClearCommunicateText
            // 
            this.btnClearCommunicateText.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnClearCommunicateText.Location = new System.Drawing.Point(82, 25);
            this.btnClearCommunicateText.Name = "btnClearCommunicateText";
            this.btnClearCommunicateText.Size = new System.Drawing.Size(110, 35);
            this.btnClearCommunicateText.TabIndex = 2;
            this.btnClearCommunicateText.Text = "清空通信信息";
            this.btnClearCommunicateText.UseVisualStyleBackColor = true;
            this.btnClearCommunicateText.Click += new System.EventHandler(this.btnClearText_Click);
            // 
            // groupBox7
            // 
            this.groupBox7.Controls.Add(this.label3);
            this.groupBox7.Controls.Add(this.label1);
            this.groupBox7.Controls.Add(this.comboBaudrate);
            this.groupBox7.Controls.Add(this.comboPortName);
            this.groupBox7.Controls.Add(this.btnSerialPortOpenClose);
            this.groupBox7.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.groupBox7.Location = new System.Drawing.Point(6, 26);
            this.groupBox7.Name = "groupBox7";
            this.groupBox7.Size = new System.Drawing.Size(209, 140);
            this.groupBox7.TabIndex = 4;
            this.groupBox7.TabStop = false;
            this.groupBox7.Text = "串口控制";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label3.Location = new System.Drawing.Point(5, 64);
            this.label3.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(69, 20);
            this.label3.TabIndex = 8;
            this.label3.Text = "波特率：";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label1.Location = new System.Drawing.Point(5, 28);
            this.label1.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(69, 20);
            this.label1.TabIndex = 7;
            this.label1.Text = "串口名：";
            // 
            // comboBaudrate
            // 
            this.comboBaudrate.BackColor = System.Drawing.Color.White;
            this.comboBaudrate.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBaudrate.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.comboBaudrate.FormattingEnabled = true;
            this.comboBaudrate.Items.AddRange(new object[] {
            "2400",
            "4800",
            "9600",
            "19200",
            "38400",
            "57600",
            "115200"});
            this.comboBaudrate.Location = new System.Drawing.Point(82, 61);
            this.comboBaudrate.Margin = new System.Windows.Forms.Padding(4);
            this.comboBaudrate.Name = "comboBaudrate";
            this.comboBaudrate.Size = new System.Drawing.Size(110, 28);
            this.comboBaudrate.TabIndex = 6;
            // 
            // comboPortName
            // 
            this.comboPortName.BackColor = System.Drawing.Color.White;
            this.comboPortName.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboPortName.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.comboPortName.FormattingEnabled = true;
            this.comboPortName.Location = new System.Drawing.Point(82, 25);
            this.comboPortName.Margin = new System.Windows.Forms.Padding(4);
            this.comboPortName.Name = "comboPortName";
            this.comboPortName.Size = new System.Drawing.Size(110, 28);
            this.comboPortName.TabIndex = 5;
            // 
            // btnSerialPortOpenClose
            // 
            this.btnSerialPortOpenClose.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnSerialPortOpenClose.Location = new System.Drawing.Point(95, 99);
            this.btnSerialPortOpenClose.Name = "btnSerialPortOpenClose";
            this.btnSerialPortOpenClose.Size = new System.Drawing.Size(97, 35);
            this.btnSerialPortOpenClose.TabIndex = 1;
            this.btnSerialPortOpenClose.Text = "打开串口";
            this.btnSerialPortOpenClose.UseVisualStyleBackColor = true;
            this.btnSerialPortOpenClose.Click += new System.EventHandler(this.buttonOpenClose_Click);
            // 
            // groupBox5
            // 
            this.groupBox5.Controls.Add(this.label4);
            this.groupBox5.Controls.Add(this.label2);
            this.groupBox5.Controls.Add(this.labelServerStartOrEnd);
            this.groupBox5.Controls.Add(this.MessageIPAndPort);
            this.groupBox5.Location = new System.Drawing.Point(12, 313);
            this.groupBox5.Name = "groupBox5";
            this.groupBox5.Size = new System.Drawing.Size(215, 90);
            this.groupBox5.TabIndex = 4;
            this.groupBox5.TabStop = false;
            this.groupBox5.Text = "信息";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label4.Location = new System.Drawing.Point(6, 23);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(84, 20);
            this.label4.TabIndex = 3;
            this.label4.Text = "服务状态：";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label2.Location = new System.Drawing.Point(8, 53);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(69, 20);
            this.label2.TabIndex = 1;
            this.label2.Text = "客户端：";
            // 
            // labelServerStartOrEnd
            // 
            this.labelServerStartOrEnd.AutoSize = true;
            this.labelServerStartOrEnd.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelServerStartOrEnd.Location = new System.Drawing.Point(96, 23);
            this.labelServerStartOrEnd.Name = "labelServerStartOrEnd";
            this.labelServerStartOrEnd.Size = new System.Drawing.Size(84, 20);
            this.labelServerStartOrEnd.TabIndex = 2;
            this.labelServerStartOrEnd.Text = "服务未开启";
            // 
            // MessageIPAndPort
            // 
            this.MessageIPAndPort.AutoSize = true;
            this.MessageIPAndPort.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.MessageIPAndPort.Location = new System.Drawing.Point(96, 53);
            this.MessageIPAndPort.Name = "MessageIPAndPort";
            this.MessageIPAndPort.Size = new System.Drawing.Size(84, 20);
            this.MessageIPAndPort.TabIndex = 0;
            this.MessageIPAndPort.Text = "无状态信息";
            // 
            // groupBox6
            // 
            this.groupBox6.Controls.Add(this.lstControlText);
            this.groupBox6.Location = new System.Drawing.Point(12, 416);
            this.groupBox6.Name = "groupBox6";
            this.groupBox6.Size = new System.Drawing.Size(345, 125);
            this.groupBox6.TabIndex = 4;
            this.groupBox6.TabStop = false;
            this.groupBox6.Text = "控制信息";
            // 
            // lstControlText
            // 
            this.lstControlText.FormattingEnabled = true;
            this.lstControlText.HorizontalScrollbar = true;
            this.lstControlText.ItemHeight = 20;
            this.lstControlText.Location = new System.Drawing.Point(6, 26);
            this.lstControlText.Name = "lstControlText";
            this.lstControlText.Size = new System.Drawing.Size(333, 124);
            this.lstControlText.TabIndex = 0;
            // 
            // lstServerCommunicationText
            // 
            this.lstServerCommunicationText.FormattingEnabled = true;
            this.lstServerCommunicationText.HorizontalScrollbar = true;
            this.lstServerCommunicationText.ItemHeight = 20;
            this.lstServerCommunicationText.Location = new System.Drawing.Point(6, 26);
            this.lstServerCommunicationText.Name = "lstServerCommunicationText";
            this.lstServerCommunicationText.Size = new System.Drawing.Size(365, 624);
            this.lstServerCommunicationText.TabIndex = 0;
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.lstServerCommunicationText);
            this.groupBox2.Location = new System.Drawing.Point(443, 12);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(377, 529);
            this.groupBox2.TabIndex = 3;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "通信信息";
            // 
            // btnUpdateDeviceId
            // 
            this.btnUpdateDeviceId.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateDeviceId.Location = new System.Drawing.Point(242, 26);
            this.btnUpdateDeviceId.Name = "btnUpdateDeviceId";
            this.btnUpdateDeviceId.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateDeviceId.TabIndex = 1;
            this.btnUpdateDeviceId.Text = "更换柜号";
            this.btnUpdateDeviceId.UseVisualStyleBackColor = true;
            this.btnUpdateDeviceId.Click += new System.EventHandler(this.btnUpdateDeviceId_Click);
            // 
            // LabelUpdateDeviceId
            // 
            this.LabelUpdateDeviceId.AutoSize = true;
            this.LabelUpdateDeviceId.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.LabelUpdateDeviceId.Location = new System.Drawing.Point(363, 32);
            this.LabelUpdateDeviceId.Name = "LabelUpdateDeviceId";
            this.LabelUpdateDeviceId.Size = new System.Drawing.Size(33, 20);
            this.LabelUpdateDeviceId.TabIndex = 2;
            this.LabelUpdateDeviceId.Text = "      ";
            // 
            // labelUpdateChargeStatus
            // 
            this.labelUpdateChargeStatus.AutoSize = true;
            this.labelUpdateChargeStatus.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelUpdateChargeStatus.Location = new System.Drawing.Point(363, 383);
            this.labelUpdateChargeStatus.Name = "labelUpdateChargeStatus";
            this.labelUpdateChargeStatus.Size = new System.Drawing.Size(33, 20);
            this.labelUpdateChargeStatus.TabIndex = 2;
            this.labelUpdateChargeStatus.Text = "      ";
            // 
            // btnUpdateChargeStatus
            // 
            this.btnUpdateChargeStatus.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateChargeStatus.Location = new System.Drawing.Point(242, 377);
            this.btnUpdateChargeStatus.Name = "btnUpdateChargeStatus";
            this.btnUpdateChargeStatus.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateChargeStatus.TabIndex = 1;
            this.btnUpdateChargeStatus.Text = "获取充电状态";
            this.btnUpdateChargeStatus.UseVisualStyleBackColor = true;
            this.btnUpdateChargeStatus.Click += new System.EventHandler(this.btnUpdateChargeStatus_Click);
            // 
            // labelUpdateUnLock
            // 
            this.labelUpdateUnLock.AutoSize = true;
            this.labelUpdateUnLock.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelUpdateUnLock.Location = new System.Drawing.Point(363, 344);
            this.labelUpdateUnLock.Name = "labelUpdateUnLock";
            this.labelUpdateUnLock.Size = new System.Drawing.Size(33, 20);
            this.labelUpdateUnLock.TabIndex = 2;
            this.labelUpdateUnLock.Text = "      ";
            // 
            // btnUpdateUnLock
            // 
            this.btnUpdateUnLock.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateUnLock.Location = new System.Drawing.Point(242, 338);
            this.btnUpdateUnLock.Name = "btnUpdateUnLock";
            this.btnUpdateUnLock.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateUnLock.TabIndex = 1;
            this.btnUpdateUnLock.Text = "远程开锁";
            this.btnUpdateUnLock.UseVisualStyleBackColor = true;
            this.btnUpdateUnLock.Click += new System.EventHandler(this.btnUpdateUnLock_Click);
            // 
            // labelUpdateName
            // 
            this.labelUpdateName.AutoSize = true;
            this.labelUpdateName.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelUpdateName.Location = new System.Drawing.Point(363, 71);
            this.labelUpdateName.Name = "labelUpdateName";
            this.labelUpdateName.Size = new System.Drawing.Size(33, 20);
            this.labelUpdateName.TabIndex = 2;
            this.labelUpdateName.Text = "      ";
            // 
            // btnUpdateName
            // 
            this.btnUpdateName.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateName.Location = new System.Drawing.Point(242, 65);
            this.btnUpdateName.Name = "btnUpdateName";
            this.btnUpdateName.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateName.TabIndex = 1;
            this.btnUpdateName.Text = "更新姓名";
            this.btnUpdateName.UseVisualStyleBackColor = true;
            this.btnUpdateName.Click += new System.EventHandler(this.btnUpdateName_Click);
            // 
            // labelUpdateDepartment
            // 
            this.labelUpdateDepartment.AutoSize = true;
            this.labelUpdateDepartment.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelUpdateDepartment.Location = new System.Drawing.Point(363, 110);
            this.labelUpdateDepartment.Name = "labelUpdateDepartment";
            this.labelUpdateDepartment.Size = new System.Drawing.Size(33, 20);
            this.labelUpdateDepartment.TabIndex = 2;
            this.labelUpdateDepartment.Text = "      ";
            // 
            // btnUpdateDepartment
            // 
            this.btnUpdateDepartment.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateDepartment.Location = new System.Drawing.Point(242, 104);
            this.btnUpdateDepartment.Name = "btnUpdateDepartment";
            this.btnUpdateDepartment.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateDepartment.TabIndex = 1;
            this.btnUpdateDepartment.Text = "更新单位1";
            this.btnUpdateDepartment.UseVisualStyleBackColor = true;
            this.btnUpdateDepartment.Click += new System.EventHandler(this.btnUpdateDepartment_Click);
            // 
            // labelUpdateCardNumber
            // 
            this.labelUpdateCardNumber.AutoSize = true;
            this.labelUpdateCardNumber.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelUpdateCardNumber.Location = new System.Drawing.Point(363, 188);
            this.labelUpdateCardNumber.Name = "labelUpdateCardNumber";
            this.labelUpdateCardNumber.Size = new System.Drawing.Size(33, 20);
            this.labelUpdateCardNumber.TabIndex = 2;
            this.labelUpdateCardNumber.Text = "      ";
            // 
            // btnUpdateCardNumber
            // 
            this.btnUpdateCardNumber.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateCardNumber.Location = new System.Drawing.Point(242, 182);
            this.btnUpdateCardNumber.Name = "btnUpdateCardNumber";
            this.btnUpdateCardNumber.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateCardNumber.TabIndex = 1;
            this.btnUpdateCardNumber.Text = "更新卡号";
            this.btnUpdateCardNumber.UseVisualStyleBackColor = true;
            this.btnUpdateCardNumber.Click += new System.EventHandler(this.btnUpdateCardNumber_Click);
            // 
            // labelUpdateFreeCard
            // 
            this.labelUpdateFreeCard.AutoSize = true;
            this.labelUpdateFreeCard.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelUpdateFreeCard.Location = new System.Drawing.Point(363, 227);
            this.labelUpdateFreeCard.Name = "labelUpdateFreeCard";
            this.labelUpdateFreeCard.Size = new System.Drawing.Size(33, 20);
            this.labelUpdateFreeCard.TabIndex = 2;
            this.labelUpdateFreeCard.Text = "      ";
            // 
            // btnUpdateFreeCard
            // 
            this.btnUpdateFreeCard.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateFreeCard.Location = new System.Drawing.Point(242, 221);
            this.btnUpdateFreeCard.Name = "btnUpdateFreeCard";
            this.btnUpdateFreeCard.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateFreeCard.TabIndex = 1;
            this.btnUpdateFreeCard.Text = "更新万能卡号";
            this.btnUpdateFreeCard.UseVisualStyleBackColor = true;
            this.btnUpdateFreeCard.Click += new System.EventHandler(this.btnUpdateFreeCard_Click);
            // 
            // labelUpdateDepartment2
            // 
            this.labelUpdateDepartment2.AutoSize = true;
            this.labelUpdateDepartment2.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelUpdateDepartment2.Location = new System.Drawing.Point(363, 149);
            this.labelUpdateDepartment2.Name = "labelUpdateDepartment2";
            this.labelUpdateDepartment2.Size = new System.Drawing.Size(33, 20);
            this.labelUpdateDepartment2.TabIndex = 2;
            this.labelUpdateDepartment2.Text = "      ";
            // 
            // btnUpdateDepartment2
            // 
            this.btnUpdateDepartment2.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnUpdateDepartment2.Location = new System.Drawing.Point(242, 143);
            this.btnUpdateDepartment2.Name = "btnUpdateDepartment2";
            this.btnUpdateDepartment2.Size = new System.Drawing.Size(115, 33);
            this.btnUpdateDepartment2.TabIndex = 1;
            this.btnUpdateDepartment2.Text = "更新单位2";
            this.btnUpdateDepartment2.UseVisualStyleBackColor = true;
            this.btnUpdateDepartment2.Click += new System.EventHandler(this.btnUpdateDepartment2_Click);
            // 
            // labelRemainTimes
            // 
            this.labelRemainTimes.AutoSize = true;
            this.labelRemainTimes.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelRemainTimes.Location = new System.Drawing.Point(363, 266);
            this.labelRemainTimes.Name = "labelRemainTimes";
            this.labelRemainTimes.Size = new System.Drawing.Size(33, 20);
            this.labelRemainTimes.TabIndex = 2;
            this.labelRemainTimes.Text = "      ";
            // 
            // btnRemainTimes
            // 
            this.btnRemainTimes.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnRemainTimes.Location = new System.Drawing.Point(242, 260);
            this.btnRemainTimes.Name = "btnRemainTimes";
            this.btnRemainTimes.Size = new System.Drawing.Size(115, 33);
            this.btnRemainTimes.TabIndex = 1;
            this.btnRemainTimes.Text = "充电次数17";
            this.btnRemainTimes.UseVisualStyleBackColor = true;
            this.btnRemainTimes.Click += new System.EventHandler(this.btnRemainTimes_Click);
            // 
            // labelRemainTimes2
            // 
            this.labelRemainTimes2.AutoSize = true;
            this.labelRemainTimes2.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelRemainTimes2.Location = new System.Drawing.Point(363, 305);
            this.labelRemainTimes2.Name = "labelRemainTimes2";
            this.labelRemainTimes2.Size = new System.Drawing.Size(33, 20);
            this.labelRemainTimes2.TabIndex = 2;
            this.labelRemainTimes2.Text = "      ";
            // 
            // btnRemainTimes2
            // 
            this.btnRemainTimes2.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnRemainTimes2.Location = new System.Drawing.Point(242, 299);
            this.btnRemainTimes2.Name = "btnRemainTimes2";
            this.btnRemainTimes2.Size = new System.Drawing.Size(115, 33);
            this.btnRemainTimes2.TabIndex = 1;
            this.btnRemainTimes2.Text = "充电次数88";
            this.btnRemainTimes2.UseVisualStyleBackColor = true;
            this.btnRemainTimes2.Click += new System.EventHandler(this.btnRemainTimes2_Click);
            // 
            // FormSocketServer
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(120F, 120F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoSize = true;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.ClientSize = new System.Drawing.Size(832, 553);
            this.Controls.Add(this.groupBox6);
            this.Controls.Add(this.btnUpdateUnLock);
            this.Controls.Add(this.groupBox5);
            this.Controls.Add(this.labelUpdateUnLock);
            this.Controls.Add(this.btnUpdateName);
            this.Controls.Add(this.labelUpdateName);
            this.Controls.Add(this.btnRemainTimes2);
            this.Controls.Add(this.labelRemainTimes2);
            this.Controls.Add(this.btnRemainTimes);
            this.Controls.Add(this.labelRemainTimes);
            this.Controls.Add(this.btnUpdateFreeCard);
            this.Controls.Add(this.labelUpdateFreeCard);
            this.Controls.Add(this.btnUpdateCardNumber);
            this.Controls.Add(this.labelUpdateCardNumber);
            this.Controls.Add(this.btnUpdateDepartment2);
            this.Controls.Add(this.labelUpdateDepartment2);
            this.Controls.Add(this.btnUpdateDepartment);
            this.Controls.Add(this.labelUpdateDepartment);
            this.Controls.Add(this.btnUpdateChargeStatus);
            this.Controls.Add(this.labelUpdateChargeStatus);
            this.Controls.Add(this.btnUpdateDeviceId);
            this.Controls.Add(this.LabelUpdateDeviceId);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.MaximizeBox = false;
            this.Name = "FormSocketServer";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "充电柜单板测试";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.groupBox1.ResumeLayout(false);
            this.groupBox8.ResumeLayout(false);
            this.groupBox7.ResumeLayout(false);
            this.groupBox7.PerformLayout();
            this.groupBox5.ResumeLayout(false);
            this.groupBox5.PerformLayout();
            this.groupBox6.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label labelServerStartOrEnd;
        private System.Windows.Forms.GroupBox groupBox5;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label MessageIPAndPort;
        private System.Windows.Forms.GroupBox groupBox6;
        private System.Windows.Forms.ListBox lstControlText;
        private System.Windows.Forms.GroupBox groupBox7;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox comboBaudrate;
        private System.Windows.Forms.ComboBox comboPortName;
        private System.Windows.Forms.Button btnSerialPortOpenClose;
        private System.Windows.Forms.Button btnClearCommunicateText;
        private System.Windows.Forms.ListBox lstServerCommunicationText;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.GroupBox groupBox8;
        private System.Windows.Forms.Button btnClearControlText;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button btnUpdateDeviceId;
        private System.Windows.Forms.Label LabelUpdateDeviceId;
        private System.Windows.Forms.Label labelUpdateChargeStatus;
        private System.Windows.Forms.Button btnUpdateChargeStatus;
        private System.Windows.Forms.Label labelUpdateUnLock;
        private System.Windows.Forms.Button btnUpdateUnLock;
        private System.Windows.Forms.Label labelUpdateName;
        private System.Windows.Forms.Button btnUpdateName;
        private System.Windows.Forms.Label labelUpdateDepartment;
        private System.Windows.Forms.Button btnUpdateDepartment;
        private System.Windows.Forms.Label labelUpdateCardNumber;
        private System.Windows.Forms.Button btnUpdateCardNumber;
        private System.Windows.Forms.Label labelUpdateFreeCard;
        private System.Windows.Forms.Button btnUpdateFreeCard;
        private System.Windows.Forms.Label labelUpdateDepartment2;
        private System.Windows.Forms.Button btnUpdateDepartment2;
        private System.Windows.Forms.Label labelRemainTimes;
        private System.Windows.Forms.Button btnRemainTimes;
        private System.Windows.Forms.Label labelRemainTimes2;
        private System.Windows.Forms.Button btnRemainTimes2;
    }
}

