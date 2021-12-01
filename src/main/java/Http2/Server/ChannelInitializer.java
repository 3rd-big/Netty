package Http2.Server;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ChannelInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        if (sslCtx != null) {
            socketChannel.pipeline()
                    .addFirst(sslCtx.newHandler(socketChannel.alloc()), Http2Util.getServerAPNHandler());
        }
    }
}
