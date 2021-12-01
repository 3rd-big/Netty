package Tcp.Client;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.nio.charset.Charset;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    // ���� ä���� ���� Ȱ��ȭ �Ǿ��� �� ����
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

//		StringBuilder test = new StringBuilder();
//		test.append("<?xml version=\"1.0\" encoding=\"EUC-KR\"?><body><MSG_CLCD>1000</MSG_CLCD><MSG_TYPE>ts</MSG_TYPE><MSG_SEQ>987654321</MSG_SEQ></body>");
//		test.insert(0, String.format("%04d", test.length()+4));

        StringBuilder stringBuilder = new StringBuilder();
        SAXBuilder saxBuilder = new SAXBuilder();
        String filePath = getClass().getResource("/XMLFiles/Test2.xml").toURI().getPath();
        Document document = saxBuilder.build(filePath);
//		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("EUC-KR"));
        XMLOutputter xmlOutputter = new XMLOutputter();

        stringBuilder.append(xmlOutputter.outputString(document));
//		stringBuilder.insert(0,"000000000000000000000000000000");
        stringBuilder.insert(0, String.format("%04d", stringBuilder.length()+4));

        String sendMessage = stringBuilder.toString();
//		String sendMessage = test.toString();

        System.out.println("EchoClient ���۳��� [" + sendMessage + "]");

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes("EUC-KR"));

        ctx.writeAndFlush(messageBuffer);
        /*
         * writeAndFlush():  ���������� ��ϰ� ������ �� ���� �޼��� ȣ�� (write(), flush())
         * write(): ä�ο� �����͸� ���
         * flush(): ä�ο� ��� �� �����͸� ������ ����
         */
    }

    // �����κ��� ���� �� �����Ͱ� ���� �� ȣ��Ǵ� �޼���
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // �����κ��� ���� �� �����Ͱ� ���� �� msg ��ü���� ���ڿ� ������ ����
        String readMessage = ((ByteBuf)msg).toString(Charset.forName("EUC-KR"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("EchoClient ���ų��� [");
        stringBuilder.append(readMessage);
        stringBuilder.append("]");

        System.out.println(stringBuilder.toString());
    }

    // ���� �� �����͸� ��� �о��� �� ȣ��Ǵ� �̺�Ʈ �޼���
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // ������ ���� �� ä���� ����
        // ���� ������ �ۼ��� ä���� ������ �ǰ� Ŭ���̾�Ʈ ���α׷��� �����
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
