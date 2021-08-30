package cn.ludan.rpc.serializer;

import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.enumeration.SerializerCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try{
            return objectMapper.writeValueAsBytes(obj);
        }catch (JsonProcessingException e){
            logger.error("序列化时有错误发生：{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            Object obj = objectMapper.readValue(bytes,clazz);
            if(obj instanceof RpcRequest){
                obj = handleRequest(obj);
            }
            return obj;
        }catch (IOException e){
            logger.error("反序列化时有错误发生：{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RpcRuqest中的Object数组在序列化时，由于Object是一个十分模糊的类型，会出现序列化失败的情况
     * 因此要借助ParamTypes来获取到Object数组中每个实例的Class来辅助序列化
     * @param obj
     * @return
     * @throws IOException
     */
    private Object handleRequest(Object obj) throws IOException{
        RpcRequest rpcRequest = (RpcRequest) obj;
        for(int i = 0; i < rpcRequest.getParamTypes().length; i ++) {
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
