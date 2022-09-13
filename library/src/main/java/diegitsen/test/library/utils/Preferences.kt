package diegitsen.test.library.utils

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Preferences (var context: Context){

    var pref: SharedPreferences? = null

    fun saveCoordinates(key: String, coords: MutableList<MutableList<Double>>?){
        val editor = pref!!.edit()
        val jsonString = Gson().toJson(coords)
        editor.putString(key, jsonString).apply()
    }

    fun getCoordinates(key: String): MutableList<MutableList<Double>>?{
        val jsonString = pref!!.getString(key, "")
        return Gson().fromJson(jsonString, object: TypeToken<MutableList<MutableList<Double>>>(){}.type)
    }

    fun saveLocation(key: String, location: Location){
        val editor = pref!!.edit()
        val jsonString = Gson().toJson(location)
        editor.putString(key, jsonString).apply()
    }

    fun getLocation(key: String): Location?{
        val jsonString = pref!!.getString(key, "")
        return Gson().fromJson(jsonString, object: TypeToken<Location>(){}.type)
    }

    fun saveInt(key: String, value: Int){
        val editor = pref!!.edit()
        editor.putInt(key, value).apply()
    }

    fun getInt(key: String): Int{
        return pref!!.getInt(key, 0)
    }

    fun saveString(key: String, value: String){
        val editor = pref!!.edit()
        editor.putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return pref!!.getString(key, "")
    }

    fun saveDouble(key: String, double: Double){
        val editor = pref!!.edit()
        val jsonString = Gson().toJson(double)
        editor.putString(key, jsonString).apply()
    }

    fun getDouble(key: String): Double?{
        val jsonString = pref!!.getString(key, "")
        return Gson().fromJson(jsonString, object: TypeToken<Double>(){}.type)
    }


    fun clearPreferences(){
        pref!!.edit().clear().apply()
    }

    init {
        pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }
}

