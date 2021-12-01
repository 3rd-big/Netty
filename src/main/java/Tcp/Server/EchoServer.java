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

        System.out.println("EchoServer 실행");

        /*
         * EventLoopGroup 에서 새로운 객체를 생성할 때 NioEventLoopGroup 객체를 생성함
         * parentGroup: 클라이언트의 연결을 수락하는 부모 스레드 그룹
         * NioEventLoopGroup(인수) 스레드 그룹 내에서 생성할 최대 스레드 수가 1이므로 단일 스레드
         * parentGroup: incomming connection을 액세스한다
         */
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);

        /*
         * 연결된 클라이언트 소켓으로부터 데이터 I/O 및 이벤트처리를 담당하는 자식 스레드 그룹
         * 생성자에 인수가 없으므로 CPU 코어 수에 따른 스레드의 수가 결정됨
         * childGroup: 액세스한 connection의 traffic을 처리한다.
         */
        EventLoopGroup childGroup = new NioEventLoopGroup();


        // 서버 부트스트랩 생성
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
