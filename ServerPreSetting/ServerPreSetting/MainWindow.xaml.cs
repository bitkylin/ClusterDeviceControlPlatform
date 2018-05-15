using System;
using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Input;
using Newtonsoft.Json;

namespace ServerPreSetting
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow
    {
        private const string FILE_PATH = "setting";
        private readonly Encoding UTF8_ENCODING = new UTF8Encoding(false);

        public MainWindow()
        {
            InitializeComponent();
        }

        private void Window_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            DragMove();
        }

        private void buttonCancel_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            readSettingByFile(FILE_PATH);
            EnableDatabaseAuth(checkBoxDatabaseAuth.IsChecked.GetValueOrDefault(false));
        }

        private void readSettingByFile(string filePath)
        {
            KySetting kySetting = null;

            try
            {
                var streamReader = new StreamReader(filePath, UTF8_ENCODING);
                var str = streamReader.ReadToEnd();
                streamReader.Close();
                kySetting = JsonConvert.DeserializeObject<KySetting>(str);
            }
            catch (FileNotFoundException)
            {
                MessageBox.Show("未找到服务器的配置文件，已重新生成缺省配置文件", "提示");
            }
            catch (JsonSerializationException)
            {
                MessageBox.Show("服务器配置文件被非法修改，已重新生成缺省配置文件", "警告");
            }
            if (kySetting == null)
            {
                kySetting = new KySetting();
                writeSettingToFile(filePath, kySetting);
            }
            textBoxDefaultCardNumber.Text = kySetting.员工默认卡号;
            textBoxDefaultDepartment.Text = kySetting.员工默认部门;
            textBoxDefaultName.Text = kySetting.员工默认姓名;
            textBoxFrameSpace.Text = kySetting.帧发送间隔.ToString();
            textBoxMaxResendTimes.Text = kySetting.检错重发最大重复次数.ToString();
            textBoxDbServerAddress.Text = kySetting.数据库服务器的主机名或IP;
            textBoxRemainChargeTimes.Text = kySetting.部署剩余充电次数阈值.ToString();
            checkBoxMsgReply.IsChecked = kySetting.帧送达监测;
            checkBoxDebug.IsChecked = kySetting.调试模式;
            checkBoxWebRandom.IsChecked = kySetting.随机Web数据模式;
            checkBoxDatabaseAuth.IsChecked = kySetting.数据库认证模式;
            textboxTcpPort.Text = kySetting.服务器端口号.ToString();
            textBoxNoResponseTime.Text = kySetting.通道未响应时间.ToString();
            checkBoxNoResponse.IsChecked = kySetting.通道无响应监测;
            textBoxDatabaseUsername.Text = kySetting.数据库用户名.ToString();
            textBoxDatabasePassword.Text = kySetting.数据库密码.ToString();
            textBoxActivationCode.Text = kySetting.授权码.ToString();
        }

        private void writeSettingToFile(string filePath, KySetting kySetting)
        {
            var streamWriter = new StreamWriter(filePath, false, UTF8_ENCODING);
            var strSerial = JsonConvert.SerializeObject(kySetting, Formatting.Indented);
            streamWriter.Write(strSerial);
            streamWriter.Close();
        }

        private void buttonConfirm_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                var kySetting = new KySetting();
                kySetting.员工默认卡号 = textBoxDefaultCardNumber.Text.Trim();
                kySetting.员工默认部门 = textBoxDefaultDepartment.Text.Trim();
                kySetting.员工默认姓名 = textBoxDefaultName.Text.Trim();
                kySetting.帧发送间隔 = int.Parse(textBoxFrameSpace.Text.Trim());
                kySetting.检错重发最大重复次数 = int.Parse(textBoxMaxResendTimes.Text.Trim());
                kySetting.部署剩余充电次数阈值 = int.Parse(textBoxRemainChargeTimes.Text.Trim());

                kySetting.帧送达监测 = checkBoxMsgReply.IsChecked.GetValueOrDefault(true);
                kySetting.调试模式 = checkBoxDebug.IsChecked.GetValueOrDefault(false);
                kySetting.随机Web数据模式 = checkBoxWebRandom.IsChecked.GetValueOrDefault(false);
                kySetting.数据库认证模式 = checkBoxDatabaseAuth.IsChecked.GetValueOrDefault(false);

                kySetting.服务器端口号 = int.Parse(textboxTcpPort.Text.Trim());
                kySetting.通道未响应时间 = int.Parse(textBoxNoResponseTime.Text.Trim());
                kySetting.通道无响应监测 = checkBoxNoResponse.IsChecked.GetValueOrDefault(true);

                kySetting.数据库服务器的主机名或IP = textBoxDbServerAddress.Text.Trim();
                kySetting.数据库用户名 = textBoxDatabaseUsername.Text.Trim();
                kySetting.数据库密码 = textBoxDatabasePassword.Text.Trim();

                kySetting.授权码 = textBoxActivationCode.Text.Trim();

                writeSettingToFile(FILE_PATH, kySetting);
                MessageBox.Show("配置文件保存成功！\n请重启服务器应用程序以使配置生效", "提示");
                Close();
            }
            catch (FormatException)
            {
                MessageBox.Show(" 数据填写有误，请修改并重新确认！", "警告");
            }
        }

        private void checkBoxDatabaseAuth_Click(object sender, RoutedEventArgs e)
        {
            EnableDatabaseAuth(checkBoxDatabaseAuth.IsChecked.GetValueOrDefault(false));
        }

        /// <summary>
        /// 根据是否启动数据库认证，更改UI布局
        /// </summary>
        private void EnableDatabaseAuth(bool isEnabled)
        {
            textBoxDatabaseUsername.IsEnabled = isEnabled;
            textBoxDatabasePassword.IsEnabled = isEnabled;
        }

        void OnGotKeyboardFocus(object sender, KeyboardFocusChangedEventArgs e)
        {
            var textBox = sender as System.Windows.Controls.TextBox;

            if (textBox != null && !textBox.IsReadOnly && e.KeyboardDevice.IsKeyDown(Key.Tab))
                textBox.SelectAll();
        }
    }
}