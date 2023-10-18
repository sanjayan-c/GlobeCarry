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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Date
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SignUp : AppCompatActivity() {

    private lateinit var edtFirstName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var pwdVisible: ImageView
    private lateinit var pwdConfirmVisible: ImageView
    private lateinit var btnSignup: Button
    private lateinit var cus_login_no_username_password: TextView
    private lateinit var cus_matching_password: TextView
    private lateinit var txtLogin: TextView
    private lateinit var userAuth: FirebaseAuth
    private lateinit var userDbRef: DatabaseReference
    private lateinit var cusWalletProgressBarLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userAuth=FirebaseAuth.getInstance()
        pwdVisible=findViewById(R.id.imgPasswordVisibility)
        pwdConfirmVisible=findViewById(R.id.imgConfirmPasswordVisibility)
        edtFirstName=findViewById(R.id.edt_first_name)
        edtLastName=findViewById(R.id.edt_last_name)
        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        edtConfirmPassword=findViewById(R.id.edt_confirm_password)
        btnSignup=findViewById(R.id.btnSignup)
        txtLogin=findViewById(R.id.text_login)
        cus_login_no_username_password=findViewById(R.id.cus_login_no_username_password)
        cus_matching_password=findViewById(R.id.cus_matching_password)
        cusWalletProgressBarLayout=findViewById(R.id.cusWalletProgressBarLayout)

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

        pwdConfirmVisible.setOnClickListener {
            if (edtConfirmPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                edtConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pwdConfirmVisible.setImageResource(R.drawable.visibility_off)
            } else {
                edtConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                pwdConfirmVisible.setImageResource(R.drawable.visibility)
            }
            // Move the cursor to the end of the text
            edtConfirmPassword.setSelection(edtConfirmPassword.text.length)
        }

        txtLogin.setOnClickListener{
            val intent=Intent(this,Login::class.java)
            startActivity(intent)
        }

        btnSignup.setOnClickListener{
            cus_login_no_username_password.visibility = View.GONE
            cus_matching_password.visibility = View.GONE
            val firstName = edtFirstName.text.toString()
            val lastName = edtLastName.text.toString()
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            val confirmPassword=edtConfirmPassword.text.toString()

            if(firstName==""||lastName==""||email==""||password==""|| password!=confirmPassword){
                if(firstName==""||lastName==""||email==""||password==""){
                    cus_login_no_username_password.text = "Fill the above form"
                    cus_login_no_username_password.visibility = View.VISIBLE
                }
                if(password!=confirmPassword){
                    cus_matching_password.text = "Passwords doesn't match"
                    cus_matching_password.visibility = View.VISIBLE
                }
            }else{
                cusWalletProgressBarLayout.visibility=View.VISIBLE
                cusWalletProgressBarLayout.isClickable = true
                cusWalletProgressBarLayout.isFocusable = true
                signUp(firstName,lastName,email,password)
            }

        }
    }

//    private fun signUp(name:String,email:String,password:String){
//        userAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    addUserToDatabase(name,email,userAuth.currentUser?.uid!!,"user" )
//                    //navigate to home
//                    val intent=Intent(this@SignUp,UserActivity::class.java)
//                    finish()
//                    startActivity(intent)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(this@SignUp,"Some error has occured",Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
private fun signUp(firstName: String, lastName: String, email: String, password: String) {
    userAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "createUserWithEmail:success")

                // Send email verification
                val user = userAuth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            val name = "$firstName $lastName"
                            addUserToDatabase(name,email,userAuth.currentUser?.uid!!,"user" )
                            addUserToSqlDatabase(firstName,lastName, userAuth.currentUser?.uid!!, email)
                            Log.d(ContentValues.TAG, "Email verification sent.")
                            Toast.makeText(
                                this@SignUp,
                                "Verification email sent. Please check your email.",
                                Toast.LENGTH_SHORT
                            ).show()
                            // You can navigate to the login screen or perform any other actions here
                        } else {
                            Log.e(ContentValues.TAG, "Failed to send verification email.", verificationTask.exception)
                            Toast.makeText(
                                this@SignUp,
                                "Failed to send verification email. Please try again later.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                // You may want to navigate to the login screen or another activity here
            } else {
                // If sign-up fails, check the error code
                val errorCode = (task.exception as FirebaseAuthException).errorCode
                if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                    // Email already exists
                    Toast.makeText(
                        this@SignUp,
                        "Email already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Handle other sign-up errors
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@SignUp, "Some error has occurred", Toast.LENGTH_SHORT).show()
                }
                }
        }
}

    private fun addUserToDatabase(name:String,email:String,uid:String,type:String){
        userDbRef= FirebaseDatabase.getInstance().getReference()
        userDbRef.child("user").child(uid).setValue(User(name, email, uid,type))
    }
    private fun addUserToSqlDatabase(firstName: String, lastName: String, uid: String, email: String) {
        val cusConSQL = ConnectionSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                try {
                    val (currentDate, currentTime) = getCurrentDateTime()
                    val query = "INSERT INTO user (userId, firstName, lastName, gmail, signUpDate, signUpTime) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"

                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, uid)
                    preparedStatement.setString(2, firstName)
                    preparedStatement.setString(3, lastName)
                    preparedStatement.setString(4, email)
                    preparedStatement.setString(5, currentDate)
                    preparedStatement.setString(6, currentTime)

                    // Execute the prepared statement
                    preparedStatement.executeUpdate()

                    // Close the prepared statement
                    preparedStatement.close()
                    //navigate to home
                    runOnUiThread {
                        cusWalletProgressBarLayout.visibility = View.GONE
                        cusWalletProgressBarLayout.isClickable = false
                        cusWalletProgressBarLayout.isFocusable = false
                    }
                    val intent= Intent(this@SignUp,Login::class.java)
                    finish()
                    startActivity(intent)
                } catch (e: SQLException) {
                    Log.e("addUserToDatabase", "SQL Exception: ${e.message}")
                    e.printStackTrace()
                }finally {
                    // Close the connection in the finally block to ensure it's always closed
                    connection.close()
                }
            } else {
                Log.e("addUserToDatabase", "Database connection is null")
            }
        }
    }
    fun getCurrentDateTime(): Pair<String, String> {
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val currentDate = currentDateTime.format(dateFormatter)
        val currentTime = currentDateTime.format(timeFormatter)
        return Pair(currentDate, currentTime)
    }
}