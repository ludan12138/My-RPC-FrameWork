package cn.ludan.rpc.socket.util;

import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.enumeration.PackageType;
import cn.ludan.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;
/**
 * socket方式发送
 * 为发送的数据添加自定义头部信息，以及用指定的序列化器序列化数据
 * @author Ludan
 * @date 2021/8/30 9:12
*/
public class ObjectWriter {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer) throws IOException {

        outputStream.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
        outputStream.write(intToBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

    private static byte[] intToBytes(int value) {
        byte[] des = new byte[4];
        des[3] =  (byte) ((value>>24) & 0xFF);
        des[2] =  (byte) ((value>>16) & 0xFF);
        des[1] =  (byte) ((value>>8) & 0xFF);
        des[0] =  (byte) (value & 0xFF);
        return des;
    }
}
