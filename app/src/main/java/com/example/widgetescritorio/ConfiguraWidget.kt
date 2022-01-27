package com.example.widgetescritorio

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class ConfiguraWidget : Activity(){
    var widgetId: Int = 10
    lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configura_widget)
        editText = findViewById<EditText>(R.id.et1)
        setResult(RESULT_CANCELED)
        val extras = intent.extras
        if (extras == null) finish()
        widgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) finish()
    }

    fun buttonOk(view:View){
        val cont:Int
        try {
            cont = Integer.parseInt(editText.text.toString())
        } catch (e:Exception){
            Toast.makeText(this, "No es un numero", Toast.LENGTH_SHORT).show()
            return
        }
        val prefs = getSharedPreferences("contadores", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("cont_ $widgetId", cont)
        editor.commit()
        MiAppWidgetProvider().actualizaWidget(this,widgetId)
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}