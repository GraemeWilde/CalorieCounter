package com.wilde.caloriecounter2.other

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import com.squareup.moshi.Moshi
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods

inline fun <reified T: Parcelable> NavArg(isNullableAllowed: Boolean): NavArg<T> {
    return NavArg(isNullableAllowed, T::class.java)
}

class NavArg<T : Parcelable>(isNullableAllowed: Boolean = false, private val clazz: Class<T>) : NavType<T>(isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): T {
        val moshi = Moshi.Builder().build()

        val meal = moshi.adapter(clazz).fromJson(value)

        return meal ?: throw IllegalArgumentException()
    }

    fun serialize(obj: T): String {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(clazz)
        val food = jsonAdapter.toJson(obj)

        return food
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }
}

inline fun <reified T> T.serialize(): String {

    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(T::class.java)

    return jsonAdapter.toJson(this)
}

inline fun <reified T> String.parse(): T {

    val moshi = Moshi.Builder().build()

    return moshi.adapter(T::class.java).fromJson(this)!!
}

class MealAndComponentsAndFoodsNavType : NavType<MealAndComponentsAndFoods>(true) {
    override fun get(bundle: Bundle, key: String): MealAndComponentsAndFoods? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): MealAndComponentsAndFoods {

        val moshi = Moshi.Builder().build()

        val meal = moshi.adapter(MealAndComponentsAndFoods::class.java).fromJson(value)

        return meal ?: throw IllegalArgumentException()
    }

    override fun put(bundle: Bundle, key: String, value: MealAndComponentsAndFoods) {
        bundle.putParcelable(key, value)
    }
}

fun NavController.navigate(route: String, vararg args: Parcelable) {
    navigate(route + "/" + {args.joinToString(separator = "/")} )
}

fun <T: Parcelable> NavBackStackEntry.getArg(key: String): T {
    return requireNotNull(arguments) { "Argument bundle is null" }.run {
        requireNotNull(getParcelable(key)) { "Argument for $key is null" }
    }
}

