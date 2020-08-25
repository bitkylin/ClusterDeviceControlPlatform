package cc.bitky.clusterdeviceplatform.server.pojo.dataprocess;

public class EmployeeGatherByDepartment {
    /**
     * 部门名称
     */
    String department;
    /**
     * 总人数
     */
    int population;
    /**
     * 分类
     */
    EmployeeCategory category;

    public EmployeeGatherByDepartment(String department, int population, EmployeeCategory category) {

        this.department = department;
        this.population = population;
        this.category = category;
    }

    public String getDepartment() {
        return department;
    }

    public int getPopulation() {
        return population;
    }

    public EmployeeCategory getCategory() {
        return category;
    }
}
