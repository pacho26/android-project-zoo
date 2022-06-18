package hr.tvz.android.slovicprojekt.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.tvz.android.slovicprojekt.model.Animal
import java.util.ArrayList

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_VERSION = 1

        const val DATABASE_NAME = "animalsList"

        const val TABLE_ANIMALS = "animals"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_FAMILY = "family"
        const val KEY_LIFETIME = "lifetime"
        const val KEY_WEIGHT = "weight"

        const val CREATE_TABLE_TODO = ("CREATE TABLE "
                + TABLE_ANIMALS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
                + " TEXT," + KEY_FAMILY + " TEXT," + KEY_LIFETIME + " TEXT," + KEY_WEIGHT + " TEXT" + ")")
    }

    // TODO: Čemu ovo služi Patrik?
    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL(CREATE_TABLE_TODO)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS)

        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAnimals(): MutableList<Animal> {
        val animals: MutableList<Animal> = ArrayList<Animal>()
        val selectQuery = "SELECT  * FROM " + TABLE_ANIMALS
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                val td: Animal = Animal(
                    c.getInt(c.getColumnIndex(KEY_ID)),
                    c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getString(c.getColumnIndex(KEY_FAMILY)),
                    c.getString(c.getColumnIndex(KEY_LIFETIME)),
                    c.getString(c.getColumnIndex(KEY_WEIGHT))
                )

                animals.add(td)
            } while (c.moveToNext())
        }
        return animals
    }

    @SuppressLint("Range")
    fun getAnimalById(id: String?): Animal? {
        val db = this.readableDatabase
        val selectQuery = ("SELECT  * FROM " + TABLE_ANIMALS + " WHERE "
                + KEY_ID + " = " + id)
        val c = db.rawQuery(selectQuery, null)
        c?.moveToFirst()
        val td: Animal = Animal(
            c.getInt(c.getColumnIndex(KEY_ID)),
            c.getString(c.getColumnIndex(KEY_NAME)),
            c.getString(c.getColumnIndex(KEY_FAMILY)),
            c.getString(c.getColumnIndex(KEY_LIFETIME)),
            c.getString(c.getColumnIndex(KEY_WEIGHT))
        )
        return td
    }

    fun createAnimal(animal: Animal): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_ID, animal.id)
        values.put(KEY_NAME, animal.name)
        values.put(KEY_FAMILY, animal.family)
        values.put(KEY_LIFETIME, animal.lifetime)
        values.put(KEY_WEIGHT, animal.weight)

        // insert row
        return db.insert(TABLE_ANIMALS, null, values)
    }
}