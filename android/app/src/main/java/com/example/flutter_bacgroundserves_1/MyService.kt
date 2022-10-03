package com.example.flutter_bacgroundserves_1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class MyService : Service() {
//
//    override fun onCreate() {
//        super.onCreate()
//        Log.e("ALAA","Thread ")
//        Toast.makeText(this, "From Flutter", Toast.LENGTH_SHORT).show()
//    }
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Toast.makeText(this, "From Flutter", Toast.LENGTH_SHORT).show()
//        Log.e("ALAA","Thread ")
//
////        for (i in 1..100) {
////            Thread {
////                if (i % 2 == 0)
////                    return@Thread
////                println("Thread $i")
////                Log.e("ALAA","Thread $i")
////            }.run()
////        }
//
//        return super.onStartCommand(intent, flags, startId)
//
//
//    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}