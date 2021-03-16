package gr.ppzglou.food.ext

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import gr.ppzglou.food.databinding.FragmentChangePhoneBinding

import gr.ppzglou.food.databinding.FragmentStepperPhoneBinding

class GenericKeyEvent internal constructor(
    private val currentView: EditText,
    private val previousView: EditText?,
    private val binding: FragmentStepperPhoneBinding
) : View.OnKeyListener {
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView != binding.ed1 && currentView.text.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }
}

class GenericTextWatcher internal constructor(
    private val currentView: View,
    private val nextView: View?,
    private val binding: FragmentStepperPhoneBinding
) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
        val text = editable.toString()
        when (currentView) {
            binding.ed1 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed2 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed3 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed4 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed5 -> if (text.length == 1) nextView!!.requestFocus()
            //You can use EditText4 same as above to hide the keyboard
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }
}

class GenericKeyEventChangePhone internal constructor(
    private val currentView: EditText,
    private val previousView: EditText?,
    private val binding: FragmentChangePhoneBinding
) : View.OnKeyListener {
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView != binding.ed1 && currentView.text.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }
}

class GenericTextWatcherChangePhone internal constructor(
    private val currentView: View,
    private val nextView: View?,
    private val binding: FragmentChangePhoneBinding
) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
        val text = editable.toString()
        when (currentView) {
            binding.ed1 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed2 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed3 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed4 -> if (text.length == 1) nextView!!.requestFocus()
            binding.ed5 -> if (text.length == 1) nextView!!.requestFocus()
            //You can use EditText4 same as above to hide the keyboard
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }
}