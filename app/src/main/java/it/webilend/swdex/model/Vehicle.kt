package it.webilend.swdex.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("cargo_capacity")
    @Expose
    val cargoCapacity: String?,
    @SerializedName("consumables")
    @Expose
    val consumables: String?,
    @SerializedName("cost_in_credits")
    @Expose
    val costInCredits: String?,
    @SerializedName("created")
    @Expose
    val created: String?,
    @SerializedName("crew")
    @Expose
    val crew: String?,
    @SerializedName("edited")
    @Expose
    val edited: String?,
    @SerializedName("films")
    @Expose
    val films: List<String>?,
    @SerializedName("length")
    @Expose
    val length: String?,
    @SerializedName("manufacturer")
    @Expose
    val manufacturer: String?,
    @SerializedName("max_atmosphering_speed")
    @Expose
    val maxAtmospheringSpeed: String?,
    @SerializedName("model")
    @Expose
    val model: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("passengers")
    @Expose
    val passengers: String?,
    @SerializedName("pilots")
    @Expose
    val pilots: List<Any>?,
    @SerializedName("url")
    @Expose
    val url: String?,
    @SerializedName("vehicle_class")
    @Expose
    val vehicleClass: String?
)