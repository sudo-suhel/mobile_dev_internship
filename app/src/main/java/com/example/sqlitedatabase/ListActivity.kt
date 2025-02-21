package com.example.sqlitedatabase

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.mutableLongListOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlitedatabase.databinding.ActivityListBinding
import java.io.File

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private var db: SQLiteDatabase? = null
    private var myFile: File? = null
    private var context: Context? = null
    private lateinit var itemArrayList: ArrayList<DataSet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize binding
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets if needed
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        myFile = ConnectionClass.myFile
        db = SQLiteDatabase.openOrCreateDatabase(myFile!!.absolutePath, null)

        context = this
        binding.itmList.layoutManager = LinearLayoutManager(this)
        binding.itmList.setHasFixedSize(true)
        itemArrayList = arrayListOf()
        showDataList()
    }

    private fun showDataList() {
        try {
            val cursor = db!!.rawQuery("SELECT * FROM student", null)
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val sId = cursor.getInt(0)
                    val name = cursor.getString(1)
                    val address = cursor.getString(2)
                    val sClass = cursor.getString(3)
                    val age = cursor.getInt(4)

                    val x = cursor.getBlob(5) ?: byteArrayOf(0x00)
                    val itemDS = DataSet(sId, name, address, sClass, age, x)
                    itemArrayList.add(itemDS)
                }
            }
            cursor.close()

            val adapter = ItmAdapter(itemArrayList)
            binding.itmList.adapter = adapter

            adapter.setOnItemClickListener(object : ItmAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val itmPos = itemArrayList[position]
                    val sId = itmPos.studentID
                    val intent = Intent(context, Student::class.java)
                    intent.putExtra("msg", "edit")
                    intent.putExtra("sid", sId)
                    startActivity(intent)
                    finish()
                }
            })
        } catch (ex: Exception) {
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}