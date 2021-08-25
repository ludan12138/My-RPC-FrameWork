package cn.ludan.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
* @author Ludan
* @date 2021/8/24 10:00
*/

@Data
@AllArgsConstructor
public class HelloObject implements Serializable {

    private Integer id;
    private String message;
}
