package com.xanadu.calculator.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.simplemobiletools.commons.helpers.MyContextWrapper
import com.xanadu.calculator.R
import com.xanadu.calculator.helpers.*
import kotlinx.android.synthetic.main.activity_main.*
import me.grantland.widget.AutofitHelper

class MainActivity : AppCompatActivity(), Calculator {
    private lateinit var calc: CalculatorImpl

    private var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    private var isAskingPermissions = false
    private val GENERIC_PERM_HANDLER = 100

    companion object {
        var funAfterSAFPermission: ((success: Boolean) -> Unit)? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calc = CalculatorImpl(this, applicationContext)

        btn_plus.setOnClickListener { calc.handleOperation(PLUS); }
        btn_minus.setOnClickListener { calc.handleOperation(MINUS); }
        btn_multiply.setOnClickListener { calc.handleOperation(MULTIPLY); }
        btn_divide.setOnClickListener { calc.handleOperation(DIVIDE); }
        btn_percent.setOnClickListener { calc.handleOperation(PERCENT); }
        btn_power.setOnClickListener { calc.handleOperation(POWER); }
        btn_root.setOnClickListener { calc.handleOperation(ROOT); }

        btn_clear.setOnClickListener { calc.handleClear(); }
        btn_clear.setOnLongClickListener { calc.handleReset(); true }

        getButtonIds().forEach {
            it.setOnClickListener { it1 -> calc.numpadClicked(it1.id); }
        }

        btn_equals.setOnClickListener { calc.handleEquals(); }

        AutofitHelper.create(result)
        AutofitHelper.create(formula)
    }

    override fun onStop() {
        super.onStop()
        actionOnPermission = null
    }

    override fun onDestroy() {
        super.onDestroy()
        funAfterSAFPermission = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MyContextWrapper(newBase).wrap(newBase, "en"))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isAskingPermissions = false
        if (requestCode == GENERIC_PERM_HANDLER && grantResults.isNotEmpty()) {
            actionOnPermission?.invoke(grantResults[0] == 0)
        }
    }

    private fun getButtonIds() = arrayOf(btn_decimal, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9)

    override fun showNewResult(value: String, context: Context) {
        result.text = value
    }

    override fun showNewFormula(value: String, context: Context) {
        formula.text = value
    }
}
