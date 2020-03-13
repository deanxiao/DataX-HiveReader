package com.alibaba.datax.plugin.reader.hivereader;

/**
 * @author dean 2019/10/25.
 * @version v1.1
 */
public class Key {
    /**
     * 1.必选:hiveSql,defaultFS
     * 2.可选(有缺省值):
     * 			tempDatabase(default)
     *          tempHdfsLocation(/tmp/hive/)
     *          hive_cmd(hive)
     *          fieldDelimiter(\u0001)
     * 3.可选(无缺省值):hive_sql_set
     * */
    

    public final static String DEFAULT_FS = "defaultFS";
    //reader执行的hiveSql语句
    public final static String HIVE_SQL = "hiveSql";
    // 临时表所在的数据库名称
    public final static String TEMP_DATABASE = "tempDatabase";
    //临时标存放的HDFS目录
    public final static String TEMP_DATABASE_HDFS_LOCATION = "tempDatabasePath";
    //hive -e命令
    public final static String HIVE_CMD = "hive_cmd";
    public final static String HIVE_SQL_SET = "hive_sql_set"; 
    // 存储文件 hdfs默认的分隔符
    public final static String FIELDDELIMITER="fieldDelimiter";
    public static final String NULL_FORMAT = "nullFormat";
    public static final String HADOOP_CONFIG = "hadoopConfig";
    public static final String HAVE_KERBEROS = "haveKerberos";
    public static final String KERBEROS_KEYTAB_FILE_PATH = "kerberosKeytabFilePath";
    public static final String KERBEROS_PRINCIPAL = "kerberosPrincipal";

}
