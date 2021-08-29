package cn.ludan.rpc.api;
/**
 * 通用接口
 * @author Ludan
 * @date 2021/8/24 9:59
*/
public interface HelloService {

    /**
     * 测试用的调用方法
     * @param object
     * @return
     */
    String hello(HelloObject object);
}
