package cn.ludan.rpc.serializer;

public interface CommonSerializer {
    byte[] serialize(Object obj);

    Object deserializer(byte[] bytes,Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code){
        switch (code){
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
