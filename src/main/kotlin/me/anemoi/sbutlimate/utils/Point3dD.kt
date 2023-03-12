package me.anemoi.sbutlimate.utils

import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3

class Point3dD {
    var x: Double
    var y: Double
    var z: Double

    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(pos: BlockPos) {
        x = pos.x.toDouble()
        y = pos.y.toDouble()
        z = pos.z.toDouble()
    }

    constructor(vec: Vec3) {
        x = vec.xCoord
        y = vec.yCoord
        z = vec.zCoord
    }

    //get as blockpos
    val blockPos: BlockPos
        get() = BlockPos(x, y, z)

    //get as vec3d
    val vec3d: Vec3
        get() = Vec3(x, y, z)

    //add
    fun add(x: Double, y: Double, z: Double): Point3dD {
        return Point3dD(this.x + x, this.y + y, this.z + z)
    }
}