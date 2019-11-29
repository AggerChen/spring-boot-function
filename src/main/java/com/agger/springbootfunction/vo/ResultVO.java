package com.agger.springbootfunction.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: ResultVO
 * @description: TODO
 * @author: chenhx
 * @create: 2019-11-25 14:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO<T> {
    private Integer code = -1;     // 返回状态码
    private String msg;            // 返回信息
    private T data;                // 返回数据

}
