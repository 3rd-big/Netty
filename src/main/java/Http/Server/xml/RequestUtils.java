package Http.Server.xml;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.CharsetUtil;

public class RequestUtils {

    static StringBuilder formatBody(HttpContent httpContent) {
        StringBuilder responseData = new StringBuilder();
        ByteBuf content = httpContent.content();

        if (content.isReadable()) {
            responseData.append("\r\n");
            responseData.append("content: ");
            responseData.append(content.toString(CharsetUtil.UTF_8));
        }
        return responseData;
    }
}
