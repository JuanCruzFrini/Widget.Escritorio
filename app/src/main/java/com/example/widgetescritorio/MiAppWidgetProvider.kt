package com.example.widgetescritorio

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class MiAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        for (widgetId in appWidgetIds!!){
            actualizaWidget(context!!, widgetId)
        }
    }

    fun actualizaWidget(context: Context, widgetId: Int) {
        val contador = incrementaContador(context, widgetId)
        val remoteView = RemoteViews(context.packageName, R.layout.widget)

        remoteView.setTextViewText(R.id.textView1, "Contador: $contador")
        //onclick widget
        val i:Intent = Intent(context, MainActivity::class.java)
        val pendingI = PendingIntent.getActivity(context,0 , i, 0)
        remoteView.setOnClickPendingIntent(R.id.analclock, pendingI)

        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteView)
    }

    private fun incrementaContador(context: Context, widgetId: Int): Any? {
        val prefs = context.getSharedPreferences("Contadores", Context.MODE_PRIVATE)
        var contador = prefs.getInt("contador $widgetId", 0)
        contador++
        var editor = prefs.edit()
        editor.putInt("contador $widgetId", contador)
        editor.commit()
        return contador
    }
}