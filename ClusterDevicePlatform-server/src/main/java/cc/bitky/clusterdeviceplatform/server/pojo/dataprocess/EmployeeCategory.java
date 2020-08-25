package cc.bitky.clusterdeviceplatform.server.pojo.dataprocess;

public class EmployeeCategory {
    private int chargeUsing;
    private int chargeFull;
    private int chargeCharging;
    private int chargeUninit;
    private int chargeOtherCharge;
    private int workNormal;
    private int workException;
    private int workChargingOutTime;
    private int workWorkingOutTime;

    public EmployeeCategory(int chargeUsing, int chargeFull, int chargeCharging, int chargeUninit, int chargeOtherCharge, int workNormal, int workException, int workChargingOutTime, int workWorkingOutTime) {
        this.chargeUsing = chargeUsing;
        this.chargeFull = chargeFull;
        this.chargeCharging = chargeCharging;
        this.chargeUninit = chargeUninit;
        this.chargeOtherCharge = chargeOtherCharge;
        this.workNormal = workNormal;
        this.workException = workException;
        this.workChargingOutTime = workChargingOutTime;
        this.workWorkingOutTime = workWorkingOutTime;
    }

    public int getChargeUsing() {
        return chargeUsing;
    }

    public int getChargeFull() {
        return chargeFull;
    }

    public int getChargeCharging() {
        return chargeCharging;
    }

    public int getChargeUninit() {
        return chargeUninit;
    }

    public int getChargeOtherCharge() {
        return chargeOtherCharge;
    }

    public int getWorkNormal() {
        return workNormal;
    }

    public int getWorkException() {
        return workException;
    }

    public int getWorkChargingOutTime() {
        return workChargingOutTime;
    }

    public int getWorkWorkingOutTime() {
        return workWorkingOutTime;
    }
}
