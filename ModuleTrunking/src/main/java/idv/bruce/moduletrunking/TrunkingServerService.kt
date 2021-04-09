package idv.bruce.moduletrunking

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.CharsetUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TrunkingServerService:Service() {
    companion object{
        var defaultDisplayName : String = "Trunking server"
    }

    var port : Int = 8080

    var maxLinkCount : Int = 5

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            start()
        }
    }

    private suspend  fun start(){

        val acceptGroup: EventLoopGroup = NioEventLoopGroup(1)
        val ioGroup : EventLoopGroup = NioEventLoopGroup()

        try{
            val bootStrap: ServerBootstrap = ServerBootstrap().apply {
                this.group(acceptGroup, ioGroup)
                this.channel(NioServerSocketChannel::class.java)
                this.childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel?) {
                        ch!!.pipeline().addLast(DataHandler())
                    }
                })
                this.option(ChannelOption.SO_BACKLOG, 128)
                this.childOption(ChannelOption.SO_KEEPALIVE, true)
            }

            val future : ChannelFuture = bootStrap.bind(port).sync()

            Log.d("Trace", "Server open : ${future.isSuccess}")
            future.channel().closeFuture().sync()

        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            acceptGroup.shutdownGracefully()
            ioGroup.shutdownGracefully()
        }

    }

    private inner class DataHandler: ChannelInboundHandlerAdapter() {

        override fun channelActive(ctx: ChannelHandlerContext?) {
            Log.d("Trace", "Channel active : ${ctx?.channel()?.remoteAddress()}")
            super.channelActive(ctx)
        }

        override fun channelInactive(ctx: ChannelHandlerContext?) {
            Log.d("Trace", "Channel inactive : ${ctx?.channel()?.remoteAddress()}")
            super.channelInactive(ctx)
        }

        override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
            val inBuffer = msg as ByteBuf

            val received = inBuffer.toString(CharsetUtil.UTF_8)
            Log.d("Trace", "Read $received")
        }
    }
}