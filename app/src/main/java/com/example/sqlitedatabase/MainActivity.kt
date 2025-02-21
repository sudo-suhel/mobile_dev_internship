package com.example.sqlitedatabase

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlitedatabase.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var db: SQLiteDatabase? = null
    private var myFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddRecord.setOnClickListener {
            addData()
        }

        binding.btnEditUpdate.setOnClickListener {
            editData()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createDatabase()
        createTables()
    }

    private fun createDatabase() {
        try {
            val folder =
                File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/Database")
            if (!folder.exists()) {
                folder.mkdir()
            }
            myFile = File(folder, "MyDb")
            ConnectionClass.myFile = myFile
            db = SQLiteDatabase.openOrCreateDatabase(myFile!!.absolutePath, null, null)
        } catch (ex: Exception) {
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun createTables() {
        var createTable =
            "CREATE TABLE IF NOT EXISTS student (studentId INTEGER PRIMARY KEY AUTOINCREMENT, studentName TEXT, address TEXT, class TEXT, age INTEGER, studentPhoto BLOB)"
        db!!.execSQL(createTable)

        createTable =
            "CREATE TABLE IF NOT EXISTS class (classId INTEGER PRIMARY KEY AUTOINCREMENT, classname TEXT)"
        db!!.execSQL(createTable)
    }

    private fun addData() {
        val intent = Intent(this, Student::class.java)
        intent.putExtra("msg", "add")
        startActivity(intent)
    }

    private fun editData() {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("msg", "edit") // Corrected "add" to "edit"
        startActivity(intent)
    }
}
