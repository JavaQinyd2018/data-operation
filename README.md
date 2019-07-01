# data-operation
通过Java JDBC封装的增删改查工具类
# 使用：
1.下载源码打包并添加依赖
 ```xml
        <dependency>
            <groupId>com.database</groupId>
    	    <artifactId>data-operation</artifactId>
            <version>1.1.0</version>
        </dependency>
 ```
2.在自己项目的resources的新建config/db.properties配置文件，添加数据源信息：
```properties
  # 默认数据源配置
jdbc.datasource.driver=com.mysql.cj.jdbc.Driver
jdbc.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
jdbc.datasource.username=root
jdbc.datasource.password=123456
jdbc.datasource.schema=test
```
## 一.基本的增删改查
### 实体类
```java
public class User {
    private String id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Date created;
    private Date updated;
}
```
### 1.查询数据
```java
 @Test
    public void test() {
       //1.直接输入sql语句查询
        Map<String, Object> selectOne = Database.selectOne("select * from tb_user where id = 1");
        System.out.println(selectOne);
        //2.查询list转化成User对象
        List<User> userList = Database.selectList(User.class, "tb_user", "id = 1 or id = 2");
        System.out.println(userList);
    }
```
### 2.删除数据
```java
    @Test
    public void testHello() {
        //根据条件删除，result是几就说明删了几条
        int result = Database.delete("tb_user", "id = 1");
        System.out.println(result);
    }
```
  ### 3.插入数据
  ```java
    @Test
    public void test4() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id",15);
        map.put("username","kobe");
        map.put("password","123456");
        map.put("phone","17467232321");
        map.put("email","kobe@123.com");
        map.put("created",new Date());
        map.put("updated",new Date());
	//1.按照键值对的方式插入数据到表中
        System.out.println(Database.insert("tb_user",map));
	User user = JSONObject.parseObject(JSON.toJSONString(map),User.class);
	//2. 直接插入实体类到数据库
        int result = Database.insert(User.class, user,"tb_user");
        System.out.println(result);
    }
  ```
  ### 4.修改数据
  ```java
    @Test
    public void test4() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id",15);
        map.put("username","jordan");
        map.put("password","345678");
        map.put("phone","17467232321");
        map.put("email","jordan@123.com");
        map.put("created",new Date());
        map.put("updated",new Date());
	//1.更新，通过map更新数据库的信息
        System.out.println(Database.update("tb_user",map));
	//2.转化成实体类更新数据库数据
	User user = JSONObject.parseObject(JSON.toJSONString(map),User.class);
        System.out.println(Database.update(User.class, user,"tb_user"));
	//3. 也可以通过sql语句直接更新
	int result = Database.update("update set username = 'kobe' where id = 15");
	System.out.println(result);
    }
  ```
  ## 二.切换数据源，根据配置增删改查操作
  ### 1.配置
  默认读取 jdbc.datasource.driver，也就是标识为dev的数据库配置
  ```properties
  # 默认数据源配置
jdbc.datasource.driver=com.mysql.cj.jdbc.Driver
jdbc.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
jdbc.datasource.username=root
jdbc.datasource.password=123456
jdbc.datasource.schema=test
  # 112数据源配置
dev.jdbc.datasource.driver=com.mysql.cj.jdbc.Driver
dev.jdbc.datasource.url=jdbc:mysql://localhost:3306/51shopping?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
dev.jdbc.datasource.username=root
dev.jdbc.datasource.password=123456
dev.jdbc.datasource.schema=test
  ```
 ### 2.使用
 切换数据源配置可以通过传入数据源配置标识
 ```java
 //1. 切换环境，直接通过条件sql进行查询
 List<Map<String,Object>> result = DatabaseSwitch.selectList("112","tb_user", "id = 1 or id = 2");
 System.out.println(result);
 //2. 查询的结果转化成实体类对象信息
 List<User> userList = DatabaseSwitch.selectList("112",User.class, "tb_user", "id = 1 or id = 2");
 System.out.println(userList);
 ```
