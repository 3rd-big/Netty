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

    // 소켓 채널이 최초 활성화 되었을 때 실행
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

        System.out.println("EchoClient 전송내용 [" + sendMessage + "]");

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes("EUC-KR"));

        ctx.writeAndFlush(messageBuffer);
        /*
         * writeAndFlush():  내부적으로 기록과 전송의 두 가지 메서드 호출 (write(), flush())
         * write(): 채널에 데이터를 기록
         * flush(): 채널에 기록 된 데이터를 서버로 전송
         */
    }

    // 서버로부터 수신 된 데이터가 있을 때 호출되는 메서드
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 서버로부터 수신 된 데이터가 저장 된 msg 객체에서 문자열 데이터 추출
        String readMessage = ((ByteBuf)msg).toString(Charset.forName("EUC-KR"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("EchoClient 수신내용 [");
        stringBuilder.append(readMessage);
        stringBuilder.append("]");

        System.out.println(stringBuilder.toString());
    }

    // 수신 된 데이터를 모두 읽었을 때 호출되는 이벤트 메서드
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // 서버와 연결 된 채널을 닫음
        // 이후 데이터 송수신 채널을 닫히게 되고 클라이언트 프로그램은 종료됨
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
