package com.blogspot.thengnet.slingacademyusers

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.loader.content.AsyncTaskLoader

class UsersLoader(context: Context?, lookupUsers: Bundle) : AsyncTaskLoader<List<User>?>(
    context!!
) {
    private val usersURL: String?
    private var result: List<User>? = null

    init {
        usersURL = lookupUsers.getString("link")
    }

    override fun deliverResult(data: List<User>?) {
        result = data
        super.deliverResult(data)
    }

    override fun onStartLoading() {
        super.onStartLoading()
        if (result != null) {
            deliverResult(result)
        } else forceLoad()
    }

    override fun loadInBackground(): List<User>? {
        Log.v("UsersLoader", "loadInBackground() called.")

        // Don't perform the request if there are no URLs, or the first URL is null.
        return if (usersURL == null) {
            null
        } else {
            Log.v("UserLoader", "loadinginbg")
            // Call static method #lookUpArticles, passing context passed when class was
            // instantiated by call to super(context), the {@link URL} & API code
            result = FetchUsers.lookupArticles(context, usersURL)
            result
        }
    }
}