package com.wilde.caloriecounter2.data.food.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products", /*indices = [Index(value = ["product_code"], unique = true)]*/)
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "brands")
    val brands: String = "",

    @ColumnInfo(name = "product_name")
    val productName: String = "",

    @ColumnInfo(name = "off_id")
    val offId: String = "",

    @ColumnInfo(name = "product_code")
    val productCode: String = "",

    @ColumnInfo(name = "quantity")
    val quantity: String? = null,

    /*@ColumnInfo(name = "serving_quantity")
    val servingQuantity: Int?,*/

    @Embedded
    val nutriments: Nutriments? = null
) : Serializable