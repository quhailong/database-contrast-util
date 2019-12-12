package top.quhailong.database.entity;

public class TableInfo{
    /**
     * 列名称
     */
    private String columnName;
    /**
     * 字段位置
     */
    private Integer ordinalPosition;
    /**
     * 是否为空
     */
    private String isNullable;
    /**
     * 列类型
     */
    private String columnType;
    /**
     * 列属性
     */
    private String columnKey;
    /**
     * 列扩展属性
     */
    private String extra;
    /**
     * 列默认值
     */
    private String columnDefault;
    /**
     * 列备注
     */
    private String columnComment;

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "columnName='" + columnName + '\'' +
                ", ordinalPosition=" + ordinalPosition +
                ", isNullable='" + isNullable + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnKey='" + columnKey + '\'' +
                ", extra='" + extra + '\'' +
                ", columnDefault='" + columnDefault + '\'' +
                ", columnComment='" + columnComment + '\'' +
                '}';
    }
}
