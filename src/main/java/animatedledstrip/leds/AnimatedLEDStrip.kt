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


/**
 * A subclass of [LEDStrip] adding animations
 *
 * @param numLEDs Number of leds in the strip
 * @param pin GPIO pin connected for signal
 * @param emulated Is this strip real or emulated?
 */
class AnimatedLEDStrip(numLEDs: Int, pin: Int, private val emulated: Boolean = false) : LEDStrip(numLEDs, pin) {


    /**
     * Array used for shuffle animation
     */
    private var shuffleArray = mutableListOf<Int>()

    init {
        for (i in 0 until numLEDs) shuffleArray.add(i)      // Initialize shuffleArray
    }


    /**
     * Function to run an Alternate animation.
     *
     * Strip alternates between two colors at the specified rate (delay between changes).
     *
     * @param colorValues1 First color to be displayed
     * @param colorValues2 Second color to be displayed
     * @param delayTime Delay in milliseconds before color changes from first to
     * second color and between second color and returning
     */
    fun alternate(colorValues1: ColorContainer, colorValues2: ColorContainer, delayTime: Int) {
        setStripColor(colorValues1)
        delay(delayTime)
        setStripColor(colorValues2)
        delay(delayTime)
    }


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


    /**
     * Function to run a Multi Pixel Run animation.
     *
     * Similar to Pixel Run but with multiple leds at a specified spacing.
     *
     * @param spacing Spacing between lit leds (for example, if spacing is 3
     * and led 0 is lit, led 3 will also be lit)
     * @param chaseDirection [Direction] of animation
     * @param colorValues1 Color of moving pixels
     * @param colorValues2 Color of background pixels
     */
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


    /**
     * A non-repetitive function to run a Multi Pixel Run To Color animation.
     *
     * @param spacing Spacing between lit leds (for example, if spacing is 3
     * and led 0 is lit, led 3 will also be lit)
     * @param chaseDirection [Direction] of animation
     * @param colorValues1 Color of moving pixels and color strip will
     * be at end of animation
     */
    fun multiPixelRunToColor(spacing: Int, chaseDirection: Direction, colorValues1: ColorContainer) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay(150)
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay(150)
            }
        }
    }


    /**
     * Function to run a Pixel Run animation.
     *
     * The strip is set to colorValues2, then a pixel 'runs' along the strip.
     * Similar to Multi Pixel Run but with only one pixel.
     *
     * @param movementDirection [Direction] of animation
     * @param colorValues1 Color of 'running' pixel
     * @param colorValues2 Background color
     */
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
                show()
                delay(50)
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels - 1) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                show()
                delay(50)
            }
        }
    }


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


    fun sparkleToColor(destinationColor: ColorContainer, delay: Int = 50) {
        shuffleArray.shuffle()
        for (i in 0 until ledStrip.numPixels) {
            setPixelColor(shuffleArray[i], destinationColor)
            show()
            delay(delay)
        }
    }


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

}
