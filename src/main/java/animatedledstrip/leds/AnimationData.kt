package animatedledstrip.leds

/*
 *  Copyright (c) 2019 AnimatedLEDStrip
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */


/**
 * Class for creating and sending a map to the server to start an animation.
 *
 */
class AnimationData(val params: Map<*, *>) {


    init {
        try {
            params["Animation"]!!
        } catch (e: Exception) {
            println("Animation not defined $e")
        }
    }

    var animation: Animation = params["Animation"] as Animation

    var color1: Long = params["Color1"] as Long? ?: 0x0

    var color2: Long = params["Color2"] as Long? ?: when (animationInfoMap[animation]?.color2) {
        ReqLevel.OPTIONAL -> animationInfoMap[animation]?.color2Default!!.hex
        ReqLevel.REQUIRED -> throw Exception()
        ReqLevel.NOTUSED -> 0x0
        null -> 0x0
    }

    var color3: Long = params["Color3"] as Long? ?: 0x0

    var color4: Long = params["Color4"] as Long? ?: 0x0

    var color5: Long = params["Color5"] as Long? ?: 0x0

    var colorList = mutableListOf<ColorContainer>()

    init {
        if (params["ColorList"] as List<*>? != null) {
            val tempList = params["ColorList"] as List<*>
            tempList.forEach { c -> colorList.add(ColorContainer(c as Long)) }
        }
    }

    var continuous: Boolean = params["Continuous"] as Boolean? ?: false

    var delay: Int = params["Delay"] as Int? ?: when(animationInfoMap[animation]?.delay) {
        ReqLevel.REQUIRED -> throw Exception()
        ReqLevel.OPTIONAL -> animationInfoMap[animation]?.delayDefault!!
        ReqLevel.NOTUSED -> 0
        null -> 0
    }

    var direction: Direction = if (params["Direction"] as Char? != null) {
        when (params["Direction"] as Char) {
            'F', 'f' -> Direction.FORWARD
            'B', 'b' -> Direction.BACKWARD
            else -> Direction.FORWARD
        }
    } else Direction.FORWARD

    var id: String = params["ID"] as String? ?: ""

    var spacing: Int = params["Spacing"] as Int? ?: 3

    override fun toString() = "$animation: $color1, $color2, $color3, $color4, $color5, $colorList, $continuous, $delay, $direction, $id, $spacing"

    fun toMap() = params
}