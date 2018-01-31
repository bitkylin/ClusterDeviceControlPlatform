package cc.bitky.clusterdeviceplatform.demo.server.statistic.info;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import cc.bitky.clusterdeviceplatform.demo.config.ServerSetting;


/**
 * 服务器当前状态信息
 */
public class ServerStatusInfo {
    /**
     * PID
     */
    String pid = ServerSetting.PID;
    /**
     * 服务器开机时间
     */
    String StartTime;
    /**
     * 服务器已运行时长
     */
    String runningTime;
    /**
     * 当前日期
     */
    String currentDate;
    /**
     * 当前时间
     */
    String currentTime;
    /**
     * 未处理消息数
     */
    long processedMsgCount;

    public ServerStatusInfo() {
        LocalDateTime start = ServerSetting.SYSTEM_START_DATE_TIME;
        StartTime = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ').split("\\.")[0];
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();
        currentDate = nowDate.format(DateTimeFormatter.ISO_DATE);
        currentTime = nowTime.format(DateTimeFormatter.ISO_TIME).split("\\.")[0];

        Duration duration = Duration.between(start, now);
        long temp = duration.getSeconds();
        long second = temp % 60;
        temp = temp / 60;
        long minutes = temp % 60;
        temp = temp / 60;
        long hour = temp % 24;
        long day = temp / 24;


        StringBuilder builder = new StringBuilder();

        if (day != 0) {
            builder.append(day).append("日");
        }
        builder.append(hour).append("时");
        builder.append(minutes).append("分");
        builder.append(second).append("秒");

        runningTime = builder.toString();
        processedMsgCount = 0;
    }

    public String getPid() {
        return pid;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public long getProcessedMsgCount() {
        return processedMsgCount;
    }
}
