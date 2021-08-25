package cn.ludan.rpc.entity;

import cn.ludan.rpc.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;
/**
 * 提供者执行完成或出错后向消费者返回的结果对象
 * @author Ludan
 * @date 2021/8/24 10:22
*/
@Data
public class RpcResponse<T> implements Serializable {

    private Integer statusCode;

    private String message;

    private T data;

    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
