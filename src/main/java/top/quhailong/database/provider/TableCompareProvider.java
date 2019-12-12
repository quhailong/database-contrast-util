package top.quhailong.database.provider;

import top.quhailong.database.dao.back.BackTableDao;
import top.quhailong.database.dao.primary.PrimaryTableDao;
import top.quhailong.database.entity.ColumnIndexInfo;
import top.quhailong.database.entity.TableCompareRequest;
import top.quhailong.database.entity.TableInfo;
import top.quhailong.database.utils.TrueFalseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 表对比数据处理
 *
 * @author: quhailong
 * @date: 2019/10/18
 */
@Component
public class TableCompareProvider {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PrimaryTableDao primaryTableDao;
    @Autowired
    private BackTableDao backTableDao;

    public String tableCompareHandle(TableCompareRequest request) {
        List<String> databasesName = request.getDatabasesName();
        String saveFile = "result.txt";
        File file = new File(saveFile);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            if (!file.exists()) {
                boolean hasFile = file.createNewFile();
                if (hasFile) {
                    logger.info("file not exists, create new file");
                }
                fos = new FileOutputStream(file);
            } else {
                fos = new FileOutputStream(file, true);
            }
            osw = new OutputStreamWriter(fos, "utf-8");
            osw.write("-----------------------------------------当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance(Locale.CHINA).getTime()) + "-----------------------------------------");
            osw.write("\r\n");  //换行
            if (databasesName != null && databasesName.size() > 0) {
                for (String databaseName : databasesName) {
                    List<String> masterTable = primaryTableDao.listMasterTable(databaseName);
                    List<String> slaveTable = backTableDao.listSlaveTable(databaseName);
                    List<String> resultTableName = tableCountCompare(databaseName, masterTable, slaveTable, osw, request.getAutoFixWhether());
                    if (resultTableName != null && resultTableName.size() > 0) {
                        for (String tableName : resultTableName) {
                            List<TableInfo> masterTableColumnList = primaryTableDao.listMasterTableColumn(databaseName, tableName);
                            List<TableInfo> slaverTableColumnList = backTableDao.listSlaverTableColumn(databaseName, tableName);
                            tableColumnCompare(databaseName, tableName, masterTableColumnList, slaverTableColumnList, osw, request.getAutoFixWhether());
                        }
                    }
                }
            }
            osw.write("-----------------------------------------对比结束-----------------------------------------");
            osw.write("\r\n");  //换行
        } catch (Exception e) {
            e.printStackTrace();
        } finally {   //关闭流
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 数据库表数量对比
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    private List<String> tableCountCompare(String databaseName, List<String> masterTable, List<String> slaveTable, OutputStreamWriter osw, Integer autoFixWhether) throws IOException {
        List<String> resultTableName = new ArrayList<>();
        for (String tableName : masterTable) {
            if (!slaveTable.contains(tableName)) {
                String content = "线上" + databaseName + "库" + tableName + "表不存在";
                if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                    Map<String, String> createTableInfo = primaryTableDao.getCreateTableInfo(databaseName, tableName);
                    StringBuffer createTableContent = new StringBuffer(createTableInfo.get("Create Table"));
                    createTableContent.insert(13, "`" + databaseName + "`.");
                    backTableDao.createTable(createTableContent.toString());
                    content = content + "-----已修正";
                }
                logger.info(content);
                osw.write(content); //写入内容
                osw.write("\r\n");  //换行
            } else {
                resultTableName.add(tableName);
            }
        }
        for (String tableName : slaveTable) {
            if (!masterTable.contains(tableName)) {
                String content = "线上" + databaseName + "库" + tableName + "表多余";
                if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                    backTableDao.removeTable(databaseName, tableName);
                    content = content + "-----已修正";
                }
                logger.info(content);
                osw.write(content); //写入内容
                osw.write("\r\n");  //换行
            }
        }
        return resultTableName;
    }

    /**
     * 数据库表内容对比
     *
     * @author: quhailong
     * @date: 2019/10/23
     */
    private void tableColumnCompare(String databaseName, String tableName, List<TableInfo> masterTableColumnList, List<TableInfo> slaverTableColumnList, OutputStreamWriter osw, Integer autoFixWhether) throws IOException {
        Map<String, TableInfo> masterTableColumnMap = listConvertMap(masterTableColumnList);
        Map<String, TableInfo> slaverTableColumnMap = listConvertMap(slaverTableColumnList);
        for (Map.Entry<String, TableInfo> entry : masterTableColumnMap.entrySet()) {
            String columnName = entry.getKey();
            TableInfo masterTableInfo = entry.getValue();
            TableInfo slaverTableInfo = slaverTableColumnMap.get(columnName);
            if (slaverTableInfo == null) {
                String content = "线上" + databaseName + "库" + tableName + "表，" + columnName + "字段缺失或字段名称不正确";
                logger.info(content);
                osw.write(content); //写入内容
                osw.write("\r\n");  //换行
            } else {
                if (!masterTableInfo.getColumnType().equals(slaverTableInfo.getColumnType())) {
                    String content = "线上" + databaseName + "库" + tableName + "表，" + slaverTableInfo.getColumnName() + "字段，字段类型不正确（" + slaverTableInfo.getColumnType() + "），应该为" + masterTableInfo.getColumnType();
                    if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                        backTableDao.updateColumnType(databaseName, tableName, slaverTableInfo.getColumnName(), masterTableInfo.getColumnType(), masterTableInfo.getIsNullable(), masterTableInfo.getExtra(), masterTableInfo.getColumnDefault(), masterTableInfo.getColumnComment(), masterTableInfo.getColumnKey());
                        content = content + "-----已修正";
                    }
                    logger.info(content);
                    osw.write(content); //写入内容
                    osw.write("\r\n");  //换行
                }
                if (!masterTableInfo.getColumnKey().equals(slaverTableInfo.getColumnKey())) {
                    String content = "线上" + databaseName + "库" + tableName + "表，" + slaverTableInfo.getColumnName() + "字段，字段属性不正确（" + slaverTableInfo.getColumnKey() + "），应该为" + masterTableInfo.getColumnKey();
                    if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                        ColumnIndexInfo columnIndexInfo = backTableDao.getColumnIndex(databaseName, tableName, masterTableInfo.getColumnName());
                        backTableDao.removeColumnIndex(databaseName, tableName, columnIndexInfo.getKey_name());
                        backTableDao.createColumnIndex(databaseName, tableName, masterTableInfo.getColumnName(), masterTableInfo.getColumnKey());
                        content = content + "-----已修正";
                    }
                    logger.info(content);
                    osw.write(content); //写入内容
                    osw.write("\r\n");  //换行
                }
                if (!masterTableInfo.getExtra().equals(slaverTableInfo.getExtra())
                        && (((masterTableInfo.getExtra().equals("on update CURRENT_TIMESTAMP")
                        || masterTableInfo.getExtra().equals("DEFAULT_GENERATED on update CURRENT_TIMESTAMP"))
                        && (!slaverTableInfo.getExtra().equals("DEFAULT_GENERATED on update CURRENT_TIMESTAMP")
                        && !slaverTableInfo.getExtra().equals("on update CURRENT_TIMESTAMP")))
                        || (masterTableInfo.getExtra().equals("auto_increment")
                        && !slaverTableInfo.getExtra().equals("auto_increment")))) {
                    String content = "线上" + databaseName + "库" + tableName + "表，" + slaverTableInfo.getColumnName() + "字段，字段扩展属性不正确（" + slaverTableInfo.getExtra() + "），应该为" + masterTableInfo.getExtra();
                    if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                        backTableDao.updateColumnType(databaseName, tableName, slaverTableInfo.getColumnName(), masterTableInfo.getColumnType(), masterTableInfo.getIsNullable(), masterTableInfo.getExtra(), masterTableInfo.getColumnDefault(), masterTableInfo.getColumnComment(), masterTableInfo.getColumnKey());
                        content = content + "-----已修正";
                    }
                    logger.info(content);
                    osw.write(content); //写入内容
                    osw.write("\r\n");  //换行
                }
                if (!masterTableInfo.getIsNullable().equals(slaverTableInfo.getIsNullable())) {
                    String content = "线上" + databaseName + "库" + tableName + "表，" + slaverTableInfo.getColumnName() + "字段，是否为空不正确（" + slaverTableInfo.getIsNullable() + "），应该为" + masterTableInfo.getIsNullable();
                    if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                        backTableDao.updateColumnType(databaseName, tableName, slaverTableInfo.getColumnName(), masterTableInfo.getColumnType(), masterTableInfo.getIsNullable(), masterTableInfo.getExtra(), masterTableInfo.getColumnDefault(), masterTableInfo.getColumnComment(), masterTableInfo.getColumnKey());
                        content = content + "-----已修正";
                    }
                    logger.info(content);
                    osw.write(content); //写入内容
                    osw.write("\r\n");  //换行
                }
                if (masterTableInfo.getColumnDefault() != null) {
                    if (!masterTableInfo.getColumnDefault().equals(slaverTableInfo.getColumnDefault())) {
                        String content = "线上" + databaseName + "库" + tableName + "表，" + slaverTableInfo.getColumnName() + "字段，字段默认值不正确（" + slaverTableInfo.getColumnDefault() + "），应该为" + masterTableInfo.getColumnDefault();
                        if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                            backTableDao.updateColumnType(databaseName, tableName, slaverTableInfo.getColumnName(), masterTableInfo.getColumnType(), masterTableInfo.getIsNullable(), masterTableInfo.getExtra(), masterTableInfo.getColumnDefault(), masterTableInfo.getColumnComment(), masterTableInfo.getColumnKey());
                            content = content + "-----已修正";
                        }
                        logger.info(content);
                        osw.write(content); //写入内容
                        osw.write("\r\n");  //换行
                    }
                } else if (masterTableInfo.getColumnDefault() == null && slaverTableInfo.getColumnDefault() != null) {
                    String content = "线上" + databaseName + "库" + tableName + "表，" + slaverTableInfo.getColumnName() + "字段，字段默认值不正确（" + slaverTableInfo.getColumnDefault() + "），应该为" + masterTableInfo.getColumnDefault();
                    if(TrueFalseEnum.TRUE.getCode().equals(autoFixWhether)){
                        backTableDao.removeColumnDefault(databaseName,tableName,masterTableInfo.getColumnName());
                        content = content + "-----已修正";
                    }
                    logger.info(content);
                    osw.write(content); //写入内容
                    osw.write("\r\n");  //换行
                }
            }
        }
        for (Map.Entry<String, TableInfo> entry : slaverTableColumnMap.entrySet()) {
            String columnName = entry.getKey();
            if (!masterTableColumnMap.containsKey(columnName)) {
                String content = "线上" + databaseName + "库" + tableName + "表，" + columnName + "字段多余";
                logger.info(content);
                osw.write(content); //写入内容
                osw.write("\r\n");  //换行
            }
        }
    }

    /**
     * 列表转Map
     *
     * @author: quhailong
     * @date: 2019/10/21
     */
    private Map<String, TableInfo> listConvertMap(List<TableInfo> tableColumnList) {
        Map<String, TableInfo> tableInfoMap = new HashMap<>();
        if (tableColumnList != null && tableColumnList.size() > 0) {
            for (TableInfo tableInfo : tableColumnList) {
                tableInfoMap.put(tableInfo.getColumnName(), tableInfo);
            }
        }
        return tableInfoMap;
    }
}
