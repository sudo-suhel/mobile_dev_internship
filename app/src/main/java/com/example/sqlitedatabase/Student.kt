package com.example.sqlitedatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlitedatabase.databinding.ActivityStudentBinding
import java.io.ByteArrayOutputStream
import java.io.File

class Student : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding
    private var imageByteArray: ByteArray? = null
    private var db: SQLiteDatabase? = null
    private var myFile: File? = null
    private lateinit var msg: String
    private var sId: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        myFile = ConnectionClass.myFile
        db = SQLiteDatabase.openOrCreateDatabase(myFile!!.absolutePath, null, null)

        val i = intent
        msg = i.getStringExtra("msg").toString()
        sId = i.getIntExtra("sid", 0)

        when (msg) {
            "add" -> binding.btnSave.text = "Insert Data"
            "edit" -> {
                binding.btnSave.text = "Update Data"
                binding.btnDel.visibility = View.VISIBLE
            }
        }

        binding.btnSave.setOnClickListener {
            if (msg == "add") insertData() else updateData()
        }

        binding.btnDel.setOnClickListener { delData() }
    }

    private fun insertData() {
        val sName = binding.editName.text.toString()
        val sAddress = binding.editAddress.text.toString()
        val sClass = binding.editClass.text.toString()
        val sAge = binding.editAge.text.toString()

        if (sName.isEmpty()) {
            Toast.makeText(this, "Input student Name", Toast.LENGTH_SHORT).show()
            binding.editName.requestFocus()
            return
        }
        if (sClass.isEmpty()) {
            Toast.makeText(this, "Input class Name", Toast.LENGTH_SHORT).show()
            binding.editClass.requestFocus()
            return
        }

        try {
            val values = ContentValues().apply {
                put("studentName", sName)
                put("address", sAddress)
                put("class", sClass)
                put("age", sAge)
                put("studentPhoto", imageByteArray)
            }
            db!!.insert("student", null, values)
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
            clearFields()
        } catch (ex: Exception) {
            Toast.makeText(this, "Insert Error: " + ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateData() {
        val sName = binding.editName.text.toString()
        val sAddress = binding.editAddress.text.toString()
        val sClass = binding.editClass.text.toString()
        val sAge = binding.editAge.text.toString()

        if (sName.isEmpty()) {
            Toast.makeText(this, "Input Student Name", Toast.LENGTH_LONG).show()
            binding.editName.requestFocus()
            return
        }
        if (sClass.isEmpty()) {
            Toast.makeText(this, "Input class Name", Toast.LENGTH_LONG).show()
            binding.editClass.requestFocus()
            return
        }

        try {
            val values = ContentValues().apply {
                put("studentName", sName)
                put("address", sAddress)
                put("class", sClass)
                put("age", sAge)
                put("studentPhoto", imageByteArray)
            }
            db!!.update("student", values, "studentId=?", arrayOf(sId.toString()))
            Toast.makeText(this, "Data Updated", Toast.LENGTH_LONG).show()
            clearFields()
        } catch (ex: Exception) {
            Toast.makeText(this, "Update Error: " + ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun delData() {
        try {
            db!!.delete("student", "studentId=?", arrayOf(sId.toString()))
            Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show()
            finish()
        } catch (ex: Exception) {
            Toast.makeText(this, "Delete Error: " + ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        binding.editName.text?.clear()
        binding.editAddress.text?.clear()
        binding.editClass.text?.clear()
        binding.editAge.text?.clear()
        binding.imageView.setImageBitmap(null)
        imageByteArray = null
    }
}
