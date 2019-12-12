package top.quhailong.database.entity;

import java.util.List;

public class TableCompareRequest {
    private List<String> databasesName;
    private Integer autoFixWhether;

    public Integer getAutoFixWhether() {
        return autoFixWhether;
    }

    public void setAutoFixWhether(Integer autoFixWhether) {
        this.autoFixWhether = autoFixWhether;
    }

    public List<String> getDatabasesName() {
        return databasesName;
    }

    public void setDatabasesName(List<String> databasesName) {
        this.databasesName = databasesName;
    }

    @Override
    public String toString() {
        return "TableCompareRequest{" +
                "databasesName=" + databasesName +
                ", autoFixWhether=" + autoFixWhether +
                '}';
    }
}
