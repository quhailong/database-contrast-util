package top.quhailong.database.entity;
/**
 * 列索引信息
 *
 * @author: quhailong
 * @date: 2019/10/23
 */
public class ColumnIndexInfo {
    private String Table;
    private Integer Non_unique;
    private String Key_name;
    private Integer Seq_in_index;
    private String Column_name;
    private String Collation;
    private Integer Cardinality;
    private String Sub_part;
    private String Packed;
    private String Null;
    private String Index_type;
    private String Comment;
    private String Index_comment;
    private String Visible;
    private String Expression;

    public String getTable() {
        return Table;
    }

    public void setTable(String table) {
        Table = table;
    }

    public Integer getNon_unique() {
        return Non_unique;
    }

    public void setNon_unique(Integer non_unique) {
        Non_unique = non_unique;
    }

    public String getKey_name() {
        return Key_name;
    }

    public void setKey_name(String key_name) {
        Key_name = key_name;
    }

    public Integer getSeq_in_index() {
        return Seq_in_index;
    }

    public void setSeq_in_index(Integer seq_in_index) {
        Seq_in_index = seq_in_index;
    }

    public String getColumn_name() {
        return Column_name;
    }

    public void setColumn_name(String column_name) {
        Column_name = column_name;
    }

    public String getCollation() {
        return Collation;
    }

    public void setCollation(String collation) {
        Collation = collation;
    }

    public Integer getCardinality() {
        return Cardinality;
    }

    public void setCardinality(Integer cardinality) {
        Cardinality = cardinality;
    }

    public String getSub_part() {
        return Sub_part;
    }

    public void setSub_part(String sub_part) {
        Sub_part = sub_part;
    }

    public String getPacked() {
        return Packed;
    }

    public void setPacked(String packed) {
        Packed = packed;
    }

    public String getNull() {
        return Null;
    }

    public void setNull(String aNull) {
        Null = aNull;
    }

    public String getIndex_type() {
        return Index_type;
    }

    public void setIndex_type(String index_type) {
        Index_type = index_type;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getIndex_comment() {
        return Index_comment;
    }

    public void setIndex_comment(String index_comment) {
        Index_comment = index_comment;
    }

    public String getVisible() {
        return Visible;
    }

    public void setVisible(String visible) {
        Visible = visible;
    }

    public String getExpression() {
        return Expression;
    }

    public void setExpression(String expression) {
        Expression = expression;
    }
}
