package cn.ludan.rpc.transport;

import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 * @author Ludan
 * @date 2021/8/25 16:27
*/
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     * 客户端发送RPC请求
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);

}
