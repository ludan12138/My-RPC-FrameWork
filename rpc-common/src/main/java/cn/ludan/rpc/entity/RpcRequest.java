package cn.ludan.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 消费者向提供者发送的请求对象
 * @author Ludan
 * @date 2021/8/24 10:15
*/
@Data
@Builder
public class RpcRequest implements Serializable {

    /**
     * 待调用的接口名称
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 调用方法的参数
     */
    private Object[] parameters;

    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;
}
