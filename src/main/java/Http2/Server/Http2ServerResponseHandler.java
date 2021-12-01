package Http2.Server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.*;
import io.netty.util.CharsetUtil;

public class Http2ServerResponseHandler extends ChannelDuplexHandler {
    static final ByteBuf RESPONSE_BYTES = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8));

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Http2HeadersFrame) {
            Http2HeadersFrame msgHeader = (Http2HeadersFrame) msg;
            if (msgHeader.isEndStream()) {
                ByteBuf content = ctx.alloc().buffer();
                content.writeBytes(RESPONSE_BYTES.duplicate());
//                content.writeBytes("Hello".getBytes());


//                ByteBuf sendContent = ctx.alloc().buffer();


                Http2Headers headers = new DefaultHttp2Headers().status(HttpResponseStatus.OK.codeAsText());
                ctx.write(new DefaultHttp2HeadersFrame(headers).stream(msgHeader.stream()));
                ctx.write(new DefaultHttp2DataFrame(content, true).stream(msgHeader.stream()));
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
