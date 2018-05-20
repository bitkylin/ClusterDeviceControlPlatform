package cc.bitky.clusterdeviceplatform.serialrawdata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import cc.bitky.clusterdeviceplatform.serialrawdata.utils.DesUtil;
import cc.bitky.clusterdeviceplatform.serialrawdata.utils.EigestUtil;
import cc.bitky.clusterdeviceplatform.serialrawdata.utils.NetCardFactory;

public class LicenseVerifyPresenter {

    private static final Path PATH_DECRYPTED = Paths.get("./kyRawDataDecrypted");
    private static final Path PATH_RAW = Paths.get("./kyRawData");

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        if (args.length != 0 && "123lml123".equals(args[0])) {
            List<NetCard> netCardsRead = readRawData();
            if (netCardsRead == null) {
                System.out.println("原始码文件验证失败");
                return;
            }

            writeFile(JSON.toJSONString(netCardsRead, true), "kyRawDataDecrypted");
            System.out.println("是否生成授权码（Y/n）");
            String fun = scanner.nextLine().trim();
            if (!"y".equals(fun.toLowerCase()) && !"".equals(fun)) {
                return;
            }

            String strDecrypted = new String(Files.readAllBytes(PATH_DECRYPTED), StandardCharsets.UTF_8);
            Files.deleteIfExists(PATH_DECRYPTED);
            String licenseKey = createLicenseKey(JSONArray.parseArray(strDecrypted, NetCard.class));
            System.out.println("！！！已生成授权码！！！");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
            System.out.println(licenseKey);
            System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            scanner.nextLine();
            return;
        }

        while (true) {
            System.out.println("\n\n选择功能：");
            System.out.println("   1.生成原始码");
            System.out.println("   2.验证授权码");
            System.out.println("   3.关闭该程序");
            System.out.print("请输入:");
            String fun = scanner.nextLine();
            switch (fun) {
                case "1":
                    createRawData();
                    System.out.println("原始码文件「kyRawData」已生成，并保存当前目录下");
                    System.out.println("请提交该原始码文件，以换取授权码");
                    break;
                case "2":
                    System.out.println("请输入待验证的授权码：");
                    System.out.print("-> ");
                    String code = scanner.nextLine();
                    boolean result = verifyLicenseKey(code);
                    System.out.println("验证结果：" + result);
                    break;
                case "3":
                    System.exit(0);
                    break;
                default:
                    System.out.println("输入无效");
            }

        }
    }

    /**
     * 创建原始码文件
     */
    private static void createRawData() throws IOException {
        String str = JSON.toJSONString(NetCardFactory.createNetCards(), false);
        writeFile(DesUtil.encrypt(str), "kyRawData");
    }

    /**
     * 读取原始码文件
     *
     * @return 读取的 Mac 地址集合对象
     */
    private static List<NetCard> readRawData() throws IOException {
        String strings = new String(Files.readAllBytes(PATH_RAW), StandardCharsets.UTF_8);
        strings = DesUtil.decrypt(strings);

        return strings == null ? null : JSONArray.parseArray(strings, NetCard.class);
    }


    /**
     * 创建授权码
     * @param netCards  Mac 地址集合对象
     * @return 已创建的授权码
     */
    private static String createLicenseKey(List<NetCard> netCards) {
        StringBuilder stringBuilder = new StringBuilder();
        netCards.forEach(card -> stringBuilder.append(EigestUtil.strEigest(card.getMac())).append("!"));
        return stringBuilder.toString();
    }

    /**
     * 验证授权码
     *
     * @param str 待验证的授权码
     * @return 验证结果
     */
    public static boolean verifyLicenseKey(String str) throws SocketException {
        return LicenseKeyPool.createNetCardPool(NetCardFactory.createNetCards()).verifyLicenseKey(str);
    }

    /**
     * 将指定字符串写入指定文件
     *
     * @param str      指定字符串
     * @param fileName 指定文件名
     */
    private static void writeFile(String str, String fileName) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)) {
            writer.write(str);
        }
    }
}
