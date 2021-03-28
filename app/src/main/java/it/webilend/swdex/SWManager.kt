package it.webilend.swdex

import com.vishal.weather.kotlin.network.SWAPIClient
import it.webilend.swdex.model.Character
import it.webilend.swdex.network.SWRestService

object SWManager{
    var characters:MutableList<Character> = mutableListOf()
    val swRestService: SWRestService =
        SWAPIClient.getClient().create(SWRestService::class.java)

    init {
        println("SWManager created!")
    }

}