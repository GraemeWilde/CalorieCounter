package com.wilde.caloriecounter2.other

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.wilde.caloriecounter2.data.food.entities.Product


//@JsonClass(generateAdapter = true)
//class MoshiParcel() : Parcelable {
//    //val clazz: Class<T>
//
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(out: Parcel?, flags: Int) {
//        val moshi = Moshi.Builder().build()
//        val jsonAdapter: JsonAdapter<this::class.java> = moshi.adapter(this::class.java)
//
//        val temp = this
//
//        val food = jsonAdapter.toJson()
//    }
//
//    companion object CREATOR : Parcelable.Creator<MoshiParcel> {
//        override fun createFromParcel(parcel: Parcel): MoshiParcel {
//            return MoshiParcel(parcel)
//        }
//
//        override fun newArray(size: Int): Array<MoshiParcel?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
//
//fun test() {
//    val a = MoshiParcel()
//
//    val moshi = Moshi.Builder().build()
//    val jsonAdapter = moshi.adapter(MoshiParcel::class.java)
//    val food = jsonAdapter.toJson()
//}