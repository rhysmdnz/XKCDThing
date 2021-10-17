package nz.memes.xkcdthing

import javax.inject.Inject

class XKCDRepository @Inject constructor(
    private val xkcdDao: XKCDDao
){
}