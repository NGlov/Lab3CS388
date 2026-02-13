package com.example.lab3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray

private const val API_KEY = "IoCgCdflRWSMzs1uByrCTe6ZaZ1BwtMWoPztv7rg"

class NationalParksFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_national_parks_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        // Using the client, perform the HTTP request
        client[
            "https://developer.nps.gov/api/v1/parks?limit=20",
            params,
            object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // Filter out the "data" JSON array and turn into a String
                    val dataJSON = json.jsonObject.get("data") as JSONArray
                    val parksRawJSON = dataJSON.toString()

                    // Create a Gson instance to help parse the raw JSON
                    val gson = Gson()

                    // Tell Gson what type we’re expecting (a list of NationalPark objects)
                    val arrayParkType = object : TypeToken<List<NationalPark>>() {}.type

                    // Convert the raw JSON string into a list of actual NationalPark data models
                    val models: List<NationalPark> = gson.fromJson(parksRawJSON, arrayParkType)

                    recyclerView.adapter = NationalParksRecyclerViewAdapter(models, this@NationalParksFragment)

                    // Look for this in Logcat:
                    Log.d("NationalParksFragment", "response successful")
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("NationalParksFragment", errorResponse)
                    }
                }
            }]
    }

    override fun onItemClick(item: NationalPark) {
        // Handle click event if needed
    }
}
