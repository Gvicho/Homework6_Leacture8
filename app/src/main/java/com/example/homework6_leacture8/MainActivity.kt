package com.example.homework6_leacture8

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.homework6_leacture8.databinding.ActivityMainBinding
import com.example.homework6_leacture8.databinding.ActivityUpdateBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var userMp = mutableMapOf<String, Person>()
    var userSt = mutableSetOf<String>()
    var usersRemoved: Int = 0

    var helperMailToPass = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        setActiveRemovedNumbers()

        binding.btnAddUser.setOnClickListener {
            addUserFun()
        }
        binding.btnRemoveUser.setOnClickListener {
            removeUserFun()
        }
        binding.btnUpdateUser.setOnClickListener {
            updateUserFun()
        }
    }

    private fun addUserFun() {
        val fName = binding.edtFName.text.toString()
        val lName = binding.edtLName.text.toString()
        val ageS = binding.edtAge.text.toString()
        val mail = binding.edtEmail.text.toString()

        if (handleInput(fName, lName, ageS, mail)) {
            Toast.makeText(this, "Fill all fields!!", Toast.LENGTH_SHORT).show()
            error()
        } else {
            if (!isEmailValid(mail)) {
                Toast.makeText(this, "Mail incorrect!", Toast.LENGTH_SHORT).show()
                return
            }

            try {
                val age = ageS.toInt()
                val person = Person(mail, fName, lName, age)
                checkIfExists(person)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Age must be integer!", Toast.LENGTH_SHORT).show()
                error()
            }
        }
    }

    private fun removeUserFun() {
        val mail = binding.edtEmail.text.toString()

        if (handleInput(mail)) {
            Toast.makeText(this, "Mail field not filled", Toast.LENGTH_SHORT).show()
            error()
        } else {
            if (isMailInConteiner(mail)) {
                removeUser(mail)
            } else {
                Toast.makeText(this, "No User with given mail", Toast.LENGTH_SHORT).show()
                error()
            }
        }
    }

    private fun setActiveRemovedNumbers() {
        binding.activeUsers.text = userMp.size.toString()
        binding.removedUsers.text = usersRemoved.toString()
    }

    private fun handleInput(v1: String, v2: String, v3: String, v4: String): Boolean {
        return v1.isEmpty() || v2.isEmpty() || v3.isEmpty() || v4.isEmpty()
    }

    private fun handleInput(v1: String): Boolean {
        return v1.isEmpty()
    }

    private fun checkIfExists(person: Person) {
        if (!userSt.contains(person.mail)) addUser(person)
        else {
            error()
            Toast.makeText(this, "Mail is already used", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUser(person: Person) {
        userMp[person.mail] = person
        userSt.add(person.mail)
        success()
    }

    private fun success() {
        binding.opIdentification.visibility = View.VISIBLE
        binding.opIdentification.text = "Successfull"
        binding.opIdentification.setTextColor(Color.GREEN)

        binding.activeUsers.text = (userSt.size).toString()
        binding.removedUsers.text = (usersRemoved).toString()
    }

    private fun error() {
        binding.opIdentification.visibility = View.VISIBLE
        binding.opIdentification.text = "Error"
        binding.opIdentification.setTextColor(Color.RED)
    }

    private fun isEmailValid(email: String): Boolean {
        val regexPattern = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        return regexPattern.matches(email)
    }

    private fun removeUser(mail: String) {
        userSt.remove(mail)
        userMp.remove(mail)
        usersRemoved++
        success()
    }

    private fun isMailInConteiner(mail: String): Boolean {
        return userSt.contains(mail)
    }

    private fun updateUserFun() {
        val mail = binding.edtEmail.text.toString()

        if (handleInput(mail)) {
            Toast.makeText(this, "Mail field not filled", Toast.LENGTH_SHORT).show()
            updateUser(mail)
        } else {
            if (isMailInConteiner(mail)) {
                updateUser(mail)
            } else {
                Toast.makeText(this, "No User with given mail", Toast.LENGTH_SHORT).show()
                error()
            }
        }
    }

    private fun updateUser(mail: String) {
        val intent = Intent(this, UpdateActivity::class.java)
        helperMailToPass = mail
        activityResultLauncher.launch(intent)

    }

    private val activityResultLauncher: ActivityResultLauncher<Intent> =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val fName = data?.getStringExtra("fName")
                val lName = data?.getStringExtra("lName")
                val age = data?.getIntExtra("age",0)

                UpdategivenInfo(fName?:"",lName?:"",age?:0)
            }
    }

    private fun  UpdategivenInfo(fName:String,lName:String,age:Int){
        userMp[helperMailToPass] = Person(helperMailToPass,fName,lName,age)
        Toast.makeText(this, "User Updated succesfuly", Toast.LENGTH_SHORT).show()
        success()
    }

}