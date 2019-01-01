package animatedledstrip.leds

/*
 *  Copyright (c) 2018 AnimatedLEDStrip
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


import animatedledstrip.ccpresets.CCBlack
import com.diozero.ws281xj.rpiws281x.WS281x
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

open class LEDStripConcurrent(var numLEDs: Int, pin: Int, private val emulated: Boolean = false) {
    var ledStrip = when (emulated) {
        true -> EmulatedWS281x(pin, 255, numLEDs)
        false -> WS281x(pin, 255, numLEDs)
    }
    private val locks = mutableMapOf<Int, Mutex>()
    private val renderLock = Mutex()

    init {
        for (i in 0 until numLEDs) locks += Pair(i, Mutex())
//        ledStrip = WS281x(pin, 255, numLEDs)
        println("numLEDs: $numLEDs")
        println("using GPIO pin: $pin")
    }

    fun isEmulated() = emulated

    fun setPixelColor(pixel: Int, colorValues: ColorContainer) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock {
                    ledStrip.setPixelColourRGB(pixel, colorValues.r, colorValues.g, colorValues.b)
                }
            }
        } catch (e: Exception) {
            println("ERROR in setPixelColor: $e")
        }
    }

    fun setPixelColor(pixel: Int, rIn: Int, gIn: Int, bIn: Int) {
        setPixelColor(pixel, ColorContainer(rIn, gIn, bIn))
    }

    fun setPixelColor(pixel: Int, hexIn: Long) {
        setPixelColor(pixel, ColorContainer(hexIn))
    }

    fun setPixelRed(pixel: Int, rIn: Int) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock {
                    ledStrip.setRedComponent(pixel, rIn)
                }
            }
        } catch (e: Exception) {
            println("ERROR in setPixelRed: $e")
        }
    }

    fun setPixelGreen(pixel: Int, gIn: Int) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock {
                    ledStrip.setGreenComponent(pixel, gIn)
                }
            }
        } catch (e: Exception) {
            println("ERROR in setPixelGreen: $e")
        }
    }

    fun setPixelBlue(pixel: Int, bIn: Int) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock {
                    ledStrip.setBlueComponent(pixel, bIn)
                }
            }
        } catch (e: Exception) {
            println("ERROR in setPixelBlue")
        }
    }

    fun setStripColor(colorValues: ColorContainer) {
        for (i in 0 until numLEDs) setPixelColor(i, colorValues.r, colorValues.g, colorValues.b)
        show()
    }

    fun setStripColor(hexIn: Long) {
        for (i in 0 until numLEDs) setPixelColor(i, hexIn)
        show()
    }

    fun setStripColor(rIn: Int, gIn: Int, bIn: Int) {
        for (i in 0 until numLEDs) ledStrip.setPixelColourRGB(i, rIn, gIn, bIn)
        show()
    }

    fun setSectionColor(start: Int, end: Int, colorValues: ColorContainer) {
        for (i in start..end) setPixelColor(i, colorValues.r, colorValues.g, colorValues.b)
        show()
    }

    fun setSectionColor(start: Int, end: Int, hexIn: Long) {
        for (i in start..end) setPixelColor(i, hexIn)
        show()
    }

    fun setSectionColor(start: Int, end: Int, rIn: Int, gIn: Int, bIn: Int) {
        for (i in start..end) ledStrip.setPixelColourRGB(i, rIn, gIn, bIn)
        show()
    }

    fun getPixelRed(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getRedComponent(pixel)
                }
            }
        } catch (e: Exception) {
            println("ERROR in getPixelRed: $e")
        }
        return 0
    }

    fun getPixelGreen(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getGreenComponent(pixel)
                }
            }
        } catch (e: Exception) {
            println("ERROR in getPixelGreen: $e")
        }
        return 0
    }

    fun getPixelBlue(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getBlueComponent(pixel)
                }
            }
        } catch (e: Exception) {
            println("ERROR in getPixelBlue: $e")
        }
        return 0
    }

    fun getPixelColor(pixel: Int): ColorContainer {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ColorContainer(ledStrip.getPixelColour(pixel).toLong())
                }
            }
        } catch (e: Exception) {
            println("ERROR in getPixelColor: $e")
        }
        println("Color not retrieved")
        return CCBlack
    }

    fun getPixelLong(pin: Int): Long {
        return getPixelColor(pin).hex
    }

    fun getPixelHexString(pin: Int): String {
        return getPixelLong(pin).toString(16)
    }

    fun getPixelColorList(): List<Long> {
        val temp = mutableListOf<Long>()
        for (i in 0 until numLEDs) temp.add(getPixelLong(i))
        return temp
    }

    // TODO: Upgrade all of these to use List and colorsFromPalette()
    // Not thread safe!
//    fun setStripFromPalette(
//        paletteType: RGBPalette16,
//        startIndex: Int,
//        blendType: TBlendType = TBlendType.LINEARBLEND,
//        brightness: Int = 255
//    ) {
//
//        var index = startIndex
//        val stringBuilder = StringBuilder()
//        for (i in 0 until ledStrip.numPixels) {
//
//            val color = colorFromPalette(paletteType, index, brightness, blendType)
//            stringBuilder.append("$i: ${color.hex.toString(16)}")
//            ledStrip.setPixelColourRGB(i, color.r, color.g, color.b)
//            index += 3
//
//        }
//
//        println(stringBuilder)
//    }
    // TODO: Convert these to work with both emulated and real LEDStrips
//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer) =
//            fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2)
//
//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer, colorValues3: ColorContainer) =
//            fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2, colorValues3)
//
//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer, colorValues3: ColorContainer, colorValues4: ColorContainer) =
//            fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2, colorValues3, colorValues4)


//    fun colorListFromPalette(pal: RGBPalette16, offset: Int) {
//        val one16 = (numLEDs / 16)
//        val two16 = ((numLEDs * 2) / 16)
//        val three16 = ((numLEDs * 3) / 16)
//        val four16 = ((numLEDs * 4) / 16)
//        val five16 = ((numLEDs * 5) / 16)
//        val six16 = ((numLEDs * 6) / 16)
//        val seven16 = ((numLEDs * 7) / 16)
//        val eight16 = ((numLEDs * 8) / 16)
//        val nine16 = ((numLEDs * 9) / 16)
//        val ten16 = ((numLEDs * 10) / 16)
//        val eleven16 = ((numLEDs * 11) / 16)
//        val twelve16 = ((numLEDs * 12) / 16)
//        val thirteen16 = ((numLEDs * 13) / 16)
//        val fourteen16 = ((numLEDs * 14) / 16)
//        val fifteen16 = ((numLEDs * 15) / 16)
//        val sixteen16 = numLEDs
//        colorsFromPalette(ledStrip, numLEDs, 0, pal[0], one16, pal[1], offset)
//        colorsFromPalette(ledStrip, numLEDs, one16, pal[1], two16, pal[2], offset)
//        colorsFromPalette(ledStrip, numLEDs, two16, pal[2], three16, pal[3], offset)
//        colorsFromPalette(ledStrip, numLEDs, three16, pal[3], four16, pal[4], offset)
//        colorsFromPalette(ledStrip, numLEDs, four16, pal[4], five16, pal[5], offset)
//        colorsFromPalette(ledStrip, numLEDs, five16, pal[5], six16, pal[6], offset)
//        colorsFromPalette(ledStrip, numLEDs, six16, pal[6], seven16, pal[7], offset)
//        colorsFromPalette(ledStrip, numLEDs, seven16, pal[7], eight16, pal[8], offset)
//        colorsFromPalette(ledStrip, numLEDs, eight16, pal[8], nine16, pal[9], offset)
//        colorsFromPalette(ledStrip, numLEDs, nine16, pal[9], ten16, pal[10], offset)
//        colorsFromPalette(ledStrip, numLEDs, ten16, pal[10], eleven16, pal[11], offset)
//        colorsFromPalette(ledStrip, numLEDs, eleven16, pal[11], twelve16, pal[12], offset)
//        colorsFromPalette(ledStrip, numLEDs, twelve16, pal[12], thirteen16, pal[13], offset)
//        colorsFromPalette(ledStrip, numLEDs, thirteen16, pal[13], fourteen16, pal[14], offset)
//        colorsFromPalette(ledStrip, numLEDs, fourteen16, pal[14], fifteen16, pal[15], offset)
//        colorsFromPalette(ledStrip, numLEDs, fifteen16, pal[15], sixteen16, pal[0], offset)
//
//        show()
//    }


    /**
     * Attempt to lock renderLock and send data to the LEDs.
     */
    fun show() {
        try {
            runBlocking {
                renderLock.tryWithLock {
                    ledStrip.render()
                }
            }
        } catch (e: Exception) {
            println("ERROR in show: $e")
        }
    }
}
