package com.bow.transport.netty4;

import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;
import com.alibaba.dubbo.remoting.buffer.ChannelBufferFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * Wrap netty dynamic channel buffer.
 * 
 * @author vv
 * @since 2016/3/18.
 */
public class Netty4BackedChannelBufferFactory implements ChannelBufferFactory {

    private static final Netty4BackedChannelBufferFactory INSTANCE = new Netty4BackedChannelBufferFactory();

    public static ChannelBufferFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public ChannelBuffer getBuffer(int capacity) {
        return new Netty4BackedChannelBuffer(ByteBufAllocator.DEFAULT.buffer(capacity));
    }

    @Override
    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(length);
        buffer.writeBytes(array, offset, length);
        return new Netty4BackedChannelBuffer(buffer);
    }

    @Override
    public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
        return new Netty4BackedChannelBuffer(Unpooled.wrappedBuffer(nioBuffer));
    }
}
