/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.fleepgeek.bullseyesample

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fleepgeek.bullseyesample.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
  private var selectedValue = 50
  private var targetValue = newTargetValue()
  private var totalScore = 0
  private var currentRound = 1

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
//    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    installSplashScreen()
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    startNewGame()

    binding.hitMeButton.setOnClickListener {
      showResult()
      totalScore += pointsForCurrentRound()
      binding.gameScoreTextView.text = totalScore.toString()
    }

    binding.startOverButton.setOnClickListener {
      startNewGame()
    }

    binding.infoButton.setOnClickListener {
      navigateToAboutPage()
    }

    binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        selectedValue = progress
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) {}

      override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    })
  }

  private fun differenceAmount() = abs(targetValue - selectedValue)

  private fun newTargetValue() = Random.nextInt(1, 100)

  private fun pointsForCurrentRound(): Int {
    val maxScore = 100
    val difference = differenceAmount()
    var bonus = 0
    if (difference == 0) {
      bonus = 100
    } else if (difference == 1) {
      bonus = 50
    }
    return maxScore - difference + bonus
  }

  private fun startNewGame() {
    totalScore = 0
    currentRound = 1
    selectedValue = 50
    targetValue = newTargetValue()

    binding.gameScoreTextView.text = totalScore.toString()
    binding.gameRoundTextView.text = currentRound.toString()
    binding.targetTextView.text = targetValue.toString()
    binding.seekBar.progress = selectedValue
  }

  private fun showResult() {
    val dialogTitle = alertTitle()
    val dialogMessage =
      getString(R.string.result_dialog_message, selectedValue, pointsForCurrentRound())
    val builder = AlertDialog.Builder(this)

    builder.setTitle(dialogTitle)
    builder.setMessage(dialogMessage)
    builder.setPositiveButton(R.string.result_dialog_button_text) { dialog, _ ->
      dialog.dismiss()
      targetValue = newTargetValue()
      binding.targetTextView.text = targetValue.toString()

      currentRound += 1
      binding.gameRoundTextView.text = currentRound.toString()
    }

    builder.create().show()
  }

  private fun alertTitle(): String {
    var difference = differenceAmount()

    val title: String = if (difference == 0) {
      getString(R.string.alert_title_1)
    } else if (difference < 5) {
      getString(R.string.alert_title_2)
    } else if (difference <= 10) {
      getString(R.string.alert_title_3)
    } else {
      getString(R.string.alert_title_4)
    }
    return title
  }

  private fun navigateToAboutPage() {
    val intent = Intent(this, AboutActivity::class.java)
    startActivity(intent)
  }
}