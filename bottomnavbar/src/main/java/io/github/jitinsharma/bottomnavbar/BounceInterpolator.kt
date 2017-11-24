package io.github.jitinsharma.bottomnavbar

import android.view.animation.Interpolator

/**
 * Created by jsharma on 24/11/17.
 */

class BounceInterpolator : Interpolator {
    private var amplitude : Double = 1.0
    private var frequency : Double = 10.0

    constructor(amplitude : Double, frequency : Double) {
        this.amplitude = amplitude
        this.frequency = frequency
    }
    override fun getInterpolation(input: Float): Float =
            (-1 * Math.pow(Math.E, -input/amplitude) * Math.cos(frequency * input) + 1).toFloat()

}