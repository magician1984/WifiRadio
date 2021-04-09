package idv.bruce.wifiradio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import idv.bruce.moduletrunking.TrunkingServer
import idv.bruce.moduletrunking.TrunkingServerCallback
import idv.bruce.moduletrunking.TrunkingServerService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val trunkingServer:TrunkingServer = TrunkingServer().also {
        it.callback = object:TrunkingServerCallback{
            override fun onReady() {

            }

            override fun onReadData(data: ByteArray) {

            }

            override fun onLinkIn(address: String) {

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, TrunkingServerService::class.java))
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this,TrunkingServerService::class.java))
    }

    private fun showLog(msg:String){
        MainScope().launch {
            findViewById<TextView>(R.id.txt_msg).text =
                findViewById<TextView>(R.id.txt_msg).text.toString() + "\n" + msg
        }
    }
}