package cn.ludan.rpc;

import cn.ludan.rpc.entity.RpcRequest;
/**
 * 客户端类通用接口
 * @author Ludan
 * @date 2021/8/25 16:27
*/
public interface RpcClient {

    /**
     * 客户端发送RPC请求
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);

}
