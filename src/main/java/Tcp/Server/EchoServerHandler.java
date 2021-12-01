package Tcp.Server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

// 입력된 데이터를 처리하는 이벤트 핸들러 상속
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 데이터 수신 이벤트 처리 메서드
    // 클라이언트로부터 데이터의 수신이 이루어졌을 때 Netty가 자동으로 호출하는 이벤트 메서드
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 수신된 데이터를 가지고 있는 Netty의 바이트 버퍼 객체로부터 문자열 객체를 읽어온다.
        String readMessage = ((ByteBuf)msg).toString(Charset.forName("EUC-KR"));
        System.out.println("EchoServer 수신내용 [" + readMessage + "]");

        StringBuilder stringBuilder = new StringBuilder();
//		stringBuilder.append("<?xml version=\"1.0\" encoding=\"EUC-KR\"?><body><MSG_CLCD>1000</MSG_CLCD><MSG_TYPE>ts</MSG_TYPE><MSG_SEQ>987654321</MSG_SEQ></body>");
//
//		stringBuilder.insert(0, String.format("%04d", stringBuilder.length()+4));
//		String sendMessage = stringBuilder.toString();
//
//		stringBuilder.append(readMessage.substring(6));
//		stringBuilder.insert(0, "<?xml ");
//		stringBuilder.insert(0, String.format("%04d", stringBuilder.length()));
//
//		String sendMessage = stringBuilder.toString();

        System.out.println("EchoServer 송신내용 [" + readMessage + "]");

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(readMessage.getBytes("EUC-KR"));

        // 수신한 데이터를 그대로 송신
        ctx.writeAndFlush(messageBuffer);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 채널 파이프 라인에 저장 된 버퍼를 전송
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
