package com.example.globe_carry

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var layout_password:ConstraintLayout
    private lateinit var worldImageView:ImageView
    private lateinit var cusWalletProgressBarLayout:FrameLayout
    private lateinit var btnLogin: Button
    private lateinit var txtSignup:TextView
    private lateinit var cus_login_no_username_password:TextView
    private lateinit var pwdVisible:ImageView
    private lateinit var userAuth:FirebaseAuth
    private lateinit var userDbRef: DatabaseReference

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
        layout_password=findViewById(R.id.layout_password)

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
                val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_message_box)
                edtEmail.background = ContextCompat.getDrawable(this, R.drawable.edt_background)
                layout_password.background = ContextCompat.getDrawable(this, R.drawable.edt_background)
                edtEmail.startAnimation(blinkAnimation)
                layout_password.startAnimation(blinkAnimation)
                // Create a Handler to reset the messageBox after 2 seconds
                val handler = Handler()
                handler.postDelayed({
                    edtEmail.background = ContextCompat.getDrawable(this, R.drawable.edt_background)
                    layout_password.background = ContextCompat.getDrawable(this, R.drawable.edt_background)
                }, 2000)
            }else{
                cus_login_no_username_password.visibility = View.GONE
                cusWalletProgressBarLayout = findViewById(R.id.cusWalletProgressBarLayout)
                worldImageView = findViewById(R.id.worldImageView)
                cusWalletProgressBarLayout.visibility = View.VISIBLE
                cusWalletProgressBarLayout.isClickable = true
                cusWalletProgressBarLayout.isFocusable = true
                val horizontalRotationAnimation = createHorizontalRotationAnimation()
                worldImageView.startAnimation(horizontalRotationAnimation)
                login(email,password)
            }
        }

    }

    private fun login(email:String,password:String){
        userAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Perform the login after a delay
                    Handler().postDelayed({
                        // Check the user's type by fetching the data from the database
                        val currentUser = userAuth.currentUser
                        if (currentUser != null) {
                            val uid = currentUser.uid
                            userDbRef= FirebaseDatabase.getInstance().reference
                            userDbRef.child("user").child(uid).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val userType = dataSnapshot.child("type").getValue(String::class.java)
                                    if (userType == "user") {
                                        // This is a user
                                        // Proceed with the login
                                        val intent = Intent(this@Login, CommonHome::class.java)
                                        finish()
                                        startActivity(intent)
                                    } else if (userType == "staff") {
                                        // This is a staff member
                                        // Proceed with the login
                                        val intent = Intent(this@Login, StaffCommonHome::class.java)
                                        finish()
                                        startActivity(intent)
                                    } else {
                                        // Handle other user types, if needed
                                        val intent = Intent(this@Login, CommonHome::class.java)
                                        finish()
                                        startActivity(intent)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                }
                            })
                        }
                    }, 2000) // Delay for 2 seconds
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
                    cusWalletProgressBarLayout.visibility = View.GONE
                    cusWalletProgressBarLayout.isClickable = false
                    cusWalletProgressBarLayout.isFocusable = false
                }
            }
    }


//    private fun login(email:String,password:String){
//        userAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = userAuth.currentUser
//                    if (user != null && user.isEmailVerified) {
//                        // User is authenticated and their email is verified
//                        Log.d(ContentValues.TAG, "signInWithEmail:success")
//
//                        // Proceed to the next screen or perform any other actions
//                        val intent = Intent(this@Login, CommonHome::class.java)
//                        startActivity(intent)
//                    } else {
//                        // User is authenticated but their email is not verified
//                        Toast.makeText(
//                            this@Login,
//                            "Please verify your email address first.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    // Check the error message
//                    val errorMessage = task.exception?.message
//                    if (errorMessage != null) {
//                        if (errorMessage.contains("password")) {
//                            // Incorrect password
//                            Toast.makeText(
//                                this@Login,
//                                "Incorrect password",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (errorMessage.contains("no user record")) {
//                            // Email not found
//                            Toast.makeText(
//                                this@Login,
//                                "No account found for this email",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            // Other error, show a generic message
//                            Toast.makeText(
//                                this@Login,
//                                "Login failed. Please try again.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    } else {
//                        // Unexpected error, show a generic message
//                        Toast.makeText(
//                            this@Login,
//                            "Login failed. Please try again.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//    }
private fun createHorizontalRotationAnimation(): RotateAnimation {
    val rotateAnimation = RotateAnimation(
        0.0f,
        360.0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    rotateAnimation.duration = 1000 // Animation duration in milliseconds
    return rotateAnimation
}

}
//android:theme="@style/Theme.Globe_Carry"