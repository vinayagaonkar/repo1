package com.example.example

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "my_list")
data class ResultResponse (
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id") var id : Int,
  @SerializedName("copyright"       ) var copyright      : String? = null,
  @SerializedName("date"            ) var date           : String?=null,
  @SerializedName("explanation"     ) var explanation    : String? = null,
  @SerializedName("hdurl"           ) var hdurl          : String? = null,
  @SerializedName("media_type"      ) var mediaType      : String? = null,
  @SerializedName("service_version" ) var serviceVersion : String? = null,
  @SerializedName("title"           ) var title          : String? = null,
  @SerializedName("url"             ) var url            : String? = null,

)