package it.webilend.swdex.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Film(
    @SerializedName("characters")
    @Expose
    val characters: List<String>?,
    @SerializedName("created")
    @Expose
    val created: String?,
    @SerializedName("director")
    @Expose
    val director: String?,
    @SerializedName("edited")
    @Expose
    val edited: String?,
    @SerializedName("episode_id")
    @Expose
    val episodeId: Int?,
    @SerializedName("opening_crawl")
    @Expose
    val openingCrawl: String?,
    @SerializedName("planets")
    @Expose
    val planets: List<String>?,
    @SerializedName("producer")
    @Expose
    val producer: String?,
    @SerializedName("release_date")
    @Expose
    val releaseDate: String?,
    @SerializedName("species")
    @Expose
    val species: List<String>?,
    @SerializedName("starships")
    @Expose
    val starships: List<String>?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("url")
    @Expose
    val url: String?,
    @SerializedName("vehicles")
    @Expose
    val vehicles: List<String>?
)