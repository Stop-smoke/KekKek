package com.stopsmoke.kekkek.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.google.firebase.firestore.FirebaseFirestore
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.MainActivity

class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_smoketime)

            // Firestore에서 데이터 가져오기
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document("user_id")  // user_id를 실제 사용자 ID로 바꾸어야 함
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val totalMinutesTime = document.getLong("totalMinutesTime") ?: 0
                        val formattedTime = formatElapsedTime(totalMinutesTime)
                        views.setTextViewText(R.id.widget_time, formattedTime)
                    } else {
                        views.setTextViewText(R.id.widget_time, "Error loading data")
                    }
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .addOnFailureListener {
                    views.setTextViewText(R.id.widget_time, "Error loading data")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }

            // 위젯 클릭 시 앱 열기
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_smoketime, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun formatElapsedTime(totalMinutes: Long): String {
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            return "${hours}시간 ${minutes}분"
        }
    }
}
