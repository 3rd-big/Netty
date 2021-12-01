package Tcp.Server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

// �Էµ� �����͸� ó���ϴ� �̺�Ʈ �ڵ鷯 ���
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // ������ ���� �̺�Ʈ ó�� �޼���
    // Ŭ���̾�Ʈ�κ��� �������� ������ �̷������ �� Netty�� �ڵ����� ȣ���ϴ� �̺�Ʈ �޼���
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // ���ŵ� �����͸� ������ �ִ� Netty�� ����Ʈ ���� ��ü�κ��� ���ڿ� ��ü�� �о�´�.
        String readMessage = ((ByteBuf)msg).toString(Charset.forName("EUC-KR"));
        System.out.println("EchoServer ���ų��� [" + readMessage + "]");

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

        System.out.println("EchoServer �۽ų��� [" + readMessage + "]");

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(readMessage.getBytes("EUC-KR"));

        // ������ �����͸� �״�� �۽�
        ctx.writeAndFlush(messageBuffer);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // ä�� ������ ���ο� ���� �� ���۸� ����
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
