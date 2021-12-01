package Tcp.Server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    private static int PORT = 50000;

    public static void main(String[] args) {

        if (args.length != 0) {
            if(args[0] != null) {
                PORT = Integer.parseInt(args[0]);
            }
        }

        System.out.println("EchoServer ����");

        /*
         * EventLoopGroup ���� ���ο� ��ü�� ������ �� NioEventLoopGroup ��ü�� ������
         * parentGroup: Ŭ���̾�Ʈ�� ������ �����ϴ� �θ� ������ �׷�
         * NioEventLoopGroup(�μ�) ������ �׷� ������ ������ �ִ� ������ ���� 1�̹Ƿ� ���� ������
         * parentGroup: incomming connection�� �׼����Ѵ�
         */
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);

        /*
         * ����� Ŭ���̾�Ʈ �������κ��� ������ I/O �� �̺�Ʈó���� ����ϴ� �ڽ� ������ �׷�
         * �����ڿ� �μ��� �����Ƿ� CPU �ھ� ���� ���� �������� ���� ������
         * childGroup: �׼����� connection�� traffic�� ó���Ѵ�.
         */
        EventLoopGroup childGroup = new NioEventLoopGroup();


        // ���� ��Ʈ��Ʈ�� ����
        try {

            ServerBootstrap sb = new ServerBootstrap();

            sb.group(parentGroup, childGroup)

                    .channel(NioServerSocketChannel.class)

                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline cp = sc.pipeline();
                            cp.addLast(new EchoServerHandler());
                        }

                    });

            ChannelFuture cf = sb.bind(PORT).sync();

            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
