package idv.bruce.moduletrunking

interface TrunkingServerCallback {
    fun onReady()
    fun onReadData(data:ByteArray)
    fun onLinkIn(address:String)
}