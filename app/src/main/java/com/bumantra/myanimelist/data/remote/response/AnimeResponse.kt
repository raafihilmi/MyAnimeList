package com.bumantra.myanimelist.data.remote.response

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
	@field:SerializedName("data")
	val data: List<DataItem>
)

data class Images(

	@field:SerializedName("jpg")
	val jpg: Jpg,
)

data class DataItem(


	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("images")
	val images: Images,

	@field:SerializedName("mal_id")
	val malId: Int,

	@field:SerializedName("synopsis")
	val synopsis: String,
)

data class Jpg(

	@field:SerializedName("image_url")
	val imageUrl: String
)

