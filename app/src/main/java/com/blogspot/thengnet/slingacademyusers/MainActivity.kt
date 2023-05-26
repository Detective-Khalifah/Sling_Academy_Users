package com.blogspot.thengnet.slingacademyusers

import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.google.android.material.snackbar.Snackbar
import com.blogspot.thengnet.slingacademyusers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<User>?>  {

    // Data binding blueprint/class of MainActivity
    private lateinit var mMainBinding: ActivityMainBinding

    private var usersAdapter: UserAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Create a new {@link ThoughtAdapter} that takes an empty, non-null {@link ArrayList} of
        // {@link Thoughts} as input.
        usersAdapter = UserAdapter(this, ArrayList())

        mMainBinding.listUsers.adapter = usersAdapter

        val url: Bundle = Bundle()
        url.putString(
            "link",
            "https://api.slingacademy.com/v1/sample-data/users"
        )

        val base: Uri =
            Uri.parse("https://dev.deepthought.education/assets/uploads/files/others/project.json")

        // Check network state and start up {@link Loader}, passing generated {@link URL} if it's
        // connected, otherwise notify via {@link Snackbar}
        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connManager.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected) {
            val loaderManager = supportLoaderManager
            loaderManager.initLoader(
                LOADER_ID, url,
                this@MainActivity as LoaderManager.LoaderCallbacks<List<User>>
            )
        } else {
            mMainBinding.pbUsers.visibility = View.GONE
            Snackbar.make(
                this, mMainBinding.frameSnack, "No net access!",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<User>?> {
        Log.v("MainActivity", "onCreateLoader() called.")
        return UsersLoader(this, bundle!!)
    }

    override fun onLoadFinished(loader: Loader<List<User>?>, data: List<User>?) {
        mMainBinding.pbUsers.visibility = View.GONE
        usersAdapter!!.clear()

        // If there is a valid list of {@link Thoughts}, then add them to the {@link ThoughtsAdapter}'s dataset.
        // This will trigger the {@link ListView} to update.
        if (data != null && data.isNotEmpty()) {
            mMainBinding.tvNoUsers.visibility = View.VISIBLE
            usersAdapter!!.addAll(data)
        } else {
            mMainBinding.tvNoUsers.setText(R.string.no_user_fetched)
            mMainBinding.tvNoUsers.visibility = View.VISIBLE
        }
    }

    override fun onLoaderReset(loader: Loader<List<User>?>) {
        usersAdapter!!.clear()
    }

    companion object {
        private const val LOADER_ID = 2
    }

}