package idv.bruce.wifiradio.kernel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Member(var id : Int = -1, var type : Int = -1, var state:Int = -1):Parcelable