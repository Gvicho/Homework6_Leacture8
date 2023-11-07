package com.example.homework6_leacture8

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.homework6_leacture8.databinding.ActivityMainBinding
import com.example.homework6_leacture8.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding :ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnUpdateUsersInfo.setOnClickListener{
            getInfoSendBack()
        }
    }

    private fun getInfoSendBack(){
        val fName = binding.edtFName.text.toString()
        val lName = binding.edtLName.text.toString()
        val ageS = binding.edtAge.text.toString()

        if(!isFilled(fName,lName,ageS)){
            try {
                val age = ageS.toInt()
                sendBack(fName,lName,age)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Age must be integer!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Fill all fields!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isFilled(v1:String,v2:String,v3:String):Boolean{
        return v1.isEmpty()||v2.isEmpty()||v3.isEmpty()
    }

    private fun sendBack(fName:String,lName:String,age:Int){
        val resultIntent = Intent()
        resultIntent.putExtra("fName",fName)
        resultIntent.putExtra("lName",lName)
        resultIntent.putExtra("age",age)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}