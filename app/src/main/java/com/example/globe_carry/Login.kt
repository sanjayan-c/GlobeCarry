package com.example.globe_carry

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var btnLogin: Button
    private lateinit var txtSignup:TextView
    private lateinit var cus_login_no_username_password:TextView
    private lateinit var pwdVisible:ImageView
    private lateinit var userAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userAuth=FirebaseAuth.getInstance()
        edtEmail=findViewById(R.id.edt_email)
        pwdVisible=findViewById(R.id.imgPasswordVisibility)
        edtPassword=findViewById(R.id.edt_password)
        btnLogin=findViewById(R.id.btnLogin)
        txtSignup=findViewById(R.id.text_register)
        cus_login_no_username_password=findViewById(R.id.cus_login_no_username_password)

        val registerString = "Register"
        val mSpannableString = SpannableString(registerString)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        txtSignup.text = mSpannableString

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

        txtSignup.setOnClickListener{
            val intent=Intent(this,SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            if(email=="" || password=="") {
                cus_login_no_username_password.visibility = View.VISIBLE
                cus_login_no_username_password.text = "Enter Email and Password"
            }else{
                cus_login_no_username_password.visibility = View.GONE
                login(email,password)
            }
        }

    }

    private fun login(email:String,password:String){
        userAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //logging in
                    val intent=Intent(this@Login,CommonHome::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // Check the error message
                    val errorMessage = task.exception?.message
                    if (errorMessage != null) {
                        if (errorMessage.contains("password")) {
                            // Incorrect password
                            Toast.makeText(
                                this@Login,
                                "Incorrect password",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (errorMessage.contains("no user record")) {
                            // Email not found
                            Toast.makeText(
                                this@Login,
                                "No account found for this email",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Other error, show a generic message
                            Toast.makeText(
                                this@Login,
                                "Login failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Unexpected error, show a generic message
                        Toast.makeText(
                            this@Login,
                            "Login failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
//private fun login(email: String, password: String) {
//    userAuth.signInWithEmailAndPassword(email, password)
//        .addOnCompleteListener(this) { task ->
//            if (task.isSuccessful) {
//                val user = userAuth.currentUser
//                if (user != null && user.isEmailVerified) {
//                    // User is authenticated and their email is verified
//                    Log.d(ContentValues.TAG, "signInWithEmail:success")
//
//                    // Proceed to the next screen or perform any other actions
//                    val intent = Intent(this@Login, UserActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {
//                    // User is authenticated but their email is not verified
//                    Toast.makeText(
//                        this@Login,
//                        "Please verify your email address first.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                // If sign-in fails, display a message to the user.
//                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
//                Toast.makeText(this@Login, "Authentication failed.", Toast.LENGTH_SHORT).show()
//            }
//        }
//}
}
//android:theme="@style/Theme.Globe_Carry"