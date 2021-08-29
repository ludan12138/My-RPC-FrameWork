package cn.ludan.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 方法调用的响应状态码
 * @author Ludan
 * @date 2021/8/24 10:46
*/
@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200,"调用方法成功"),
    FAIL(500,"调用方法失败"),
    METHOD_NOT_FOUND(501,"未找到指定方法"),
    CLASS_NOT_FOUND(502,"未找到指定类");


    private final int code;
    private final String message;

}
