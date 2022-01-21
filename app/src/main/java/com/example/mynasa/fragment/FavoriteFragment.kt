package com.example.mynasa.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example.ResultResponse
import com.example.mynasa.R
import com.example.mynasa.adapter.ImageAdapter
import com.example.mynasa.database.MyListDao
import com.example.mynasa.database.MylistDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var adapter: ImageAdapter
    private lateinit var favoriteRecyclerView: RecyclerView
    private  var list:List<ResultResponse> = ArrayList()
    private lateinit var myListDao: MyListDao
    private lateinit var emptyText:TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.favourite_list_layout,container,false)
        myListDao = MylistDB.getMyListDatabase(requireContext())?.getMyListDao()!!
        favoriteRecyclerView=view.findViewById(R.id.favorite_listview)
        emptyText=view.findViewById(R.id.empty_text)

        getAllFavourites()
        return view
    }

    private fun getAllFavourites() {
       GlobalScope.launch {
           list=myListDao.getAllData()
           Log.d("validtag", "getAllFavourites: "+list.toString())
           Handler(Looper.getMainLooper()).post({
               if(list.isNullOrEmpty()){
                   emptyText.visibility=View.VISIBLE
               }else{
                   emptyText.visibility=View.GONE
                   adapter= ImageAdapter(list, true)
                   favoriteRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                   favoriteRecyclerView.adapter=adapter
                  // adapter.notifyDataSetChanged()
               }

               }
           )
       }
    }
}