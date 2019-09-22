package com.theobviousexit.rawg

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RawgApi {
    @Headers("User-Agent: com.theobviousexit.rawg")
    @GET("games?page_size=100")
    suspend fun getGames(@Query("search") name: String): RawgResponse
}

//games search
@Parcelize
data class Result(
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("rating") val rating:String = "",
    @SerializedName("clip") val clip: Clip? = null,
    @SerializedName("background_image") val backgroundImage: String = "",
    @SerializedName("metacritic") val metacritic:Long = 0,
    @SerializedName("short_screenshots") val shortScreenshots:List<ShortScreenshot> = emptyList(),
    @SerializedName("platforms") val platforms:List<PlatformObj> = emptyList()

): Parcelable

@Parcelize
data class ShortScreenshot(
    @SerializedName("id") val id:Long = 0,
    @SerializedName("image") val image:String = ""
): Parcelable

@Parcelize
data class Clip(
    @SerializedName("clip") val clip: String = "",
    @SerializedName("clips") val clips: Clips? = null,
    @SerializedName("preview") val preview:String = ""
) : Parcelable

@Parcelize
data class Clips(
    @SerializedName("320") val small:String = "",
    @SerializedName("640") val medium:String = "",
    @SerializedName("full") val full:String = ""
):Parcelable

@Parcelize
data class PlatformObj(
    @SerializedName("platform") val platform:Platform?=null
):Parcelable

@Parcelize
data class Platform(
    @SerializedName("id") val id:Int = 0,
    @SerializedName("name") val name:String = "",
    @SerializedName("slug") val slug:String = ""
):Parcelable

@Parcelize
data class RawgResponse(
    val next: String? = null,
    val previous:String? = null,
    val results: List<Result> = arrayListOf()
) : Parcelable

//game details
@Parcelize
data class GameDetailResult(
    @SerializedName("id") val id:Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("background_image") val backgroundImage: String = "",
    @SerializedName("background_image_additional") val backgroundImageAdditional:String = "",
    @SerializedName("description") val description:String = "",
    @SerializedName("metacritic") val metacritic:Long = 0,
    @SerializedName("saturated_color") val saturatedColor:String = "",
    @SerializedName("dominant_color") val dominantColor:String = ""
):Parcelable