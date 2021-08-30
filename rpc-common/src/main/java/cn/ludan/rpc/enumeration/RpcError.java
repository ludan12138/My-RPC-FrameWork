package cn.ludan.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum RpcError {
    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的（反）序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("连接Naco服务器失败"),
    REGISTER_SERVICE_FAILED("注册服务失败");

    private final String message;
}
