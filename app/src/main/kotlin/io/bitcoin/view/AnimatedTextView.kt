package io.bitcoin.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeConverter
import android.animation.TypeEvaluator
import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.Keep
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Property
import android.widget.TextView
import io.bitcoin.R
import java.text.NumberFormat

class AnimatedTextView @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
	private val animationDuration = this.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
	private val typeEvaluator = TypeEvaluator<Double> { fraction, startValue, endValue ->
		startValue + (endValue - startValue) * fraction
	}

	private var defaultTextColor = ContextCompat.getColor(this.context, R.color.text)
	private var value = 0.0

	@Keep
	override fun setTextColor(color: Int) {
		super.setTextColor(color)

		this.defaultTextColor = color
	}

	fun setValue(value: Double, formatter: NumberFormat) {
		val textColor = when {
			value < this.value -> ContextCompat.getColor(this.context, R.color.ask)
			value > this.value -> ContextCompat.getColor(this.context, R.color.bid)
			else -> this.defaultTextColor
		}

		this.animateChange(value, textColor, formatter)
	}

	private fun animateChange(value: Double, @ColorInt targetTextColor: Int, formatter: NumberFormat) {
		this.clearAnimation()

		val currentTextColor = this.currentTextColor
		val textAnimator = ObjectAnimator.ofObject<AnimatedTextView, Double, CharSequence>(
				this, TextViewTextProperty<AnimatedTextView>(), Converter(Double::class.java, formatter), this.typeEvaluator, this.value, value
		).setDuration(this.animationDuration)

		val animatorSet = AnimatorSet()

		if (currentTextColor == targetTextColor) {
			animatorSet.play(textAnimator)
		} else {
			val colorIn = ObjectAnimator.ofArgb(this, "textColor", currentTextColor, targetTextColor)
					.setDuration(this.animationDuration)
			val colorOut = ObjectAnimator.ofArgb(this, "textColor", targetTextColor, currentTextColor)
					.setDuration(this.animationDuration)

			animatorSet.play(colorIn).with(textAnimator).before(colorOut)
		}

		animatorSet.start()

		this.value = value
	}

	private class Converter<T>(fromClass: Class<T>, private val formatter: NumberFormat) : TypeConverter<T, CharSequence>(fromClass, CharSequence::class.java) {
		override fun convert(value: T): String = this.formatter.format(value)
	}

	private class TextViewTextProperty<T : TextView> : Property<T, CharSequence>(CharSequence::class.java, "text") {
		override fun get(`object`: T): CharSequence? = `object`.text

		override fun set(`object`: T, value: CharSequence?) {
			`object`.text = value
		}
	}
}
