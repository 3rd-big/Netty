package Tcp.Client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	private static final String HOST = "127.0.0.1";

    private static int PORT = 50000;
//	private static int PORT = 51206;

    public static void main(String[] args) {

//		if(args[0] != null) {
//			PORT = Integer.parseInt(args[0]);
//		}

        // EventLoopGroup ���� ���ο� ��ü�� ������ �� NioEventLoopGroup ��ü�� ����
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(clientGroup)				// EventLoopGroup ����( ������ �ٸ��� 1���� ��� - ����� ä���� �ϳ��� ����)
                    .channel(NioSocketChannel.class)	// ä���� ���� ����(NIO ���� ä�η� ����)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        // ä�� ���������� ������ �Ϲ� ���� ä�� Ŭ������ SocketChannel ����
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoClientHandler());

                        }
                    });

            ChannelFuture f = b.connect(HOST, PORT).sync();

            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}
