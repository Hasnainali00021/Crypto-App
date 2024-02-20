package com.example.cryptoapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptoapp.databinding.ActivityMainBinding
import java.util.Locale     // A class representing a specific geographical, political, or cultural region

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding  //binding: A variable to hold the generated binding class for accessing views in the ActivityMain layout.
    private lateinit var rvAdapter: RvAdapter  //rvAdapter: A variable to hold the adapter for the RecyclerView, responsible for managing and displaying items in the list.
    private lateinit var data:ArrayList<Modal>  // data: An ArrayList to store cryptocurrency data, likely represented using a custom class called "Modal."
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        data = ArrayList<Modal>()
        apiData   // Calling API function
        rvAdapter = RvAdapter(this,data)   // Creates a new RvAdapter instance, passing the current activity context and the data ArrayList.
        binding.Rv.layoutManager = LinearLayoutManager(this)  // Vertical Linear View
        binding.Rv.adapter = rvAdapter // Assigns the created RvAdapter to the RecyclerView, enabling it to display the cryptocurrency data.

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                val filterdata = ArrayList<Modal>()
                for (item in data) {
                    if (item.name.lowercase(Locale.getDefault())
                            .contains(p0.toString().lowercase(Locale.getDefault()))
                    ) {
                        filterdata.add(item)
                    }

                }
                if (filterdata.isEmpty()) {
                    Toast.makeText(this@MainActivity, "No data available", Toast.LENGTH_LONG)
                        .show()
                } else {
                    rvAdapter.changeData(filterdata) //  Updates the RecyclerView's adapter with the filtered data if matches exist.
                }
            }
        })
    }
    val apiData:Unit
        get() {
            val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

            val queue= Volley.newRequestQueue(this)
            val jsonObjectRequest:JsonObjectRequest =

                @SuppressLint("NotifyDataSetChanged")
                object:JsonObjectRequest(Method.GET,url,null,Response.Listener {
                    response ->
                    binding.progressBar.isVisible = false
                    try {
                        val dataArray=response.getJSONArray("data")
                        for(i in 0 until dataArray.length())
                        {
                            val dataObject=dataArray.getJSONObject(i)
                            val symbol = dataObject.getString("symbol")
                            val name = dataObject.getString("name")
                            val qoute = dataObject.getJSONObject("quote")
                            val USD = qoute.getJSONObject("USD")
                            val price = String. format("$ " + "%.2f",USD.getDouble("price") )

                            data.add(Modal(name,symbol,price.toString()))
                        }
                        rvAdapter.notifyDataSetChanged()
                    } catch(e:Exception){

                        Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                })
                {
                    override fun getHeaders(): Map<String, String> {

                        val headers= HashMap<String,String>();
                        headers["X-CMC_PRO_API_KEY"]="0ad7f1f7-bf84-4952-b11f-6085fe477ba1"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
}