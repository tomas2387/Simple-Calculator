package com.xanadu.calculator.extensions

import android.content.Context
import com.xanadu.calculator.helpers.Config

val Context.config: Config get() = Config.newInstance(applicationContext)

