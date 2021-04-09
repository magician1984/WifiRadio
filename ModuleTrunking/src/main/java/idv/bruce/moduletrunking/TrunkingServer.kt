package idv.bruce.moduletrunking

import android.util.Log
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class TrunkingServer: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job()


    var port : Int = 9989

    var maxLinkCount : Int = 5

    var callback:TrunkingServerCallback? = null

    fun start(){

            val acceptGroup:EventLoopGroup = NioEventLoopGroup(1)
            val ioGroup :EventLoopGroup = NioEventLoopGroup()

            try{
                val bootStrap:ServerBootstrap = ServerBootstrap().apply {
                    this.group(acceptGroup, ioGroup)
                    this.channel(NioServerSocketChannel::class.java)
                    this.childHandler(object: ChannelInitializer<SocketChannel>() {
                        override fun initChannel(ch: SocketChannel?) {
                            ch!!.pipeline().addLast(StringDecoder())
                            ch!!.pipeline().addLast(DataHandler())
                        }

                    })
                    this.option(ChannelOption.SO_BACKLOG, 128)
                    this.childOption(ChannelOption.SO_KEEPALIVE, true)
                }

                val future : ChannelFuture = bootStrap.bind(port).sync()

                Log.d("Trace", "Server open : ${future.isSuccess}")
                future.channel().closeFuture().sync()

            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                acceptGroup.shutdownGracefully()
                ioGroup.shutdownGracefully()
            }

    }

    fun stop(){
        coroutineContext.cancel()
    }

    private inner class DataHandler: ChannelInboundHandlerAdapter() {
        override fun channelActive(ctx: ChannelHandlerContext?) {
            super.channelActive(ctx)
            Log.d("Trace", "Channel active")
        }

        override fun channelInactive(ctx: ChannelHandlerContext?) {
            super.channelInactive(ctx)
            Log.d("Trace", "Channel inactive")
        }

        override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
            Log.d("Trace", "Read")
            super.channelRead(ctx, msg)
        }

    }
}