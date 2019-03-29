import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: Yadong Qin
 * @Date: 2019/1/27
 */
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
