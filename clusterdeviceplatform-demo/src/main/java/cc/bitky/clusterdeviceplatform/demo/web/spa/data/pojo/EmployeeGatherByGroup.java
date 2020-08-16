package cc.bitky.clusterdeviceplatform.demo.web.spa.data.pojo;

public class EmployeeGatherByGroup {
    /**
     * 设备组 ID
     */
    private int groupId;
    /**
     * 总人数
     */
    private int population;
    /**
     * 分类
     */
    private EmployeeCategory category;

    public EmployeeGatherByGroup(int groupId, int population, EmployeeCategory category) {
        this.groupId = groupId;
        this.population = population;
        this.category = category;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getPopulation() {
        return population;
    }

    public EmployeeCategory getCategory() {
        return category;
    }
}
