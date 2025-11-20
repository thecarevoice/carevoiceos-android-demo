
import com.google.gson.annotations.SerializedName

data class DeepLinkBean(
    @SerializedName("codeChallenge")
    val codeChallenge: String,
    @SerializedName("codeChallengeMethod")
    val codeChallengeMethod: String,
    @SerializedName("cvUserUniqueId")
    val cvUserUniqueId: String
)

data class DeepLinkBeanResponse(
    @SerializedName("deepLink")
    val deepLink: String,
    @SerializedName("token")
    val token: String,


    @SerializedName("cvUserUniqueId")
    val cvUserUniqueId: String,
    @SerializedName("expiresAtEpochMilli")
    val expiresAtEpochMilli: Long,
    @SerializedName("expiresInSeconds")
    val expiresInSeconds: Long,

)
