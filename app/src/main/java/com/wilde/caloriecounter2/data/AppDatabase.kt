package com.wilde.caloriecounter2.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wilde.caloriecounter2.data.food.FoodDao
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.*
import com.wilde.caloriecounter2.data.meals.entities.*
import com.wilde.caloriecounter2.data.weight.WeightDao
import com.wilde.caloriecounter2.data.weight.Weight
import kotlinx.coroutines.*
import java.time.LocalDateTime

private const val LOGGER_TAG = "AppDatabase"
private const val DATABASE_NAME = "calorie_counter_"

@Database(
    entities = [Weight::class, Product::class, MealParent::class, MealComponent::class],
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
                                        Product(
                                            brands = "Test Egg Brand",
                                            productName = "Egg Whites",
                                        ),
                                        Product(
                                            brands = "Test English Muffin Brand",
                                            productName = "English Muffins",
                                        )
                                    )
                                    inst.mealDAO().insertMeals(
                                        Meal(
                                            MealParent(
                                                name = "Egg Whites Muffin"
                                            ),
                                            listOf(
                                                MealComponent(
                                                    foodId = foodsIds[0].toInt(),
                                                    quantity = Quantity(
                                                        1f,
                                                        QuantityType.Ratio
                                                    )
                                                )
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