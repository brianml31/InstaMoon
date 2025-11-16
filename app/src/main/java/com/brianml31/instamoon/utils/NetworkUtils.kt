package com.brianml31.instamoon.utils;

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkUtils {
    companion object{
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager == null) {
                return false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.getActiveNetwork()
                if (network == null) return false
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            } else {
                val activeNetwork = connectivityManager.getActiveNetworkInfo()
                return activeNetwork != null && activeNetwork.isConnected()
            }
        }
    }
}
