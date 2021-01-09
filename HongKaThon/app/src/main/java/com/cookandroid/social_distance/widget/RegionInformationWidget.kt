package com.cookandroid.social_distance.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.cookandroid.social_distance.R
import com.cookandroid.social_distance.gps.GpsTracker
import com.cookandroid.social_distance.gps.Region
import com.cookandroid.social_distance.singleton.CoronaData

class RegionInformationWidget : AppWidgetProvider() {
    companion object {
        const val ACTION_REFRESH = "action_refresh"
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action) {
            ACTION_REFRESH -> {
                Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show()
                update(context, AppWidgetManager.getInstance(context), ComponentName(context, RegionInformationWidget::class.java))
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        update(context, appWidgetManager, appWidgetIds)
    }

    private fun update(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetManager.updateAppWidget(appWidgetIds, getUpdatedView(context))
    }

    private fun update(context: Context, appWidgetManager: AppWidgetManager, componentName: ComponentName) {
        appWidgetManager.updateAppWidget(componentName, getUpdatedView(context))
    }

    private fun getUpdatedView(context: Context): RemoteViews {
        val intent = Intent(context, RegionInformationWidget::class.java).setAction(ACTION_REFRESH)
        val pending = PendingIntent.getBroadcast(context, 0, intent, 0)

        return RemoteViews(context.packageName, R.layout.widget_region_information).apply {
            val region = GpsTracker(context).getArea()

            setTextViewText(R.id.region, region.korean)
            setTextViewText(R.id.total, "${CoronaData.getTotalInfection(region)}명")
            setTextViewText(R.id.plus, "+${CoronaData.getPlusInfection(region)}명")
            setTextViewText(R.id.level, "${CoronaData.getLevel(region)}단계")
            setOnClickPendingIntent(R.id.refresh, pending)
        }
    }
}