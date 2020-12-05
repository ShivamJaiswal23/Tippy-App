package com.example.tiptap

import android.animation.ArgbEvaluator
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar.progress = INITIAL_TIP_PERCENT
        tipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//              tipPercent.setText("$progress%")
                tipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
                Log.i(TAG , "onProgressChanged $progress" )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription: String
        when (tipPercent){
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great"
            else -> tipDescription = "Amazing"
       }
        description.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBar.max,
            ContextCompat.getColor(this , R.color.colorWorst),
            ContextCompat.getColor(this , R.color.colorBest)
        ) as Int
        description.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (editText.text.isEmpty()){
            tipAmount.text = ""
            totalAmount.text = ""
            return
        }
        val baseAmount = editText.text.toString().toDouble()
        val tipPercent = seekBar.progress
        val tipamount = baseAmount * tipPercent / 100
        val totalamount = baseAmount + tipamount
        tipAmount.text = "%.2f".format(tipamount)
        totalAmount.text = "%.2f".format(totalamount)
    }
}

