package com.example.flutter_bacgroundserves_1

import android.Manifest
import android.app.AlertDialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlin.math.log

class MainActivity: FlutterActivity() {


    var mLocationService: MyService2 = MyService2()
    lateinit var mServiceIntent: Intent






    private fun starServiceFunc(){
        mLocationService = MyService2()
        mServiceIntent = Intent(this, mLocationService.javaClass)
        if (!Util.isMyServiceRunning(mLocationService.javaClass, this)) {
            startService(mServiceIntent)

            Toast.makeText(this,"service_start_successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this,"service_already_running", Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {

        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "backgroundservices").setMethodCallHandler {
                call, result ->

            if (call.method == "startService") {

                Log.e("ALAA"," value Token ${call.argument<String>("Token").toString()}")
            }




            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                        AlertDialog.Builder(this).apply {
                            setTitle("Background permission")
                            setMessage("background_location_permission_message")
                            setPositiveButton("Start service anyway",
                                DialogInterface.OnClickListener { dialog, id ->
                                    starServiceFunc()
                                })
                            setNegativeButton("Grant background Permission",
                                DialogInterface.OnClickListener { dialog, id ->
                                    requestBackgroundLocationPermission()
                                })
                        }.create().show()

                    }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                        starServiceFunc()
                    }
                }else{
                    starServiceFunc()
                }

            }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder(this)
                        .setTitle("ACCESS_FINE_LOCATION")
                        .setMessage("Location permission required")
                        .setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            requestFineLocationPermission()
                        }
                        .create()
                        .show()
                } else {
                    requestFineLocationPermission()
                }
            }

        }

        }



    override fun onDestroy() {
        /*  if (::mServiceIntent.isInitialized) {
              stopService(mServiceIntent)
          }*/
        super.onDestroy()
    }

    private fun requestBackgroundLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), MY_BACKGROUND_LOCATION_REQUEST)
    }

    private fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,), MY_FINE_LOCATION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this, requestCode.toString(), Toast.LENGTH_LONG).show()
        when (requestCode) {
            MY_FINE_LOCATION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        requestBackgroundLocationPermission()
                    }

                } else {
                    Toast.makeText(this, "ACCESS_FINE_LOCATION permission denied", Toast.LENGTH_LONG).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.packageName, null),),)
                    }
                }
                return
            }
            MY_BACKGROUND_LOCATION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Background location Permission Granted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Background location permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }



    companion object {
        private const val MY_FINE_LOCATION_REQUEST = 99
        private const val MY_BACKGROUND_LOCATION_REQUEST = 100
    }

    }
































































//    private val CHANNEL = "backgroundservices"
//
//    companion object {
//        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
//    }
//
//
//    private fun setUpLocationListener() {
//        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        // for getting the current location update after every 2 seconds with high accuracy
//        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
//            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            return
//        }
//        fusedLocationProviderClient.requestLocationUpdates(
//            locationRequest,
//            object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    super.onLocationResult(locationResult)
//                    for (location in locationResult.locations) {
////                     Log.e()location.latitude.toString()
////                        txtLng.text = location.longitude.toString()
//                    }
//                    // Few more things we can do here:
//                    // For example: Update the location of user on server
//                }
//            },
//            Looper.myLooper()!!
//        )
//
//    }
//
//
//    override fun onStart() {
//        super.onStart()
//        when {
//            PermissionUtils.isAccessFineLocationGranted(this) -> {
//                when {
//                    PermissionUtils.isLocationEnabled(this) -> {
//                        setUpLocationListener()
//                    }
//                    else -> {
//                        PermissionUtils.showGPSNotEnabledDialog(this)
//                    }
//                }
//            }
//            else -> {
//                ActivityCompat.requestPermissions(
//                    activity,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    LOCATION_PERMISSION_REQUEST_CODE
//                )
//            }
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    when {
//                        PermissionUtils.isLocationEnabled(this) -> {
//                            setUpLocationListener()
//                        }
//                        else -> {
//                            PermissionUtils.showGPSNotEnabledDialog(this)
//                        }
//                    }
//                } else {
//                    Toast.makeText(
//                        this,
//                        "LPalpALp",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//    }
//
//
//    private fun isLocationPermissionGranted(): Boolean {
//        return if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                2000
//            )
//            false
//        } else {
//            true
//        }
//    }
//
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
//        isLocationPermissionGranted()
//        super.configureFlutterEngine(flutterEngine)
//        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "backgroundservices").setMethodCallHandler {
//                call, result ->
//
//            var intent = Intent(this, MyService2::class.java)
//            //intent.putExtra("msg","Welcome to Service")
//            startForegroundService(intent)
//
//            Log.e("ALAA","ALAA ${call.method}")
//            Toast.makeText(this, "From Flutter", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//}
//
//
//
//object PermissionUtils {
//    /**
//     * Function to request permission from the user
//     */
////    fun requestAccessFineLocationPermission(activity: AppCompatActivity, requestId: Int) {
////        ActivityCompat.requestPermissions(
////            activity,
////            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
////            requestId
////        )
////    }
//
//    /**
//     * Function to check if the location permissions are granted or not
//     */
//    fun isAccessFineLocationGranted(context: Context): Boolean {
//        return ContextCompat
//            .checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    /**
//     * Function to check if location of the device is enabled or not
//     */
//    fun isLocationEnabled(context: Context): Boolean {
//        val locationManager: LocationManager =
//            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//    }
//
//    /**
//     * Function to show the "enable GPS" Dialog box
//     */
//    fun showGPSNotEnabledDialog(context: Context) {
//        AlertDialog.Builder(context)
//            .setTitle("Laplaplpa")
//            .setMessage("ALALALA")
//            .setCancelable(false)
//            .setPositiveButton("No") { _, _ ->
//                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//            }
//            .show()
//    }
//}
