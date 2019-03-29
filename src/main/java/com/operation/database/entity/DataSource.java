package com.operation.database.entity;

import lombok.*;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 10:27
 * @Since:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataSource {
    private String url;
    private String username;
    private String password;
    private String schame;
}
