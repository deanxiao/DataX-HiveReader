# DataX HiveReader

---

## 1 快速介绍

Hivereader插件:从Hive表读取数据


## 2 实现原理

实现方式是:根据配置的QuerySql,通过将查询结果保存到一张新的临时hive表中这种方式；然后获取临时表的hdfs文件地址，然后读取文件到缓冲区，最后删除临时的表。



## 3 功能说明

Hivereader插件:从Hive表读取数据

- 使用QuerySql方式取Hive表数据
  - 即可取整个数据，也可取部分数据
  - 支持取选取指定列或分区列
  - 支持同时取多个分区的数据
  - HdfsReader只能取整个表或整个分区的所有数据
- 支持读取任何格式与压缩的Hive的源表；



### 3.1 配置样例

#### job.json

```
{
  "job": {
    "setting": {
      "speed": {
        "channel":1
      }
    },
    "content": [
      {
        "reader": {
          "name": "hivereader",
          "parameter": {
              "hiveSql": [
                    "select username,telephone,mail from mysql_to_hive;"
               ],
              "defaultFS": "hdfs://xxx:port"
           }
        },
        "writer": {
          ......
        }
        }
    ]
  }

}
```

#### 3.2 参数说明

- hiveSql
  - 描述：Hive上的查询语句QuerySql
  - 必选：是
  - 默认值：无

- defaultFS 
  - 描述：Hadoop hdfs文件系统namenode节点地址。示例：hdfs://xxxxxxxxxx:8020
  - 必选：是
  - 默认值：无

- tempDatabase
  - 描述：hive临时表存放的database(需写权限); **建议根据实际场景设置**
  - 必选：否
  - 默认值：default

- tempDatabasePath
  - 描述：hive临时表所在数据库的hdfs路径(需写权限)；**建议根据实际场景设置**
  - 必选：否
  - 默认值：/user/hive/warehouse/

- fieldDelimiter
  - 描述：字段分隔符，hive临时表使用，建议**不设置**
  - 必选：否
  - 默认值：\\\\u0001（json里面需要写成'\\u0001'），可选示例'\\\t'  , ',' 等；注意，这与HdfsReader不一样

- nullFormat

  - 描述：文本文件中无法使用标准字符串定义null(空指针)，nullFormat可定义哪些字符串可以表示为null。

    例如如果用户配置: nullFormat:"\\\N"，那么如果源头数据是"\N"，DataX视作null字段。

  - 必选：否

  - 默认值：\\\\N

- hive_sql_set

  - 描述：hive临时表执行的前置hive set语句；示例，设置队列set tez.queue.name=root.xxxxxxx；
  - 必选：否
  - 默认值：空

- haveKerberos
  - 描述：是否进行Kerberos认证(true/false)
  - 必选：否
  - 默认值：空 

- kerberosKeytabFilePath
  - 描述：Kerberos认证 keytab文件路径，绝对路径,如/etc/security/keytabs/xxxx_user.keytab
  - 必选：否
  - 默认值：空 

- kerberosPrincipal
  - 描述：Kerberos认证Principal名，如xxxx/hadoopclient@xxx.xxx 
  - 必选：否
  - 默认值：空 

- hadoopConfig
  - 描述：hadoopConfig里可以配置与Hadoop相关的一些高级参数，比如HA的配置。
  - 必选：否
  - 默认值：空 

#### 3.3 环境准备

* 执行datax任务的机器要按照hive,并且配置好环境变量
* tempDatabase和tempDatabase需要有“写”权限



## 4 性能报告

### 4.1 环境准备

#### 4.1.3 测试环境

已在以下测试环境测试通过:

- **测试环境1**

  CDH 5.7.0 (hive 1.1.1 , hadoop 2.7.1)

- **测试环境2**

  HDP 3.1.4 (hive 3.1.0 , hadoop 3.1.1 )

### 4.2 测试报告

- 场景一

  json只传必选参数，其他为默认参数值

  ```json
  {
    "job": {
      "setting": {
        "speed": {
          "channel":1
        }
      },
      "content": [
        {
          "reader": {
            "name": "hivereader",
            "parameter": {
                "hiveSql": [
  "select id,pay_decimal,pay_str,pay_double from stage.stage_md_test limit 10;"
                 ],
                "defaultFS": "hdfs://quickstart.cloudera:8020"
             }
          },
  "writer":{"name": "streamwriter",
            "parameter": {
              "encoding": "UTF-8",
              "print": true
           
                      }
                  }
          }
      ]
    }
  
  }
  ```

- 场景二

​       json传了默认的参数，加部分可选参数（临时表database与hdfs路径）

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel":1
      }
    },
    "content": [
      {
        "reader": {
          "name": "hivereader",
          "parameter": {
              "hiveSql": [
                    "select id,pay_decimal,pay_str,pay_double from stage.stage_md_test limit 10;"
               ],
              "defaultFS": "hdfs://quickstart.cloudera:8020",
			  "tempDatabase":"stage",
			  "tempDatabasePath":"/user/hive/warehouse/stage.db/"

           }
        },
"writer":{"name": "streamwriter",
          "parameter": {
            "encoding": "UTF-8",
            "print": true
         
                    }
                }
        }
    ]
  }

}
```



- 场景三

  json传入所有的可选参数，注意fieldDelimiter传入的'\001'需要写成'\\\u0001'

  ```json
  {
    "job": {
      "setting": {
        "speed": {
          "channel":1
        }
      },
      "content": [
        {
          "reader": {
            "name": "hivereader",
            "parameter": {
                "hiveSql": [
                      "select id,pay_decimal,pay_str,pay_double from stage.stage_md_test limit 10;"
                 ],
                "defaultFS": "hdfs://quickstart.cloudera:8020",
  			  "tempDatabase":"stage",
  			  "tempDatabasePath":"/user/hive/warehouse/stage.db/",
  			  "fieldDelimiter":"\\u0001",
                "nullFormat":"\\N",
                "haveKerberos":"true",
                "kerberosKeytabFilePath":"/etc/security/keytabs/xxxx_user.keytab",
                "kerberosPrincipal":"xxxx_user@HADOOP_CLUSTER_XXXXX.COM",
                "hadoopConfig":{
                 "dfs.nameservices": "hadoop_cluster",
                 "dfs.ha.namenodes.hadoop_cluster": "nn1,nn2",
                 "dfs.namenode.rpc-address.hadoop_cluster.nn1": "IPXXXXXXX01:8020",
                 "dfs.namenode.rpc-address.hadoop_cluster.nn2": "IPXXXXXXX02:8020",
                 "dfs.client.failover.proxy.provider.hadoop_cluster": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"},
             }
          },
  "writer":{"name": "streamwriter",
            "parameter": {
              "encoding": "UTF-8",
              "print": true
           
                      }
                  }
          }
      ]
    }
  
  }
  ```

