package com.bow.rpc.protocol;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.ClassHelper;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.Codec2;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.protocol.thrift.ClassNameGenerator;
import com.alibaba.dubbo.rpc.protocol.thrift.ThriftClassNameGenerator;
import com.alibaba.dubbo.rpc.protocol.thrift.ThriftConstants;
import com.alibaba.dubbo.rpc.protocol.thrift.ThriftUtils;
import com.alibaba.dubbo.rpc.protocol.thrift.io.RandomAccessByteArrayOutputStream;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ice framed protocol codec.
 * {@link IceInternal.Protocol}
 *
 * <pre>
 * |<-                                  message header                                  ->|<- message body ->|
 * +----------------+----------------------+------------------+---------------------------+------------------+
 * | magic (2 bytes)|message size (4 bytes)|head size(2 bytes)| version (1 byte) | header |   message body   |
 * +----------------+----------------------+------------------+---------------------------+------------------+
 * |<-                                               message size                                          ->|
 * </pre>
 * 
 * @author vv
 * @since 2017/3/18.
 */
public class IceCodec implements Codec2 {
    private static final AtomicInteger ICE_SEQ_ID = new AtomicInteger(0);

    private static final ConcurrentMap<String, Class<?>> cachedClass = new ConcurrentHashMap();

    static final ConcurrentMap<Long, IceCodec.RequestData> cachedRequest = new ConcurrentHashMap();

    public static final int MESSAGE_LENGTH_INDEX = 2;

    public static final int MESSAGE_HEADER_LENGTH_INDEX = 6;

    public static final int MESSAGE_SHORTEST_LENGTH = 10;

    public static final String NAME = "ice";

    public static final String PARAMETER_CLASS_NAME_GENERATOR = "class.name.generator";

    public static final byte VERSION = (byte) 1;

    public static final short MAGIC = (short) 0x1ce;

    @Override
    public void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException {

        if (message instanceof Request) {
            encodeRequest(channel, buffer, (Request) message);
        } else if (message instanceof Response) {
            // encodeResponse( channel, buffer, ( Response ) message );
        } else {
            throw new UnsupportedOperationException(new StringBuilder(32).append("ICE codec only support encode ")
                    .append(Request.class.getName()).append(" and ").append(Response.class.getName()).toString());
        }
    }

    @Override
    public Object decode(Channel channel, ChannelBuffer buffer) throws IOException {
        return null;
    }

    private void encodeRequest(Channel channel, ChannelBuffer buffer, Request request) throws IOException {

        RpcInvocation inv = (RpcInvocation) request.getData();

        int seqId = nextSeqId();

        String serviceName = inv.getAttachment(Constants.INTERFACE_KEY);

        if (StringUtils.isEmpty(serviceName)) {
            throw new IllegalArgumentException(
                    new StringBuilder(32).append("Could not find service name in attachment with key ")
                            .append(Constants.INTERFACE_KEY).toString());
        }

        RandomAccessByteArrayOutputStream bos = new RandomAccessByteArrayOutputStream(1024);

        byte[] header = new byte[IceInternal.Protocol.headerSize];

        buffer.writeBytes(header);
        buffer.writeBytes(bos.toByteArray());

    }

    private static int nextSeqId() {
        return ICE_SEQ_ID.incrementAndGet();
    }

    // just for test
    static int getSeqId() {
        return ICE_SEQ_ID.get();
    }

    static class RequestData {
        int id;

        String serviceName;

        String methodName;

        static IceCodec.RequestData create(int id, String sn, String mn) {
            IceCodec.RequestData result = new IceCodec.RequestData();
            result.id = id;
            result.serviceName = sn;
            result.methodName = mn;
            return result;
        }

    }
}
