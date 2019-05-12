package com.movieapp

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Base activity for all other activities.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        MultiDex.install(this);
        configureToolbar()
    }

    /**
     * Configure toolbar for the activity.
     */
    private fun configureToolbar() {
            supportActionBar!!.title = getToolbarTitle()
        }

     abstract fun getLayoutResource(): Int

      abstract fun getToolbarTitle(): String?

    // Progress dialog instance.
    var progressDialog: ProgressDialog? = null

    /**
     * Display progress dialog.
     */
    fun showProgressDialog(){
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        if (!progressDialog!!.isShowing){
            progressDialog!!.setMessage("Please wait....")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    /**
     * Dismiss progress dialog.
     */
    fun dismissProgressDialog(){
        if (progressDialog != null){
            progressDialog!!.dismiss()
            progressDialog = null;
        }
    }

    /**
     * Show toast message utility.
     */
    fun showToast(message: String) {
        Toast.makeText(applicationContext, " " + message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Check network availability.
     */
    fun isNetworkAvailable(): Boolean {
        val cm = AppController.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    /**
     * Hide keyboard programmatically.
     */
    fun hideKeyboard1(){
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
    }
}