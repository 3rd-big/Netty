package Https.Server.ssl;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.io.File;

public class HttpsServer {

    private int port;

    public HttpsServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        SslContext sslCtx = null;

        try {
//    		File certChainFile = new File("C:\\Users\\Seo\\Server.cer");
//    		File keyFile = new File("C:\\Users\\Seo\\server_private222.pkcs8");
//    		File keyFile = new File("C:\\Users\\Seo\\certificatename.pkcs8");
            File certChainFile = new File("C:\\\\Users\\\\Seo\\\\Desktop\\\\Direa\\\\HTTPS\\\\lfvn.cer");
            File keyFile = new File("C:\\Users\\Seo\\Desktop\\Direa\\HTTPS\\fep.lottefinance.vn.pkcs8");

//    		File certChainFile = new File("/home/cruzdvl/ssl/lfvn.cer");
//    		File keyFile = new File("/home/cruzdvl/ssl/fep.lottefinance.vn.pkcs8");

//    		sslCtx = SslContext.newServerContext(certChainFile, keyFile);	// 아랫줄 테스트 ... 되네..?

            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

        }catch (Exception e) {
            e.printStackTrace();
        }

        int port = args.length > 0 ? Integer.parseInt(args[0]) : 32433;

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpsServerInitializer(sslCtx));
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}