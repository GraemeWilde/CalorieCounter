package com.wilde.caloriecounter2.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wilde.caloriecounter2.data.food.FoodDao
import com.wilde.caloriecounter2.data.food.entities.Nutriments
import com.wilde.caloriecounter2.data.food.entities.PerServing
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.MealDao
import com.wilde.caloriecounter2.data.meals.entities.*
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import com.wilde.caloriecounter2.data.other.quantity.QuantityType
import com.wilde.caloriecounter2.data.weight.Weight
import com.wilde.caloriecounter2.data.weight.WeightDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val LOGGER_TAG = "AppDatabase"
private const val DATABASE_NAME = "calorie_counter_"

@Database(
    entities = [Weight::class, Product::class, Meal::class, MealComponent::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(com.wilde.caloriecounter2.data.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightDAO(): WeightDao
    abstract fun foodDAO(): FoodDao
    abstract fun mealDAO(): MealDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                //.fallbackToDestructiveMigration()
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d(LOGGER_TAG, "Creating Database")
                            super.onCreate(db)

                            instance?.let { inst ->
                                CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                                    inst.weightDAO().insertAll(
                                        Weight(LocalDateTime.now().minusDays(3), 100f),
                                        Weight(
                                            LocalDateTime.now().minusDays(2).minusMinutes(200),
                                            120f
                                        )
                                    )
                                    val foodsIds = inst.foodDAO().insertFoods(
//                                        Product(
//                                            brands = "Test Egg Brand",
//                                            productName = "Egg Whites",
//                                        ),
//                                        Product(
//                                            brands = "Test English Muffin Brand",
//                                            productName = "English Muffins",
//                                        ),
//                                        Product(
//                                            brands = "Test food brand with a lot of text to debug how wrapping would work if there was a really long brand name",
//                                            productName = "Long food brand product",
//                                        ),
                                        Product(productName="Coca-Cola", brands="Coca-Cola", offId="06742708", productCode="06742708", packageSize="2 L", nutriments= Nutriments(perServing= PerServing(servingSize=355.0f, calories=11.0f, fat=0.0f, saturatedFat=0.0f, transFat=null, cholesterol=null, sodium=0.025f, carbohydrates=39.0f, fibre=0.0f, sugars=39.0f, proteins=0.0f), per100g=PerServing(servingSize=100.0f, calories=39.4f, fat=0.0f, saturatedFat=0.0f, transFat=null, cholesterol=null, sodium=0.00704f, carbohydrates=11.0f, fibre=0.0f, sugars=11.0f, proteins=0.0f))),
                                        Product(productName="Plain English Muffins", brands="Great Value", offId="0681131761529", productCode="0681131761529", packageSize="6", nutriments=Nutriments(perServing=PerServing(servingSize=57.0f, calories=140.0f, fat=1.5f, saturatedFat=0.4f, transFat=null, cholesterol=null, sodium=0.051999897f, carbohydrates=27.0f, fibre=1.0f, sugars=2.0f, proteins=5.0f), per100g=PerServing(servingSize=100.0f, calories=246.0f, fat=2.63f, saturatedFat=0.702f, transFat=null, cholesterol=null, sodium=0.0912f, carbohydrates=47.4f, fibre=1.75f, sugars=3.51f, proteins=8.77f))),
                                        Product(id=6, productName="Great Value", brands="Extra large eggs", offId="0681131911962", productCode="0681131911962", packageSize="12", nutriments=Nutriments(perServing=PerServing(servingSize=58.0f, calories=80.0f, fat=2.0f, saturatedFat=2.0f, transFat=null, cholesterol=0.122f, sodium=0.075f, carbohydrates=1.0f, fibre=0.0f, sugars=0.0f, proteins=7.0f), per100g=PerServing(servingSize=100.0f, calories=137.931f, fat=3.4483f, saturatedFat=3.4483f, transFat=null, cholesterol=0.21f, sodium=0.1293103f, carbohydrates=1.7241f, fibre=0.0f, sugars=0.0f, proteins=12.069f)))
                                    )
                                    inst.mealDAO().insertMeals(
                                        MealAndComponents(
                                            Meal(
                                                name = "Egg Whites Muffin"
                                            ),
                                            listOf(
                                                //MealComponent(
                                                MealComponent(
                                                    foodId = foodsIds[2].toInt(),
                                                    quantity = Quantity(
                                                        1f,
                                                        QuantityType.Ratio
                                                    )
                                                ),
                                                MealComponent(
                                                    foodId = foodsIds[1].toInt(),
                                                    quantity = Quantity(
                                                        1f,
                                                        QuantityType.Ratio
                                                    )
                                                )
                                                /*Product()
                                            )*/
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                ).build()
        }
    }
}