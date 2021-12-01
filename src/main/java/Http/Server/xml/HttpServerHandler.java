package Http.Server.xml;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.json.simple.JSONObject;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

//    private static Logger logger = LogManager.getLogger(HttpServerHandler.class);
//	private static final String LOG_TITLE = "[HttpServerHandler] ";

    private HttpRequest request;
    StringBuilder responseData = new StringBuilder();

    JSONObject jsonData = new JSONObject();
    JSONObject header = new JSONObject();
    JSONObject body = new JSONObject();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof HttpRequest) {

            this.request = (HttpRequest) msg;

            HttpHeaders headers = request.headers();
            if (!headers.isEmpty()) {
                for (Map.Entry<String, String> h : headers) {
                    String key = h.getKey();
                    header.put(key, h.getValue());
                }
            }

            header.put("REQUEST_URI",  request.getUri());
            header.put("REQUEST_METHOD", request.getMethod().name());
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
//			body.put("CONTENT", content.toString(CharsetUtil.UTF_8));

            if (msg instanceof LastHttpContent) {
//                logger.debug("LastHttpContent message received!!" + request.getUri());

                LastHttpContent trailer = (LastHttpContent) msg;

//				jsonData.put("header", header);
//				jsonData.put("body", body);
//				responseData.append(jsonData.toJSONString());

                responseData.append(content.toString(CharsetUtil.UTF_8));

                writeResponse(ctx, trailer, responseData);
                System.out.println("responseData.toString() >> " + responseData.toString());
            }
        }

    }

    private void writeResponse(ChannelHandlerContext ctx, LastHttpContent trailer, StringBuilder responseData) {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, ((HttpObject) trailer).getDecoderResult()
                .isSuccess() ? OK : BAD_REQUEST, Unpooled.copiedBuffer(responseData.toString(), CharsetUtil.UTF_8));

        httpResponse.headers().set("CONTENT-LENGHT", httpResponse.content().readableBytes());

        System.out.println("httpResponse.content().toString() >> " + httpResponse.content().toString());
        System.out.println("httpResponse.headers().get(\"CONTENT-LENGHT\") >> " + httpResponse.headers().get("CONTENT-LENGHT"));

        ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        logger.info("夸没 贸府 肯丰");
        System.out.println("夸没 贸府 肯丰");
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        logger.error(cause);
        ctx.close();
    }
}
