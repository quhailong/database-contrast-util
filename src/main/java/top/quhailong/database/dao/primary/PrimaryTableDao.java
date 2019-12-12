package top.quhailong.database.dao.primary;

import top.quhailong.database.entity.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PrimaryTableDao {
    @Select("select TABLE_NAME as tableName from information_schema.TABLES where TABLE_SCHEMA=#{databaseName}")
    List<String> listMasterTable(String databaseName);


    @Select("select COLUMN_COMMENT as 'columnComment',COLUMN_DEFAULT as 'columnDefault', COLUMN_NAME as 'columnName',ORDINAL_POSITION as 'ordinalPosition',IS_NULLABLE as 'isNullable',COLUMN_TYPE as 'columnType',COLUMN_KEY as 'columnKey',EXTRA as 'extra' from information_schema.COLUMNS where TABLE_SCHEMA=#{databaseName} and TABLE_NAME=#{tableName}")
    List<TableInfo> listMasterTableColumn(@Param("databaseName") String databaseName, @Param("tableName") String tableName);
    /**
     * 获取建表语句
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    @Select("SHOW CREATE TABLE ${databaseName}.${tableName}")
    Map<String,String> getCreateTableInfo(@Param("databaseName") String databaseName, @Param("tableName") String tableName);
}
