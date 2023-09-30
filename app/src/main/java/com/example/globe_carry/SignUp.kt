package com.example.globe_carry

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var txtLogin: TextView
    private lateinit var pwdVisible: ImageView
    private lateinit var userAuth: FirebaseAuth
    private lateinit var userDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userAuth=FirebaseAuth.getInstance()
        pwdVisible=findViewById(R.id.imgPasswordVisibility)
        edtName=findViewById(R.id.edt_name)
        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        btnSignup=findViewById(R.id.btnSignup)
        txtLogin=findViewById(R.id.text_login)

        val registerString = "Login"
        val mSpannableString = SpannableString(registerString)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        txtLogin.text = mSpannableString

        pwdVisible.setOnClickListener {
            if (edtPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pwdVisible.setImageResource(R.drawable.visibility_off)
            } else {
                edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                pwdVisible.setImageResource(R.drawable.visibility)
            }
            // Move the cursor to the end of the text
            edtPassword.setSelection(edtPassword.text.length)
        }

        txtLogin.setOnClickListener{
            val intent=Intent(this,Login::class.java)
            startActivity(intent)
        }

        btnSignup.setOnClickListener{
            val name = edtName.text.toString()
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            signUp(name,email,password)
        }
    }

    private fun signUp(name:String,email:String,password:String){
        userAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    addUserToDatabase(name,email,userAuth.currentUser?.uid!!,"user" )
                    //navigate to home
                    val intent=Intent(this@SignUp,UserActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@SignUp,"Some error has occured",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name:String,email:String,uid:String,type:String){
        userDbRef= FirebaseDatabase.getInstance().getReference()
        userDbRef.child("user").child(uid).setValue(User(name, email, uid,type))
    }

}