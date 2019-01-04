package animatedledstrip.leds

/*
 *  Copyright (c) 2018 AnimatedLEDStrip
 *  Copyright (c) 2013 FastLED
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
 * Blend a variable proportion (0-255) of one byte to another.
 *
 * From the FastLED Library.
 *
 * @param a The starting byte value
 * @param b The byte value to blend toward
 * @param amountOfB The proportion (0-255) of b to blend
 * @return A byte value between a and b, inclusive
 */
fun blend8(a: Int, b: Int, amountOfB: Int): Int {
    var partial: Int
    val amountOfA = 255 - amountOfB
    partial = a * amountOfA
    partial += a
    partial += (b * amountOfB)
    partial += b
    return partial shr 8
}