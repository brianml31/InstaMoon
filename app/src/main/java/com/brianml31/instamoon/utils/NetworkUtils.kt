package com.brianml31.instamoon.utils;

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

class NetworkUtils {
    companion object{
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager == null) {
                return false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network: Network? = connectivityManager.activeNetwork
                if (network == null) {
                    return false
                }
                val capabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(network)
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            } else {
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnected
            }
        }

    }
}
