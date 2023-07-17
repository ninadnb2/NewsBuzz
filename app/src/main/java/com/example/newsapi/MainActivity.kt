package com.example.newsapi

import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.newsapi.databinding.ActivityMainBinding
import com.example.newsapi.databinding.ActivityNewsBinding
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.internal.service.Common
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_news.view.*
import kotlinx.android.synthetic.main.activity_news_room.*
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.android.synthetic.main.item_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Arrays
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() ,NewsAdapter.NewsItemClicked{
    lateinit var binding:ActivityMainBinding
    private lateinit var mAdapter: NewsAdapter
    //private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    companion object{
        private val LOGIN_REQUEST_CODE=7171
    }

    private lateinit var providers:List<AuthUI.IdpConfig>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listener: FirebaseAuth.AuthStateListener
    private var verificationId:String?=null

    private lateinit var database:FirebaseDatabase
    private lateinit var driverInfoRef: DatabaseReference


    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  val swipe_refresh: SwipeRefreshLayout = findViewById(R.id.swipe_refresh)

        init()
        val toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        tabLayout =findViewById(R.id.tabLayout)
       tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
           override fun onTabReselected(tab: TabLayout.Tab?) {
               val category=getCategoryFromPosition(tab!!.position)
               getNews(category)
           }

           override fun onTabSelected(tab: TabLayout.Tab?) {
               val category=getCategoryFromPosition(tab!!.position)
               getNews(category)
           }

           override fun onTabUnselected(tab: TabLayout.Tab?) {

           }

       })
      //  onRefresh()

       /* swipe_refresh.setOnRefreshListener {
            NewService.newsInstance.getHeadLines("in",1,"")
        }*/

        home_save_news.setOnClickListener {
            startActivity(Intent(this@MainActivity,NewsRoomActivity::class.java))
        }

        val homeTab = tabLayout.getTabAt(0)
        homeTab?.select()


        profile_home.setOnClickListener {
            showLoginActivity()
            Log.e("ninad","clicked")
        }
    }

    private fun getCategoryFromPosition(position: Int): String {
        return when (position) {
            0 -> "general" //home
            1 -> "health"
            2 -> "business"
            3 -> "science"
            4 -> "technology"
            5 -> "sports"
            6 -> "entertainment"
            else -> ""
        }
    }


    private fun getNews(category:String) {
        val news: Call<News> = NewService.newsInstance.getHeadLines("", 1, category,"en")

        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val newsArray = ArrayList<News>()
                val news: News? = response.body()

                Log.d("news", "in response block")
                if (news != null) {
                    Log.d("ninad", news.toString())
                    mAdapter = NewsAdapter(this@MainActivity, this@MainActivity, news.articles)

                    newsList.adapter=mAdapter
                    newsList.layoutManager = LinearLayoutManager(this@MainActivity)
                    newsArray.add(news)
                }
                mAdapter.updateNews(news?.articles ?: emptyList())
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Error", "Error")
            }
        })
    }





    override fun onItemClicked(item: Article) {
        val intent=Intent(this@MainActivity,NewsActivity::class.java)
        intent.putExtra("newsimage",item.urlToImage)
        intent.putExtra("newsurl",item.url)
        intent.putExtra("newsimageurl",item.urlToImage)
        intent.putExtra("newstitle",item.title)
        intent.putExtra("newsauthor",item.author)
        intent.putExtra("newsdescription",item.description)
        intent.putExtra("newscontent",item.content)
        startActivity(intent)

    }

   /* override fun onRefresh() {
        NewService.newsInstance.getHeadLines("",1,"")

        fun onLoadSwipeRefresh() {
            swipe_refresh.post(
                Runnable() {

                    NewService.newsInstance.getHeadLines("", 1, "")

                }
            )
        }


    }*/

    private fun init(){

        providers= Arrays.asList(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        firebaseAuth=FirebaseAuth.getInstance()
        listener=FirebaseAuth.AuthStateListener { myFireBaseAuth ->
            val user=myFireBaseAuth.currentUser
            if(user?.phoneNumber!=null){
                        Toast.makeText(this, "Welcome " + user.displayName, Toast.LENGTH_SHORT)
                            .show()

            } else if(user?.isEmailVerified!=null) {
                        Toast.makeText(this, "Welcome " + user.displayName, Toast.LENGTH_SHORT)
                            .show()
            }
            else {
                showLoginActivity()
            }
        }
    }

    private fun checkUserFromFirebase() {
        driverInfoRef
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(this@MainActivity,"User Already Registered",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@MainActivity,p0.message,Toast.LENGTH_SHORT).show()
                }

            })
    }



    private fun showLoginActivity() {
        val authMethodPickerLayout = AuthMethodPickerLayout.Builder(R.layout.login_page)
            .setPhoneButtonId(R.id.btn_phone_signin)
            .setGoogleButtonId(R.id.btn_email_signin)
            .build();

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            LOGIN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LOGIN_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
            }
            else
                Toast.makeText(this,""+response?.error?.message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    // we are sending our user to new activity.
                    Log.e("ninad","task successful")
                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    Log.e("ninad","task Unsuccessful")
                }
            })
    }

    private fun sendVerificationCode(number: String) {
        // this method is used for getting
        // OTP on user phone number.
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // callback method is called on Phone auth provider.
    private val   // initializing our callbacks for on
    // verification callback method.
            mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // below method is used when
            // OTP is sent from Firebase
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                // when we receive the OTP it
                // contains a unique id which
                // we are storing in our string
                // which we have already created.
                verificationId = s
            }

            // this method is called when user
            // receive OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // below line is used for getting OTP code
                // which is sent in phone auth credentials.
                val code = phoneAuthCredential.smsCode

                // checking if the code
                // is null or not.
                if (code != null) {
                    // if the code is not null then
                    // we are setting that code to
                    // our OTP edittext field.


                    // after setting this code
                    // to OTP edittext field we
                    // are calling our verifycode method.
                    verifyCode(code)
                }
            }

            // this method is called when firebase doesn't
            // sends our OTP code due to any error or issue.
            override fun onVerificationFailed(e: FirebaseException) {
                // displaying error message with firebase exception.
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    // below method is use to verify code from Firebase.
    private fun verifyCode(code: String) {
        // below line is used for getting
        // credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential)
    }
}
