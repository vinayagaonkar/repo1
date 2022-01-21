package com.example.mynasa.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example.ResultResponse
import com.example.mynasa.R
import com.example.mynasa.adapter.ImageAdapter
import com.example.mynasa.fragment.FavoriteFragment
import com.example.mynasa.viewmodel.ImageViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var dateEditText: EditText
    private lateinit var search:ImageButton
    private lateinit var viewModel: ImageViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter:ImageAdapter
    private lateinit var viewFavourites:ImageView
    private val listOfResult:ArrayList<ResultResponse> = ArrayList<ResultResponse>()
    private  val fragmentmanager:FragmentManager=supportFragmentManager
    companion object{
        val baseUrl:String="https://api.nasa.gov/"
        val api_key:String="NVGmQm3tqUwT1ub2SHZ9OqgVJyO036PAhje8suXS"
        val fromDate:String="2021-10-12"
        val endDate:String="2021-11-10"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, WatchViewModelFactory()).get(ImageViewModel::class.java)
        imageAdapter= ImageAdapter(listOfResult, false)
        dateEditText=findViewById(R.id.date_field)
        search=findViewById(R.id.search_button)
        viewFavourites=findViewById(R.id.favourite_view)
        recyclerView=findViewById(R.id.recycler_view)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=imageAdapter
        viewFavourites.setOnClickListener({
            //open new fragment
              fragmentmanager.beginTransaction()
                .replace(R.id.frame, FavoriteFragment())
                .addToBackStack("FavoriteFragment")
                .commit()
        })
        search.setOnClickListener({
            if(dateEditText.text.isNotEmpty()) {
                startSearch()
            }else{
                Toast.makeText(this,"Please enter date to search",Toast.LENGTH_LONG).show()
            }
        })
        dateEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length==0){
                    viewModel.getSomePictures()   // to reload some pictures when edit text field is empty
                }
            }
        })
        viewModel.init() //to initialize the required objects
        viewModel.getSomePictures() // to show some images when we launch the application
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fragmentmanager.popBackStack()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPicturesList().observe(this, Observer {
            listOfResult.clear()
            if (it!=null && listOfResult.isEmpty()) {
                listOfResult.addAll(it)
                imageAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun startSearch() {
        val date: String? =dateEditText?.text?.toString()
        date?.let { viewModel.getPictureOfTheDay(it) }
    }
    inner class WatchViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ImageViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

