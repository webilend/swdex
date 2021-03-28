package it.webilend.swdex.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    @Expose
    var id: String?,
    @SerializedName("birth_year")
    @Expose
    val birthYear: String?,
    @SerializedName("created")
    @Expose
    val created: String?,
    @SerializedName("edited")
    @Expose
    val edited: String?,
    @SerializedName("eye_color")
    @Expose
    val eyesColor: String?,
    @SerializedName("films")
    @Expose
    val films: List<String>?,
    @SerializedName("gender")
    @Expose
    val gender: String?,
    @SerializedName("hair_color")
    @Expose
    val hairColor: String?,
    @SerializedName("height")
    @Expose
    val height: String?,
    @SerializedName("homeworld")
    @Expose
    val homeworld: String?,
    @SerializedName("mass")
    @Expose
    val mass: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("skin_color")
    @Expose
    val skinColor: String?,
    @SerializedName("species")
    @Expose
    val species: List<String>?,
    @SerializedName("starships")
    @Expose
    val starships: List<String>?,
    @SerializedName("url")
    @Expose
    val url: String?,
    @SerializedName("vehicles")
    @Expose
    val vehicles: List<String>?
)