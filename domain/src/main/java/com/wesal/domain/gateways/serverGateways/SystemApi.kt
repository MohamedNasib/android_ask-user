package com.wesal.domain.gateways.serverGateways

import com.wesal.domain.core.ApiUrl
import com.wesal.entities.base.Calling
import com.wesal.entities.base.PaginationModel
import com.wesal.entities.models.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap
import java.util.LinkedHashMap

internal val systemApi: SystemApi by lazy { myRetrofit.create(SystemApi::class.java) }

interface SystemApi {


    @FormUrlEncoded
    @POST(ApiUrl.CHECK_PHONE)
    fun checkPhone(
        @Field("mobile") phone: String
    ): Call<Calling<StepperModel>>

    @FormUrlEncoded
    @POST(ApiUrl.RESEND_CODE_FOR_REG)
    fun resendCodeForRegister(@Field("advertiser_id") advertiserId: Int): Call<Calling<Any>>

    @FormUrlEncoded
    @POST(ApiUrl.CONFIRM_CODE_FOR_REG)
    fun confirmCodeForRegister(
        @Field("advertiser_id") advertiserId: Int,
        @Field("active_code") code: String
    ): Call<Calling<StepperModel>>

    @POST(ApiUrl.REGISTER)
    fun register(@Body map: HashMap<String, Any>): Call<Calling<UserModel>>

    @FormUrlEncoded
    @POST(ApiUrl.CHECK_PHONE_FORGET_PASSWORD)
    fun checkPhoneForForgetPassword(@Field("mobile") phone: String): Call<Calling<StepperModel>>

    @FormUrlEncoded
    @POST(ApiUrl.RESEND_CODE_FOR_FORGET)
    fun resendCodeForForgetPassword(@Field("advertiser_id") advertiserId: Int): Call<Calling<Any>>

    @FormUrlEncoded
    @POST(ApiUrl.CONFIRM_CODE_FOR_FORGET)
    fun confirmCodeForForgetPassword(
        @Field("advertiser_id") advertiserId: Int,
        @Field("forget_code") code: String
    ): Call<Calling<StepperModel>>

    @FormUrlEncoded
    @POST(ApiUrl.RESET_PASSWORD)
    fun resetPassword(
        @Field("advertiser_id") advertiserId: Int,
        @Field("password") password: String,
        @Field("password_confirmation") rePassword: String
    ): Call<Calling<Any>>


    @POST(ApiUrl.LOGIN)
    fun login(@Body map: HashMap<String, Any>): Call<Calling<UserModel>>

    @GET(ApiUrl.HOME)
    fun getHomeData(): Call<Calling<HomeModel>>

    @FormUrlEncoded
    @POST(ApiUrl.TOGGLE_ADVERT_FAVORITES)
    fun toggleFavoritesAdvert(@Field("advertisement_id") advId: Int): Call<Calling<Any>>

    @GET(ApiUrl.SUB_SECTIONS)
    fun getSubSectionList(@Path("id") sectionId: Int): Call<Calling<List<SectionModel>>>

    @GET(ApiUrl.ADVERTS_IN_SECTIONS)
    fun getAdvertsInSubSection(
        @Path("id") sectionId: Int,
        @Query("page") currentPage: Int,
        @QueryMap filters: HashMap<String, Any>
    ): Call<Calling<PaginationModel<AdvertModel>>>

    @GET(ApiUrl.ORDERS_IN_SECTIONS)
    fun getOrdersInSubSection(
        @Path("id") sectionId: Int,
        @Query("page") currentPage: Int,
        @QueryMap filters: HashMap<String, Any>
    ): Call<Calling<PaginationModel<OrdersModel>>>

    @GET(ApiUrl.ADVERT_DETAILS)
    fun getAdvertDetails(@Path("id") advertId: Int): Call<Calling<AdvertContentModel>>

    @POST(ApiUrl.ADD_COMMENT)
    fun addComment(@Body map: HashMap<String, Any>): Call<Calling<AddCommentModel>>

    @POST(ApiUrl.REMOVE_COMMENT)
    fun deleteComment(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @POST(ApiUrl.ADD_RATE)
    fun addRate(@Body map: HashMap<String, Any>): Call<Calling<AddRateModel>>

    @POST(ApiUrl.REMOVE_RATE)
    fun deleteRate(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET(ApiUrl.ADVERT_ON_MAP)
    fun getAdvertOnMap(
        @Path("id") sectionId: Int,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Call<Calling<PaginationModel<AdvertModel>>>

    @GET(ApiUrl.ADVERTER_PAGE)
    fun getAdverterPage(
        @Path("id") adverterId: Int,
        @Query("page") currentPage: Int
    ): Call<Calling<PaginationModel<AdvertModel>>>

    @GET(ApiUrl.CREATE_ADVERT_TERMS)
    fun getAdvertTermsAndConditions(): Call<Calling<FixedPageModel>>

    @POST(ApiUrl.CREATE_ADVERT_STEP_CHECK_TERMS)
    fun createAdvertStepCheckPage(): Call<Calling<AdvertStepperModel>>

    @GET(ApiUrl.PACKAGES_LIST)
    fun getPackagesList(): Call<Calling<List<PackageModel>>>

    @GET(ApiUrl.BANKS_LIST)
    fun loadBanks(): Call<Calling<List<BanksModel>>>

    @GET(ApiUrl.BANKS_INFO)
    fun getBankInfo(@Path("id") bankAccountId: Int): Call<Calling<BanksDetailsModel>>

    @POST(ApiUrl.PAY_PACKAGE)
    fun payPackageBackObject(@Body map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>>

    @POST(ApiUrl.PAY_PACKAGE)
    fun payPackageBackString(@Body map: HashMap<String, Any>): Call<Calling<String>>

    @GET(ApiUrl.GET_MAIN_CATEGORIES)
    fun getMainCategories(): Call<Calling<List<SectionModel>>>

    @GET(ApiUrl.GET_SUB_CATEGORIES)
    fun getSubMainCategories(@Path("id") sectionId: Int): Call<Calling<List<SectionModel>>>

    @POST(ApiUrl.CREATE_ADVERT_STEP_SECTION)
    fun createAdvertStepSection(@Body map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>>

    @JvmSuppressWildcards
    @Multipart
    @POST(ApiUrl.CREATE_ADVERT_STEP_MEDIA)
    fun createAdvertStepMedia(@PartMap map: Map<String, RequestBody>): Call<Calling<AdvertStepperModel>>

    @POST(ApiUrl.CHECK_DRAFT_ADVERTS)
    fun checkDraftAdverts(): Call<Calling<AdvertStepperModel>>

    @POST(ApiUrl.MAKE_NEW_ADVERT_ID)
    fun makeNewAdvertId(@Body map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>>

    @GET(ApiUrl.SIDES_LIST)
    fun getSidesList(): Call<Calling<List<SidesModel>>>

    @GET(ApiUrl.BLOCKS_LIST)
    fun getBlocksList(): Call<Calling<List<BlocksModel>>>

    @FormUrlEncoded
    @POST(ApiUrl.ADVERT_FEATURES_LIST)
    fun getAdvertSpecifications(@Field("advertisement_id") advertId: Int): Call<Calling<List<AdvertFeaturesModel>>>

    @POST(ApiUrl.CREATE_ADVERT_STEP_FEATURES)
    fun createAdvertStepFeature(@Body map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>>

    @POST(ApiUrl.CREATE_ADVERT_STEP_CONTACT)
    fun createAdvertStepContact(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    ////////////////Orders
    @POST(ApiUrl.CREATE_ORDER_STEP_CHECK)
    fun createOrderStepCheckPage(): Call<Calling<OrderStepperModel>>

    @POST(ApiUrl.CREATE_ORDER_STEP_GET_ID)
    fun makeNewOrderId(@Body map: HashMap<String, Any>): Call<Calling<OrderStepperModel>>

    @POST(ApiUrl.CREATE_ORDER_STEP_SECTION)
    fun createOrderStepSection(@Body map: HashMap<String, Any>): Call<Calling<OrderStepperModel>>

    @FormUrlEncoded
    @POST(ApiUrl.GET_ORDER_SPECIFICATION)
    fun getOrderSpecifications(@Field("order_id") orderId: Int): Call<Calling<List<AdvertFeaturesModel>>>

    @POST(ApiUrl.CREATE_ORDER_STEP_SPECIFICATION)
    fun createOrderStepFeature(@Body map: LinkedHashMap<String, Any>): Call<Calling<OrderStepperModel>>

    @POST(ApiUrl.CREATE_ORDER_STEP_CONTACT)
    fun createOrderStepContact(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET(ApiUrl.GET_ORDER_DETAILS)
    fun getOrderDetails(@Path("id") orderId: Int): Call<Calling<OrderContentModel>>

    @FormUrlEncoded
    @POST(ApiUrl.CREATE_COMMENT_FOR_ORDER)
    fun addCommentToOrder(
        @Field("order_id") orderId: Int,
        @Field("comment") comment: String
    ): Call<Calling<AddCommentModel>>

    @FormUrlEncoded
    @POST(ApiUrl.REMOVE_COMMENT_FOR_ORDER)
    fun deleteCommentFromOrder(@Field("comment_id") commentId: Int): Call<Calling<Any>>

    @GET(ApiUrl.GET_ASK_HAIL)
    fun getAskHailList(@Query("page") currentPage: Int): Call<Calling<PaginationModel<AskHailModel>>>

    @JvmSuppressWildcards
    @Multipart
    @POST(ApiUrl.CREATE_ASK_HAIL)
    fun createAskHail(@PartMap map: LinkedHashMap<String, RequestBody>): Call<Calling<Any>>

    @GET(ApiUrl.GET_ASK_HAIL_DETAILS)
    fun getAskDetails(@Path("id") askId: Int): Call<Calling<AskHailContentModel>>

    @FormUrlEncoded
    @POST(ApiUrl.ADD_COMMENT_TO_ASK)
    fun addCommentToAsk(
        @Field("question_id") askId: Int,
        @Field("comment") comment: String
    ): Call<Calling<AddCommentModel>>

    @FormUrlEncoded
    @POST(ApiUrl.REMOVE_COMMENT_TO_ASK)
    fun deleteCommentFromAsk(@Field("comment_id") commentId: Int): Call<Calling<Any>>

    @GET(ApiUrl.GET_FIXED_PAGE)
    fun getFixedPages(): Call<Calling<List<FixedPageModel>>>

    @GET(ApiUrl.GET_FIXED_PAGE_DETAILS)
    fun getFixedPageDetails(@Path("slug") fixedPageSlug: String): Call<Calling<FixedPageModel>>

    @GET(ApiUrl.GET_ABOUT_APP)
    fun getAboutApp(): Call<Calling<AboutAppModel>>

    @POST(ApiUrl.SEND_CONTACT_US)
    fun sendContactUs(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET(ApiUrl.GET_SERVICE_LIST)
    fun getServicesList(@Path("type") serviceType: String): Call<Calling<List<ServicesModel>>>

    @GET(ApiUrl.GET_SETTINGS)
    fun getSettings(): Call<Calling<SettingsModel>>

    @POST(ApiUrl.CHANGE_SETTINGS)
    fun changeSettings(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET(ApiUrl.GET_JOBS_LIST)
    fun getJobs(): Call<Calling<PaginationModel<JobsModel>>>

    @POST(ApiUrl.APPLY_FOR_JOB)
    fun applyForJob(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @POST(ApiUrl.APPLY_FOR_SHOT)
    fun snapShotRequest(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @POST(ApiUrl.CREATE_ADVERT_STEP_SOCIAL)
    fun createAdvertStepSocial(@Body map: HashMap<String, Any>): Call<Calling<Any>>
    @POST(ApiUrl.EDIT_ADVERT_SOCIAL)
    fun editAdvertStepSocial(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET
    fun getPrayingTime(
        @Url url: String,
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("method") method: Int
    ): Call<Calling<PrayingTimeModel>>

    @GET(ApiUrl.GET_SPECIAL_ADVERTS)
    fun getSpecialAdvertsList(
        @Query("page") currentPage: Int,
        @QueryMap queryMap: HashMap<String, Any>
    ): Call<Calling<PaginationModel<AdvertModel>>>

    @GET
    fun getWeatherInformation(@Url url: String): Call<WeatherContainerModel>

    @GET(ApiUrl.GET_FAV_ADVERT)
    fun getMyFavouritesAdvert(): Call<Calling<List<AdvertModel>>>

    @POST(ApiUrl.CLEAR_FAV_ADVERT)
    fun clearFavouritesAdverts(): Call<Calling<Any>>

    @GET(ApiUrl.GET_MY_ADVERTS)
    fun getMyAdverts(@Query("page") currentPage: Int): Call<Calling<PaginationModel<AdvertModel>>>

    @POST(ApiUrl.EDIT_ADVERT_SECTIONS)
    fun editAdvertStepSection(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET(ApiUrl.GET_HIGHLIGHT_INFO)
    fun getHighlightInfo(): Call<Calling<HighlightInfoModel>>

    @GET(ApiUrl.GET_HIGHLIGHT_PACKAGES)
    fun getHighlightPackages(@Query("special_with") advertPlace: String): Call<Calling<List<PackageModel>>>

    @POST(ApiUrl.PAY_HIGHLIGHT_PACKAGES)
    fun payHighlightBackString(@Body map: HashMap<String, Any>): Call<Calling<String>>

    @POST(ApiUrl.PAY_HIGHLIGHT_PACKAGES)
    fun payHighlightBackObject(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @FormUrlEncoded
    @POST(ApiUrl.ACTIVE_OR_BLOCK_ADVERT)
    fun activeOrBlockAdverts(
        @Field("advertisement_id") advertId: Int,
        @Field("status") status: String
    ): Call<Calling<Any>>

    @GET(ApiUrl.GET_INACTIVE_ADVERTS)
    fun showInactiveAdverts(): Call<Calling<List<AdvertModel>>>

    @POST(ApiUrl.ACTIVE_ALL_ADVERTS)
    fun activeAllInactiveAdverts(): Call<Calling<Any>>

    @FormUrlEncoded
    @POST(ApiUrl.DELETE_ADVERTS)
    fun deleteAdvert(@Field("advertisement_id") advertId: Int): Call<Calling<Any>>

    @GET(ApiUrl.GET_MY_ORDERS)
    fun getMyOrdersList(@Query("page") currentPage: Int): Call<Calling<PaginationModel<OrdersModel>>>

    @FormUrlEncoded
    @POST(ApiUrl.DELETE_ORDER)
    fun deleteOrder(@Field("order_id") orderId: Int): Call<Calling<Any>>

    @GET(ApiUrl.GET_NOTIFICATIONS)
    fun getNotifications(): Call<Calling<List<NotificationsModel>>>
    @GET(ApiUrl.GET_UNREAD_NOTIFICATIONS)
    fun getUnreadNotifications(): Call<Calling<String>>

    @POST(ApiUrl.REMOVE_ALL_NOTIFICATIONS)
    fun removeAllNotifications(): Call<Calling<Any>>

    @POST(ApiUrl.REMOVE_ONE_NOTIFICATIONS)
    fun removeOneNotification(@Path("id") notifyId: String): Call<Calling<Any>>

    @GET(ApiUrl.GET_MY_QUESTIONS)
    fun getMyQuestions(@Query("page") currentPage: Int): Call<Calling<PaginationModel<AskHailModel>>>

    @FormUrlEncoded
    @POST(ApiUrl.REMOVE_QUESTION)
    fun deleteQuestion(@Field("question_id") askId: Int): Call<Calling<Any>>

    @POST(ApiUrl.EDIT_QUESTION)
    fun editQuestion(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @JvmSuppressWildcards
    @Multipart
    @POST(ApiUrl.EDIT_QUESTION)
    fun editingQuestion(@PartMap map: LinkedHashMap<String, RequestBody>): Call<Calling<Any>>

    @GET(ApiUrl.GET_USER_INFO)
    fun getUserInfo(): Call<Calling<AccountInformationModel>>

    @FormUrlEncoded
    @POST(ApiUrl.UPDATE_USER_INFO)
    fun editPersonalInfo(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile") phone: String
    ): Call<Calling<Any>>

    @POST(ApiUrl.CHANGE_PASSWORD)
    fun changePassword(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET(ApiUrl.GET_MY_PACKAGE)
    fun getMyCurrentPackage(): Call<Calling<MyPackageModel>>

    @POST(ApiUrl.RENEW_MY_PACKAGE)
    fun reNewPackageBackString(@Body map: HashMap<String, Any>): Call<Calling<String>>

    @POST(ApiUrl.RENEW_MY_PACKAGE)
    fun renewPackageBackObject(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @POST(ApiUrl.UPDATE_MY_PACKAGE)
    fun updatePackageBackString(@Body map: HashMap<String, Any>): Call<Calling<String>>

    @POST(ApiUrl.UPDATE_MY_PACKAGE)
    fun updatePackageBackObject(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @POST(ApiUrl.REPLAY_ON_QUESTION)
    fun replayOnQuestionComment(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @POST(ApiUrl.REPLAY_ON_ADVERT)
    fun replayOnAdvertComment(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @POST(ApiUrl.REPLAY_ON_ORDER)
    fun replayOnOrderComment(@Body map: HashMap<String, Any>): Call<Calling<Any>>

    @GET(ApiUrl.GET_MESSAGES)
    fun getMessages(
        @Query("page") currentPage: Int,
        @Query("show_filter") showingFilter: String
    ): Call<Calling<PaginationModel<MessagesModel>>>
    @FormUrlEncoded
    @POST(ApiUrl.GET_CHAT_HISTORY)
    fun getChatHistory(
        @Field("page") currentPage: Int,
        @Field("chat_id") chatId: Int
    ): Call<Calling<ChattingContentModel>>
    @JvmSuppressWildcards
    @Multipart
    @POST(ApiUrl.EDIT_ADVERT_MEDIA)
    fun updateAdvertStepMedia(@PartMap map: LinkedHashMap<String, RequestBody>): Call<Calling<Any>>
    @POST(ApiUrl.EDIT_ADVERT_SPECIFICATION)
    fun updateAdvertStepFeature(@Body map: LinkedHashMap<String, Any>): Call<Calling<Any>>
    @POST(ApiUrl.EDIT_ADVERT_CONTACT)
    fun updateAdvertStepContact(@Body map: HashMap<String, Any>): Call<Calling<Any>>
    @POST(ApiUrl.EDIT_ORDER_SECTION)
    fun editOrderStepSection(@Body map: HashMap<String, Any>): Call<Calling<Any>>
    @POST(ApiUrl.EDIT_ORDER_SPECIFICATION)
    fun editOrderStepFeature(@Body map: LinkedHashMap<String, Any>): Call<Calling<Any>>
    @POST(ApiUrl.EDIT_ORDER_CONTACT)
    fun editOrderStepContact(@Body map: HashMap<String, Any>): Call<Calling<Any>>
    @GET(ApiUrl.SPECIAL_ADVERT_PACKAGE_INFO)
    fun getAdvertSpecialPackageInfo(@Query("advertisement_id") advertId: Int): Call<Calling<MyPackageModel>>
    @GET(ApiUrl.GET_COMMENTS_ADVERTS)
    fun getAdvertComments(
        @Path("id")    advertId: Int,
        @Query("page") currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>>
    @GET(ApiUrl.GET_COMMENTS_ORDER)
    fun getOrderComments(
       @Path("id")     orderId: Int,
       @Query("page")  currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>>
    @GET(ApiUrl.GET_COMMENTS_QUESTIONS)
    fun getQuestionsComments(
       @Path("id")     questionId: Int,
       @Query("page")  currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>>
    @GET(ApiUrl.GET_RATES_ADVERTS)
    fun getAdvertRates(
        @Path("id")     advertId: Int,
        @Query("page")  currentPage: Int
    ): Call<Calling<PaginationModel<RateModel>>>
    @FormUrlEncoded
    @POST(ApiUrl.GET_CHAT_HISTORY)
    fun createChat(
        @Field("chat_type") type: String,
        @Field("chat_type_id") relatedId: Int
    ): Call<Calling<ChattingContentModel>>

    @POST(ApiUrl.LOG_OUT)
    fun logOut(): Call<Calling<Any>>
}