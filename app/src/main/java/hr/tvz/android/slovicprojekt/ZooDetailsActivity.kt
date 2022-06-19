package hr.tvz.android.slovicprojekt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ZooDetailsActivity : AppCompatActivity() {

    var ZOO_KANAL = "zooKanal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoo_details)

        Objects.requireNonNull(getSupportActionBar())?.hide()

        val channel = NotificationChannel(
            ZOO_KANAL,
            "Ime kanala",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Opis kanala"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Handle calls
        val callButton = findViewById<Button>(R.id.call_button)
        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:01 2302 198"))
            startActivity(intent)
        }

        // Handle reservation and send notification
        val reserveButton = findViewById<Button>(R.id.reserve_button)
        reserveButton.setOnClickListener {
            Toast.makeText(
                getApplicationContext(),
                "You reserved your ticket successfully!",
                Toast.LENGTH_SHORT
            ).show();

            // Build notification
            val builder = Notification.Builder(this, ZOO_KANAL)
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        android.R.drawable.ic_menu_week
                    )
                )
                .setContentTitle("Reservation ID: " + Random().nextInt(10000000))
                .setContentText("Your reservation is valid for one week.")


            // On notification click, redirect to main activity
            val resultIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                resultIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            builder.setContentIntent(pendingIntent)

            var notification: Notification = builder.build()
            var notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
    }
}