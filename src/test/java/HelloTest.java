import com.google.common.collect.Maps;
import com.operation.database.Database;
import com.operation.database.DatabaseCheck;
import com.operation.database.service.SelectWithConfigHelper;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: Yadong Qin
 * @Date: 2019/1/27
 */
public class HelloTest {

    @Test
    public void test() {
        System.out.println(Database.selectList("189",User.class,"t_user","id = '38960ee68b1f4cb6bc180641990b3f93' or id = '3dba6ad877974e9281299079f1acc49f'"));
//        System.out.println(Database.delete("189","t_user","id = '38960ee68b1f4cb6bc180641990b3f93'"));
    }

    @Test
    public void test2() throws IOException {
        String file = "D:\\WebProject\\TestFramwork\\src\\main\\resources\\config\\dbConf\\testdb.conf";
        //InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        //System.out.println(resourceAsStream);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Properties properties = new Properties();
        properties.load(bufferedReader);
        System.out.println(properties.get("ext1_db_url"));
    }

    @Test
    public void test3() {
        String filePath = "config/dbConf/testdb.conf";
        String format = String.format("%s\\src\\main\\resources\\%s", System.getProperty("user.dir"), filePath.replace("/", "\\"));
        System.out.println(format);
    }

    @Test
    public void test4() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id",15);
        map.put("username","aiyoumi");
        map.put("password","123456");
        map.put("phone","17467232321");
        map.put("email","aiyoumi@123.com");
        map.put("created",new Date());
        map.put("updated",new Date());
//        System.out.println(Database.insert("tb_user",map));
//        System.out.println(Database.insert(User.class, JSONObject.parseObject(JSON.toJSONString(map),User.class),"tb_user"));
        System.out.println(Database.delete("tb_user","id = 15"));
    }

    @Test
    public void test5() {
        String file = "tester/service/case/insertCsvFile.csv";
        //D:\WebProject\TestFramwork\src\test\resources\tester\service\case\insertCsvFile.csv
        //D:\WebProject\TestFramwork\src\main\resources\tester\service\case\insertCsvFile.csv
        System.out.println(Database.batchInsert(file, "tb_user"));
    }

    @Test
    public void test6() {
        String file = "tester/service/case/checkDBFile.csv";
        DatabaseCheck.checkListByCsvFile(file);
    }

    @Test
    public void test7() {
        System.out.println(Database.selectOne("select * from tb_user where id = 1"));
        System.out.println(SelectWithConfigHelper.selectOne(null,"189","select * from t_user where id = '3dba6ad877974e9281299079f1acc49f'"));
        System.out.println(SelectWithConfigHelper.selectCount(null, "189","select count(*) from t_user"));
        System.out.println(SelectWithConfigHelper.selectList(null, "189","t_user","username = '小妹' and station = '上海'",""));
    }
}
