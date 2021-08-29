package cn.ludan.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发送包的类型 请求/响应
 * @author Ludan
 * @date 2021/8/28 13:53
*/

@AllArgsConstructor
@Getter
public enum PackageType {
    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;
}
