package com.wilde.caloriecounter2.data.food

import androidx.room.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class FoodResponse(
    @Json(name = "count")
    val count: Int = 0,

    @Json(name = "skip")
    val skip: Int = 0,

    @Json(name = "page_size")
    val pageSize: Int = 0,

    @Json(name = "products")
    val products: List<Product>,

    @Json(name = "page")
    val page: Int = 0
)

@Entity(tableName = "products", indices = [Index(value = ["product_code"], unique = true)])
@JsonClass(generateAdapter = true)
data class ProductJson(
    @Json(name = "brands")
    val brands: String = "",

    @Json(name = "product_name")
    val productName: String = "",

    @Json(name = "id")
    val offId: String = "",

    @Json(name = "code")
    val productCode: String = "",

    @Json(name = "serving_quantity")
    val servingQuantity: Float?,

    @Json(name = "quantity")
    val quantity: String?,

    @Json(name = "nutriments")
    val nutriments: NutrimentsJson?
)

@JsonClass(generateAdapter = true)
data class NutrimentsJson(
    @Json(name = "energy-kcal_serving")
    val caloriesServing: Float?,

    @Json(name = "energy-kcal_100g")
    val calories100g: Float?,

    @Json(name = "fat_serving")
    val fatServing: Float?,

    @Json(name = "fat_100g")
    val fat100g: Float?,

    @Json(name = "saturated-fat_serving")
    val saturatedFatServing: Float?,

    @Json(name = "saturated-fat_100g")
    val saturatedFat100g: Float?,

    @Json(name = "trans-fat_serving")
    val transFatServing: Float?,

    @Json(name = "trans-fat_100g")
    val transFat100g: Float?,

    @Json(name = "cholesterol_serving")
    val cholesterolServing: Float?,

    @Json(name = "cholesterol_100g")
    val cholesterol100g: Float?,

    @Json(name = "sodium_serving")
    val sodiumServing: Float?,

    @Json(name = "sodium_100g")
    val sodium100g: Float?,

    @Json(name = "carbohydrates_serving")
    val carbohydratesServing: Float?,

    @Json(name = "carbohydrates_100g")
    val carbohydrates100g: Float?,

    @Json(name = "fiber_serving")
    val fibreServing: Float?,

    @Json(name = "fiber_100g")
    val fibre100g: Float?,

    @Json(name = "sugars_serving")
    val sugarsServing: Float?,

    @Json(name = "sugars_100g")
    val sugars100g: Float?,

    @Json(name = "proteins_serving")
    val proteinsServing: Float?,

    @Json(name = "proteins_100g")
    val proteins100g: Float?,
)

class ProductAdapter {
    @ToJson
    fun toJson(product: Product): ProductJson {
        val perServing = product.nutriments?.perServing
        val p100g = product.nutriments?.per100g
        val nutriment = NutrimentsJson(
            perServing?.calories,
            p100g?.calories,
            perServing?.fat,
            p100g?.fat,
            perServing?.saturatedFat,
            p100g?.saturatedFat,
            perServing?.transFat,
            p100g?.transFat,
            perServing?.cholesterol,
            p100g?.cholesterol,
            perServing?.sodium,
            p100g?.sodium,
            perServing?.carbohydrates,
            p100g?.carbohydrates,
            perServing?.fibre,
            p100g?.fibre,
            perServing?.sugars,
            p100g?.sugars,
            perServing?.proteins,
            p100g?.proteins
        )
        return ProductJson(
            product.brands,
            product.productName,
            product.offId,
            product.productCode,
            p100g?.servingSize,
            product.quantity,
            nutriment
        )
    }

    @FromJson
    fun fromJson(productJson: ProductJson): Product {
        val nutriments = productJson.nutriments?.let { nutrimentJson ->
            val perServing = if (productJson.servingQuantity == null) null
            else with(nutrimentJson) {
                PerServing(
                    productJson.servingQuantity,
                    caloriesServing,
                    fatServing,
                    saturatedFatServing,
                    transFatServing,
                    cholesterolServing,
                    sodiumServing,
                    carbohydratesServing,
                    fibreServing,
                    sugarsServing,
                    proteinsServing
                )
            }

            val per100g = if (nutrimentJson.calories100g == null) null
            else with(nutrimentJson) {
                PerServing(
                    100f,
                    calories100g,
                    fat100g,
                    saturatedFat100g,
                    transFat100g,
                    cholesterol100g,
                    sodium100g,
                    carbohydrates100g,
                    fibre100g,
                    sugars100g,
                    proteins100g
                )
            }


            Nutriments(perServing, per100g)
        }

        return Product(
            0,
            productJson.brands,
            productJson.productName,
            productJson.offId,
            productJson.productCode,
            productJson.quantity,
            nutriments
        )
    }
}

@Entity(tableName = "products", indices = [Index(value = ["product_code"], unique = true)])
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
    val quantity: String?,

    /*@ColumnInfo(name = "serving_quantity")
    val servingQuantity: Int?,*/

    @Embedded
    val nutriments: Nutriments?
) : Serializable

data class Nutriments(
    @Embedded(prefix = "per_serving_")
    val perServing: PerServing?,
    @Embedded(prefix = "per_100g_")
    val per100g: PerServing?,
) : Serializable

/*{
   val perServing: PerServing = PerServing(

       caloriesServing,
       fatServing,
       saturatedFatServing,
       transFatServing,
       cholesterolServing,
       sodiumServing,
       carbohydratesServing,
       fibreServing,
       sugarsServing,
       proteinsServing
   )
}*/

data class PerServing(
    @ColumnInfo(name = "serving_size")
    val servingSize: Float?,

    @ColumnInfo(name = "calories")
    val calories: Float?,

    @ColumnInfo(name = "fat")
    val fat: Float?,

    @ColumnInfo(name = "saturated_fat")
    val saturatedFat: Float?,

    @ColumnInfo(name = "trans_fat")
    val transFat: Float?,

    @ColumnInfo(name = "cholesterol")
    val cholesterol: Float?,

    @ColumnInfo(name = "sodium")
    val sodium: Float?,

    @ColumnInfo(name = "carbohydrates")
    val carbohydrates: Float?,

    @ColumnInfo(name = "fibre")
    val fibre: Float?,

    @ColumnInfo(name = "sugars")
    val sugars: Float?,

    @ColumnInfo(name = "proteins")
    val proteins: Float?,
) : Serializable

/*data class Nutriments(
    val calories: Float = 0f,

    val fat: Float = 0f,

    val saturatedFat: Float = 0f,

    val transFat: Float = 0f,

    val cholesterol: Float = 0f,

    @Json(name = "sodium_serving")
    val sodium: Float = 0f,

    @Json(name = "carbohydrates_serving")
    val carbohydrates: Float = 0f,

    @Json(name = "fiber_serving")
    val fibre: Float = 0f,

    @Json(name = "sugars_serving")
    val sugars: Float = 0f,

    @Json(name = "proteins_serving")
    val proteins: Float = 0f,
)*/