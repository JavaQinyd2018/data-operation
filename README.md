# data-operation
通过Java JDBC封装的增删改查工具类
# 使用：
## 一.基本的增删改查
### 实体类
```java
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
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
### 1.查询
```java
 @Test
    public void test() {
        //查询list转化成User对象
        List<User> userList = Database.selectList(User.class, "tb_user", "id = '38960ee68b1f4cb6bc180641990b3f93' or id = '3dba6ad877974e9281299079f1acc49f'");
        System.out.println(userList);
        Map<String, Object> selectOne = Database.selectOne("select * from tb_user where id = 1");
        System.out.println(selectOne);
    }
```
### 2.删除
```java
 @Test
    public void testHello() {
        //删除，result是几就说明删了几条
        int result = Database.delete("tb_user", "id = '38960ee68b1f4cb6bc180641990b3f93'");
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
        System.out.println(Database.insert("tb_user",map));
        System.out.println(Database.insert(User.class, JSONObject.parseObject(JSON.toJSONString(map),User.class),"tb_user"));
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
        System.out.println(Database.update("tb_user",map));
        System.out.println(Database.update(User.class, JSONObject.parseObject(JSON.toJSONString(map),User.class),"tb_user"));
    }
  ```
  ## 二.切换数据源，根据配置增删改查操作
  ### 1.配置
  默认读取 jdbc1_db_url，也就是标识为1的数据库配置
  ```properties
  # 默认数据源配置
  jdbc1_db_url=jdbc:mysql://localhost:3306/e3mall?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=true
  jdbc1_db_username=root
  jdbc1_db_password=123456
  jdbc1_db_schema=e3mall
  # 112数据源配置
  jdbc112_db_url=jdbc:mysql://localhost:3306/e3mall?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=true
  jdbc112_db_username=root
  jdbc112_db_password=123456
  jdbc112_db_schema=e3mall
 ```
 ### 2.使用
 切换数据源配置可以通过传入数据源配置标识，需要传入参数
 ```java
 List<User> userList = Database.selectList("112",User.class, "tb_user", "id = '38960ee68b1f4cb6bc180641990b3f93' or id = '3dba6ad877974e9281299079f1acc49f'");
```
