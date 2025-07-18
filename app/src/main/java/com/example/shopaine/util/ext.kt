package com.example.shopaine.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.logging.SimpleFormatter

val coroutineExceptionHandler = CoroutineExceptionHandler { _ , throwable ->
    Log.v("error" , "Error ->" + throwable.message)
}

fun stylePrice(oldPrice: String) : String {

    if (oldPrice.length > 3){

        val reversed = oldPrice.reversed()
        var newPrice = ""

        for ( i in reversed.indices){
            newPrice += reversed[i]
            if ((i+1) % 3 == 0 ){
                newPrice += ","
            }

        }

        val readyTOGO = newPrice.reversed()

        if (readyTOGO.first() == ','){
            return readyTOGO.substring(1) + "Tomans"
        }

        return readyTOGO +"Tomans"


    }

    return oldPrice + "Tomans"
}


fun styleTime(timeInMillies : Long) : String {

    val formatter = SimpleDateFormat("yyyy/MM/dd  hh:mm")

    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillies

    return formatter.format(calender.time)


}