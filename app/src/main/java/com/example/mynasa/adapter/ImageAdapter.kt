package com.example.mynasa.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.example.ResultResponse
import com.example.mynasa.R
import com.example.mynasa.database.MyListDao
import com.example.mynasa.database.MylistDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImageAdapter(private val mList: List<ResultResponse>,private val isFavoritePage:Boolean) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private lateinit var mContext:Context
    private lateinit var myListDao: MyListDao

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the list_cardview view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_cardview, parent, false)
        mContext=parent.context
        myListDao = MylistDB.getMyListDatabase(mContext)?.getMyListDao()!!
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        // sets the image to the imageview from our itemHolder class using glide
        holder.imageView?.let {
            Glide.with(mContext!!)
                .load(item?.url)
                .error(R.color.black)
                .into(it)
        }
        // sets the text to the textview from our itemHolder class
        holder.title.text = item.title
        holder.date.text = item.date
        holder.description.text = item.explanation
        //hence we are using same adapter for search and favourite pages need to hide the views based on screen
        if(isFavoritePage){
            holder.favorite.visibility=View.GONE
        }else{
            holder.favorite.visibility=View.VISIBLE
        }

        updateIcon(holder,item)

        holder.favorite.setOnClickListener({
            Onclick(holder,item)
        })
    }

    /*This method handles UI changes and DBB operations after onclick of favorites icon*/
    private fun Onclick(holder: ImageAdapter.ViewHolder, item: ResultResponse) {
        GlobalScope.launch {
            item?.date?.let {
                val isExists =myListDao.exists(it)
                Handler(Looper.getMainLooper()).post({
                    if(isExists){
                        // if yes, clicking again on favorite icon will remove from favorites
                        holder.favorite.setBackgroundResource(R.drawable.favorite_bordered)
                        item.date?.let { it1 -> removeFromFavourites(it1) }
                    }else{
                        // then add to favorites
                        holder.favorite.setBackgroundResource(R.drawable.favorite_red)
                        insertToFavorites(item)
                    }
                })
            }
        }
    }
    /*This method checks if the item is already added to favorites, based on that will show favorite icon*/
    private fun updateIcon(holder: ImageAdapter.ViewHolder, item: ResultResponse) {
        GlobalScope.launch {
            item?.date?.let {
               val isExists =myListDao.exists(it)
                Handler(Looper.getMainLooper()).post({
                    if(isExists){
                        holder.favorite.setBackgroundResource(R.drawable.favorite_red)
                    }else{
                        holder.favorite.setBackgroundResource(R.drawable.favorite_bordered)
                    }
                })
            }
        }
    }

    private fun insertToFavorites(item: ResultResponse) {
        Toast.makeText(mContext,"Added to favourites",Toast.LENGTH_SHORT).show()
        //coroutines will run the DB operation in other threads
        GlobalScope.launch {
            myListDao.insertWatchList(item)

        }
    }

    private fun removeFromFavourites(@NonNull date: String) {
        Toast.makeText(mContext,"Remove from favourites",Toast.LENGTH_SHORT).show()
        GlobalScope.launch {
            myListDao.removeWatchList(date)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val description: TextView = itemView.findViewById(R.id.description)
        val favorite:ImageView=itemView.findViewById(R.id.favoutite)
    }
}
