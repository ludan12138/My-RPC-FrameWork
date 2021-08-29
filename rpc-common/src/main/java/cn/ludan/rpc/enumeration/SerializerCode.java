package cn.ludan.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化器的类型
 * @author Ludan
 * @date 2021/8/28 13:56
*/
@Getter
@AllArgsConstructor
public enum SerializerCode {

    JSON(1);

    private final int code;
}
