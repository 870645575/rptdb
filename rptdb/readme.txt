作者：蔡泽鹏  QQ：870645575
邮箱:870645575@qq.com

网址： http://localhost:8080/rptdb/index
数据库配置文件(如果不存在则使用测试数据库连接)：/src/jdbc.properties
sql：
create table rptdb.t_depot_area
(
 D_NAME                                             VARCHAR2(200),
 D_AREA                                             VARCHAR2(2000),
 D_CAPACITY                                         NUMBER(16,4),
 D_COORDINATE                                       VARCHAR2(2000),
 D_DATE                                             DATE);
