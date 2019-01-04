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


import animatedledstrip.ccpresets.*
import com.diozero.ws281xj.PixelAnimations.delay
import kotlinx.coroutines.*
import java.lang.Math.random


/**
 * Helper enum that determines if an animation should appear to move 'Forward'
 * or 'Backward'.
 *
 * The start of the LED strip is the end where the signal is connected (small
 * arrows on strip pointing away from it).
 * The end of the LED strip is the opposite end from the start (small arrows on
 * strip point towards it).
 */
enum class Direction {
    /**
     * Animation appears to move from the start to the end of the strip.
     */
    FORWARD,
    /**
     * Animation appears to move from the end to the start of the strip.
     */
    BACKWARD
}

class AnimatedLEDStrip(numLEDs: Int, pin: Int) : LEDStrip(numLEDs, pin) {

    private var shuffleArray = mutableListOf<Int>()

    init {
        for (i in 0 until numLEDs) shuffleArray.add(i)
    }

    fun alternate(colorValues1: ColorContainer, colorValues2: ColorContainer, delayTime: Int) {
        setStripColor(colorValues1)
        delay(delayTime)
        setStripColor(colorValues2)
        delay(delayTime)
    }

    fun alternate(r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delayTime: Int) =
        alternate(ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delayTime)

    fun fadePixelRed(pixel: Int, startIntensity: Int, endIntensity: Int, revertAtCompletion: Boolean) {
        val fadeDirection: Int = if (startIntensity > endIntensity) -1 else if (startIntensity < endIntensity) 1 else 0

        val originalValues = getPixelColor(pixel)

        when (fadeDirection) {
            1 -> for (i in startIntensity..endIntensity) {
                setPixelRed(pixel, i)
                show()
            }
            -1 -> for (i in startIntensity downTo endIntensity) {
                setPixelRed(pixel, i)
                show()
            }
            0 -> return
        }

        if (revertAtCompletion) {
            setPixelColor(pixel, originalValues)
            show()
        }


    }

    fun fadePixelGreen(pixel: Int, startIntensity: Int, endIntensity: Int, revertAtCompletion: Boolean) {
        val fadeDirection: Int = if (startIntensity > endIntensity) -1 else if (startIntensity < endIntensity) 1 else 0

        val originalValues = getPixelColor(pixel)

        when (fadeDirection) {
            1 -> for (i in startIntensity..endIntensity) {
                setPixelGreen(pixel, i)
                show()
            }
            -1 -> for (i in startIntensity downTo endIntensity) {
                setPixelGreen(pixel, i)
                show()
            }
            0 -> return
        }

        if (revertAtCompletion) {
            setPixelColor(pixel, originalValues)
            show()
        }

    }

    fun fadePixelBlue(pixel: Int, startIntensity: Int, endIntensity: Int, revertAtCompletion: Boolean) {
        val fadeDirection: Int = if (startIntensity > endIntensity) -1 else if (startIntensity < endIntensity) 1 else 0

        val originalValues = getPixelColor(pixel)

        when (fadeDirection) {
            1 -> for (i in startIntensity..endIntensity) {
                setPixelBlue(pixel, i)
                show()
            }
            -1 -> for (i in startIntensity downTo endIntensity) {
                setPixelBlue(pixel, i)
                show()
            }
            0 -> return
        }

        if (revertAtCompletion) {
            setPixelColor(pixel, originalValues)
            show()
        }

    }

    fun fadePixelAll() {
        //Todo: fill in function
    }

    fun multiPixelRun(
        spacing: Int,
        chaseDirection: Direction,
        colorValues1: ColorContainer,
        colorValues2: ColorContainer = CCBlack
    ) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay(100)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues2
                )
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay(100)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues2
                )
            }
        }
    }

    fun multiPixelRun(
        spacing: Int,
        chaseDirection: Direction,
        r1In: Int,
        g1In: Int,
        b1In: Int,
        r2In: Int,
        g2In: Int,
        b2In: Int
    ) = multiPixelRun(spacing, chaseDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In))

    fun multiPixelRunToColor(spacing: Int, chaseDirection: Direction, colorValues1: ColorContainer) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay(150)
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay(150)
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        }
    }

    fun multiPixelRunToColor(spacing: Int, chaseDirection: Direction, r1In: Int, g1In: Int, b1In: Int) =
        multiPixelRunToColor(spacing, chaseDirection, ColorContainer(r1In, g1In, b1In))

    fun pixelRun(movementDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack) {
        setStripColor(colorValues2)
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                setPixelColor(q, colorValues1)
                show()
                delay(50)
                setPixelColor(q, colorValues2)
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                setPixelColor(q, colorValues1)
                show()
                delay(50)
                setPixelColor(q, colorValues2)
            }
        }
    }

    fun pixelRun(movementDirection: Direction, r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int) =
        pixelRun(movementDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In))

    fun pixelRunWithTrail(
        movementDirection: Direction,
        colorValues1: ColorContainer,
        colorValues2: ColorContainer = CCBlack
    ) {
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                for (i in 0 until ledStrip.numPixels - 1) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay(50)
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels - 1) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay(50)
            }
        }
    }

    fun pixelRunWithTrail(
        movementDirection: Direction,
        r1In: Int,
        g1In: Int,
        b1In: Int,
        r2In: Int,
        g2In: Int,
        b2In: Int
    ) = pixelRunWithTrail(movementDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In))


    fun sparkle(sparkleColor: ColorContainer) {
        var originalColor: ColorContainer
        shuffleArray.shuffle()
        for (i in 0 until ledStrip.numPixels) {
            originalColor = getPixelColor(shuffleArray[i])
            setPixelColor(shuffleArray[i], sparkleColor)
            show()
            delay(50)
            setPixelColor(shuffleArray[i], originalColor)
        }
    }

    fun sparkle(rIn: Int, gIn: Int, bIn: Int) = sparkle(ColorContainer(rIn, gIn, bIn))

    fun sparkleCC(sparkleColor: ColorContainer) {
        val deferred = (0 until ledStrip.numPixels).map { n ->
            GlobalScope.async {
                val originalColor: ColorContainer = getPixelColor(n)
                delay((random() * 100000 % 4950).toInt())
                setPixelColor(n, sparkleColor)
                show()
                delay(50)
                setPixelColor(n, originalColor)
            }
        }
        runBlocking {
            deferred.awaitAll()
        }
    }

    fun sparkleCC(rIn: Int, gIn: Int, bIn: Int) = sparkleCC(ColorContainer(rIn, gIn, bIn))

    fun sparkleToColor(destinationColor: ColorContainer, delay: Int = 50) {
        shuffleArray.shuffle()
        for (i in 0 until ledStrip.numPixels) {
            setPixelColor(shuffleArray[i], destinationColor)
//            println("Test $i")
            show()
            delay(delay)
        }
    }

    fun sparkleToColor(rIn: Int, gIn: Int, bIn: Int) = sparkleToColor(ColorContainer(rIn, gIn, bIn))

    fun sparkleToColorCC(sparkleColor: ColorContainer) {
        shuffleArray.shuffle()
        shuffleArray.forEach { n ->
            GlobalScope.launch {
                Thread.sleep((random() * 100000 % 50).toLong())
//                println(n)
                setPixelColor(n, sparkleColor)
                show()
//                Thread.sleep(50)
            }
        }
//        runBlocking {
//            deferred.awaitAll()
//        }
    }

    fun sparkleToColorCC(rIn: Int, gIn: Int, bIn: Int) = sparkleToColorCC(ColorContainer(rIn, gIn, bIn))

    fun stack(stackDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack) {
        if (stackDirection == Direction.FORWARD) {
            setStripColor(colorValues2)
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until q) {
                    setPixelColor(i, colorValues1)
                    show()
                    delay(10)
                    setPixelColor(i, colorValues2)
                }
                setPixelColor(q, colorValues1)
                show()
            }
        } else if (stackDirection == Direction.BACKWARD) {
            setStripColor(colorValues2)
            for (q in 0 until ledStrip.numPixels) {
                for (i in q - 1 downTo 0) {
                    setPixelColor(i, colorValues1)
                    show()
                    delay(10)
                    setPixelColor(i, colorValues2)
                }
                setPixelColor(q, colorValues1)
                show()
            }
        }
    }

    fun wipe(colorValues: ColorContainer, wipeDirection: Direction) {
        if (wipeDirection == Direction.BACKWARD) {
            for (i in ledStrip.numPixels - 1 downTo 0) {
                setPixelColor(i, colorValues)
                show()
                delay(10)
            }
        } else if (wipeDirection == Direction.FORWARD) {
            for (i in 0 until ledStrip.numPixels) {
                setPixelColor(i, colorValues)
                show()
                delay(10)
            }
        }
    }

    fun wipe(rIn: Int, gIn: Int, bIn: Int, wipeDirection: Direction) =
        wipe(ColorContainer(rIn, gIn, bIn), wipeDirection)


}