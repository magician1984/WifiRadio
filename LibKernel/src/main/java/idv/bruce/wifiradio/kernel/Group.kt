package idv.bruce.wifiradio.kernel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Group(var id : Int = -1, var type:Int = -1, var members:ArrayList<Member>? = null):Parcelable