package idv.bruce.moduletrunking

import idv.bruce.moduletrunking.structure.TGroup
import idv.bruce.wifiradio.kernel.Group

internal class Repository {
    val mGroups:HashMap<Int, TGroup> = HashMap()

    private var multiCastIndex:Int = 0

    fun createGroups(vararg groups:Group){
        groups
    }
}