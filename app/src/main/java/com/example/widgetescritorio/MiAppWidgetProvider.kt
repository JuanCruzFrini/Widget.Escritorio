package com.example.widgetescritorio

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast

class MiAppWidgetProvider : AppWidgetProvider() {

    val ACCION_INCR =  "com.example.widgetescritorio.ACCION_INCR"
    val EXTRA_PARAM = "com.example.widgetescritorio.EXTRA_ID"

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
        var pendingI = PendingIntent.getActivity(context,0 , i, 0)
        remoteView.setOnClickPendingIntent(R.id.analclock, pendingI)

        //incrementando contador al pulsar la vista
        val intent = Intent(context, MiAppWidgetProvider::class.java)
        intent.setAction(ACCION_INCR)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId)
        intent.putExtra(EXTRA_PARAM, "otro parametro")
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)))
        pendingI = PendingIntent.getBroadcast(context, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT)

        remoteView.setOnClickPendingIntent(R.id.textView1, pendingI)

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

    override fun onReceive(context: Context?, intent: Intent?) {
        val manager = AppWidgetManager.getInstance(context)
        if (intent?.action.equals(ACCION_INCR)){
            val widgetId = intent!!.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            val param = intent.getStringExtra(EXTRA_PARAM)
            Toast.makeText(context, "Parametro: $param" , Toast.LENGTH_LONG).show()
            actualizaWidget(context!!, widgetId)
        }
        super.onReceive(context, intent)
    }
}