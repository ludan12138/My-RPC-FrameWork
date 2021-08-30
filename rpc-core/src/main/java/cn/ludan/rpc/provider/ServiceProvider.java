package cn.ludan.rpc.provider;

public interface ServiceProvider {
    /**
     * 注册服务
     * @param service
     * @param <T>
     */
    <T> void register (T service);

    /**
     * 根据服务名获取对应的服务实现
     * @param serviceName
     * @return
     */
    Object getService(String serviceName);
}
