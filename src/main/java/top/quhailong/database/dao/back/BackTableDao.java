package top.quhailong.database.dao.back;

import top.quhailong.database.entity.ColumnIndexInfo;
import top.quhailong.database.entity.TableInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BackTableDao {

    @Select("select TABLE_NAME as tableName from information_schema.TABLES where TABLE_SCHEMA=#{databaseName}")
    List<String> listSlaveTable(String databaseName);

    //@Select("select * from information_schema.COLUMNS where TABLE_SCHEMA=#{databaseName} and TABLE_NAME=#{tableName}")
    @Select("select COLUMN_COMMENT as 'columnComment',COLUMN_DEFAULT as 'columnDefault', COLUMN_NAME as 'columnName',ORDINAL_POSITION as 'ordinalPosition',IS_NULLABLE as 'isNullable',COLUMN_TYPE as 'columnType',COLUMN_KEY as 'columnKey',EXTRA as 'extra' from information_schema.COLUMNS where TABLE_SCHEMA=#{databaseName} and TABLE_NAME=#{tableName}")
    List<TableInfo> listSlaverTableColumn(@Param("databaseName") String databaseName, @Param("tableName") String tableName);

    /**
     * 修改表信息
     *
     * @author: quhailong
     * @date: 2019/10/22
     */
    Integer updateColumnType(@Param("databaseName") String databaseName, @Param("tableName") String tableName, @Param("columnName") String columnName, @Param("columnType") String columnType, @Param("isNullable") String isNullable, @Param("extra") String extra, @Param("columnDefault") String columnDefault, @Param("columnComment") String columnComment, @Param("columnKey") String columnKey);

    /**
     * 创建索引
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    Integer createColumnIndex(@Param("databaseName") String databaseName, @Param("tableName") String tableName, @Param("columnName") String columnName, @Param("columnKey") String columnKey);

    /**
     * 查询某一字段的索引
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    @Select("show index from ${databaseName}.${tableName} where Column_name = #{columnName}")
    ColumnIndexInfo getColumnIndex(@Param("databaseName") String databaseName, @Param("tableName") String tableName, @Param("columnName") String columnName);

    /**
     * 删除索引
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    @Delete("drop index ${indexName} on ${databaseName}.${tableName}")
    Integer removeColumnIndex(@Param("databaseName") String databaseName, @Param("tableName") String tableName, @Param("indexName") String indexName);

    /**
     * 创建数据库
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    @Insert("${createTableContent}")
    Integer createTable(@Param("createTableContent") String createTableContent);

    /**
     * 删除数据库
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    @Delete("drop table ${databaseName}.${tableName}")
    Integer removeTable(@Param("databaseName") String databaseName, @Param("tableName") String tableName);

    /**
     * 删除列的默认值
     *
     * @author: quhailong
     * @date: 2019/10/24
     */
    @Update("alter table ${databaseName}.${tableName} alter column ${columnName} drop default;")
    Integer removeColumnDefault(@Param("databaseName") String databaseName, @Param("tableName") String tableName,@Param("columnName") String columnName);
}
