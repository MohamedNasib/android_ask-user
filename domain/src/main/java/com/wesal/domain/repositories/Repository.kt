package com.wesal.domain.repositories

import android.location.Location
import com.wesal.entities.base.Calling
import com.wesal.entities.base.PaginationModel
import com.wesal.entities.enums.EStartRoute
import com.wesal.entities.models.AboutAppModel
import com.wesal.entities.models.AccountInformationModel
import com.wesal.entities.models.AddCommentModel
import com.wesal.entities.models.AddRateModel
import com.wesal.entities.models.AdvMediaModel
import com.wesal.entities.models.AdvertContentModel
import com.wesal.entities.models.AdvertFeaturesModel
import com.wesal.entities.models.AdvertModel
import com.wesal.entities.models.AdvertStepperModel
import com.wesal.entities.models.AskHailContentModel
import com.wesal.entities.models.AskHailModel
import com.wesal.entities.models.BanksDetailsModel
import com.wesal.entities.models.BanksModel
import com.wesal.entities.models.BlocksModel
import com.wesal.entities.models.ChattingContentModel
import com.wesal.entities.models.CommentModel
import com.wesal.entities.models.CountryModel
import com.wesal.entities.models.FixedPageModel
import com.wesal.entities.models.HighlightInfoModel
import com.wesal.entities.models.HomeModel
import com.wesal.entities.models.JobsModel
import com.wesal.entities.models.MessagesModel
import com.wesal.entities.models.MyPackageModel
import com.wesal.entities.models.NotificationsModel
import com.wesal.entities.models.OrderContentModel
import com.wesal.entities.models.OrderStepperModel
import com.wesal.entities.models.OrdersModel
import com.wesal.entities.models.PackageModel
import com.wesal.entities.models.PrayingTimeModel
import com.wesal.entities.models.RateModel
import com.wesal.entities.models.SectionModel
import com.wesal.entities.models.ServicesModel
import com.wesal.entities.models.SettingsModel
import com.wesal.entities.models.SidesModel
import com.wesal.entities.models.StepperModel
import com.wesal.entities.models.UserModel
import com.wesal.entities.models.WeatherContainerModel
import retrofit2.Call

interface Repository {
    fun getStartRoute(): EStartRoute
    fun setLandingCompleted()
    fun clearUserData()
    fun saveCountryList(value: List<CountryModel>?)
    fun loadCountryList(): List<CountryModel>?
    suspend fun checkPhone(phone: String): Call<Calling<StepperModel>>
    fun setUserData(value: UserModel?)
    suspend fun resendCodeForRegister(advertiserId: Int): Call<Calling<Any>>
    suspend fun confirmCodeForRegister(advertiserId: Int, code: String): Call<Calling<StepperModel>>
    suspend fun register(
        advertiserId: Int,
        name: String,
        email: String,
        password: String
    ): Call<Calling<UserModel>>

    suspend fun checkPhoneForForgetPassword(value: String): Call<Calling<StepperModel>>
    suspend fun resendCodeForForgetPassword(advertiserId: Int): Call<Calling<Any>>
    suspend fun confirmCodeForForgetPassword(advertiserId: Int, code: String): Call<Calling<StepperModel>>
    suspend fun resetPassword(advertiserId: Int, password: String, rePassword: String): Call<Calling<Any>>
    suspend fun login(phone: String, password: String): Call<Calling<UserModel>>
    suspend fun getHomeData(): Call<Calling<HomeModel>>
    suspend fun toggleFavoritesAdvert(advId: Int): Call<Calling<Any>>
    suspend fun getSubSectionList(sectionId: Int): Call<Calling<List<SectionModel>>>
    suspend fun getAdvertsInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Call<Calling<PaginationModel<AdvertModel>>>

    suspend fun getAdvertDetails(advertId: Int): Call<Calling<AdvertContentModel>>
    suspend fun addComment(advertId: Int, comment: String): Call<Calling<AddCommentModel>>
    fun getCurrentUserId(): Int
    suspend fun deleteComment(commentId: Int): Call<Calling<Any>>
    suspend fun addRate(advertId: Int, rate: String): Call<Calling<AddRateModel>>
    suspend fun deleteRate(rateId: Int): Call<Calling<Any>>
    suspend fun getAdvertOnMap(
        sectionId: Int,
        latitude: Double,
        longitude: Double
    ): Call<Calling<PaginationModel<AdvertModel>>>

    suspend fun getAdverterPage(
        adverterId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<AdvertModel>>>

    suspend fun getAdvertTermsAndConditions(): Call<Calling<FixedPageModel>>
    suspend fun createAdvertStepCheckPage(): Call<Calling<AdvertStepperModel>>
    suspend fun getPackagesList(): Call<Calling<List<PackageModel>>>
    suspend fun loadBanks(): Call<Calling<List<BanksModel>>>
    suspend fun getBankInfo(bankAccountId: Int): Call<Calling<BanksDetailsModel>>
    suspend fun payPackageBackObject(map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>>
    suspend fun payPackageBackString(map: HashMap<String, Any>): Call<Calling<String>>
    fun getSystemLanguage(): String
    suspend fun getMainCategories(): Call<Calling<List<SectionModel>>>
    suspend fun getSubMainCategories(sectionId: Int): Call<Calling<List<SectionModel>>>
    suspend fun createAdvertStepSection(map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>>
    suspend fun createAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>
    ): Call<Calling<AdvertStepperModel>>

    suspend fun checkDraftAdverts(): Call<Calling<AdvertStepperModel>>
    suspend fun makeNewAdvertId(): Call<Calling<AdvertStepperModel>>
    suspend fun getSidesList(): Call<Calling<List<SidesModel>>>
    suspend fun getBlocksList(): Call<Calling<List<BlocksModel>>>
    suspend fun getAdvertSpecifications(advertId: Int): Call<Calling<List<AdvertFeaturesModel>>>
    suspend fun createAdvertStepFeature(map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>>
    suspend fun createAdvertStepContact(map: HashMap<String, Any>): Call<Calling<Any>>
    fun updateUserLocation(latitude: Double, longitude: Double)
    suspend fun getOrdersInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Call<Calling<PaginationModel<OrdersModel>>>

    suspend fun createOrderStepCheckPage(): Call<Calling<OrderStepperModel>>
    suspend fun makeNewOrderId(): Call<Calling<OrderStepperModel>>
    suspend fun createOrderStepSection(map: HashMap<String, Any>): Call<Calling<OrderStepperModel>>
    suspend fun getOrderSpecifications(orderId: Int): Call<Calling<List<AdvertFeaturesModel>>>
    suspend fun createOrderStepFeature(map: LinkedHashMap<String, Any>): Call<Calling<OrderStepperModel>>
    suspend fun createOrderStepContact(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun getOrderDetails(orderId: Int): Call<Calling<OrderContentModel>>
    suspend fun addCommentToOrder(orderId: Int, comment: String): Call<Calling<AddCommentModel>>
    suspend fun deleteCommentFromOrder(commentId: Int): Call<Calling<Any>>
    suspend fun getAskHailList(currentPage: Int): Call<Calling<PaginationModel<AskHailModel>>>
    suspend fun createAskHail(
        selectedImage: String?,
        title: String,
        desc: String,
        public: Boolean
    ): Call<Calling<Any>>

    suspend fun getAskDetails(askId: Int): Call<Calling<AskHailContentModel>>
    suspend fun addCommentToAsk(askId: Int, comment: String): Call<Calling<AddCommentModel>>
    suspend fun deleteCommentFromAsk(commentId: Int): Call<Calling<Any>>
    suspend fun getFixedPages(): Call<Calling<List<FixedPageModel>>>
    fun cacheFixedPages(value: List<FixedPageModel>?)
    fun loadFixedPages():List<FixedPageModel>?
    suspend fun getFixedPageDetails(fixedPageSlug: String): Call<Calling<FixedPageModel>>
    suspend fun getAboutApp(): Call<Calling<AboutAppModel>>
    suspend fun sendContactUs(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun getServicesList(serviceType: String): Call<Calling<List<ServicesModel>>>
    suspend fun getSettings(): Call<Calling<SettingsModel>>
    fun changeLanguage(targetLanguage: String)
    suspend fun changeSettings(
        notification: String,
        comments: String,
        chat: String,
        questions: String,
        fav: String,
        language: String
    ): Call<Calling<Any>>

    suspend fun createAdvertStepSocial(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun editAdvertStepSocial(map: HashMap<String, Any>): Call<Calling<Any>>


    suspend fun getJobs(): Call<Calling<PaginationModel<JobsModel>>>
    suspend fun applyForJob(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun snapShotRequest(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun getPrayingTime(lat: Double, lng: Double): Call<Calling<PrayingTimeModel>>
    suspend fun getSpecialAdvertsList(
        currentPage: Int,
        queryMap: HashMap<String, Any>
    ): Call<Calling<PaginationModel<AdvertModel>>>

    suspend fun getWeatherInformation(latitude: Double, longitude: Double): Call<WeatherContainerModel>
    suspend fun getMyFavouritesAdvert(): Call<Calling<List<AdvertModel>>>
    suspend fun clearFavouritesAdverts(): Call<Calling<Any>>
    suspend fun getMyAdverts(currentPage: Int): Call<Calling<PaginationModel<AdvertModel>>>
    suspend fun editAdvertStepSection(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun getHighlightInfo(): Call<Calling<HighlightInfoModel>>
    suspend fun getHighlightPackages(advertPlace: String): Call<Calling<List<PackageModel>>>
    suspend fun payHighlightBackString(map: HashMap<String, Any>): Call<Calling<String>>
    suspend fun payHighlightBackObject(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun activeOrBlockAdverts(advertId: Int, status: String): Call<Calling<Any>>
    suspend fun showInactiveAdverts(): Call<Calling<List<AdvertModel>>>
    suspend fun activeAllInactiveAdverts(): Call<Calling<Any>>
    suspend fun deleteAdvert(advertId: Int): Call<Calling<Any>>
    suspend fun getMyOrdersList(currentPage: Int): Call<Calling<PaginationModel<OrdersModel>>>
    suspend fun deleteOrder(orderId: Int): Call<Calling<Any>>
    suspend fun getNotifications(): Call<Calling<List<NotificationsModel>>>
    suspend fun getUnreadNotifications(): Call<Calling<String>>
    suspend fun removeAllNotifications(): Call<Calling<Any>>
    suspend fun removeOneNotification(notifyId: String): Call<Calling<Any>>
    suspend fun getMyQuestions(currentPage: Int): Call<Calling<PaginationModel<AskHailModel>>>
    suspend fun deleteQuestion(askId: Int): Call<Calling<Any>>
    suspend fun editQuestion(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun editingQuestion(
        askId: Int,
        selectedCoverImage: String?,
        myTitle: String?,
        description: String?,
        showNameStatus: String?,
        deleteImage: Boolean
    ): Call<Calling<Any>>

    suspend fun getUserInfo(): Call<Calling<AccountInformationModel>>
    suspend fun editPersonalInfo(name: String, email: String, phone: String): Call<Calling<Any>>
    suspend fun changePassword(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun getMyCurrentPackage(): Call<Calling<MyPackageModel>>
    suspend fun reNewPackageBackString(map: HashMap<String, Any>): Call<Calling<String>>
    suspend fun renewPackageBackObject(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun updatePackageBackString(map: HashMap<String, Any>): Call<Calling<String>>
    suspend fun updatePackageBackObject(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun replayOnQuestionComment(commentId: Int, replay: String): Call<Calling<Any>>
    suspend fun replayOnAdvertComment(commentId: Int, replay: String): Call<Calling<Any>>
    suspend fun replayOnOrderComment(commentId: Int, replay: String): Call<Calling<Any>>
    fun getMessages(
        currentPage: Int,
        showingFilter: String
    ): Call<Calling<PaginationModel<MessagesModel>>>

    suspend fun getChatHistory(currentPage: Int, chatId: Int): Call<Calling<ChattingContentModel>>
    fun getApiToken(): String
    fun getFirebaseToken(): String
    suspend fun updateAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>,
        deletedImagesIds: MutableList<Int>
    ): Call<Calling<Any>>

    suspend fun updateAdvertStepFeature(map: LinkedHashMap<String, Any>): Call<Calling<Any>>
    suspend fun updateAdvertStepContact(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun editOrderStepSection(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun editOrderStepFeature(map: LinkedHashMap<String, Any>): Call<Calling<Any>>
    suspend fun editOrderStepContact(map: HashMap<String, Any>): Call<Calling<Any>>
    suspend fun getAdvertSpecialPackageInfo(advertId: Int): Call<Calling<MyPackageModel>>
    suspend fun getAdvertComments(
        advertId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>>

    suspend fun getOrderComments(
        orderId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>>

    suspend fun getQuestionsComments(
        questionId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>>

    suspend fun getAdvertRates(advertId: Int, currentPage: Int): Call<Calling<PaginationModel<RateModel>>>
    suspend fun createChat(type: String, relatedId: Int): Call<Calling<ChattingContentModel>>
    fun saveFireBaseToken(token: String)
    fun loginAsVisitor()
    fun isInVisitorMode(): Boolean
    fun getuserData(): UserModel?
    fun getUserLastLocation(): Location
    suspend fun logOut(): Call<Calling<Any>>

}