package com.wesal.domain.useCases

import android.location.Location
import com.wesal.domain.core.casesHandler.Results
import com.wesal.entities.base.PaginationModel
import com.wesal.entities.enums.EStartRoute
import com.wesal.entities.models.*
import java.util.LinkedHashMap

interface UseCase {
    //region AUTHORIZATION
    fun getStartRoute(): EStartRoute
    fun setLandingCompleted()
    fun clearUserData()
    fun loadCountries(): List<CountryModel>?
    suspend fun checkPhone(phone: String): Results<StepperModel?>
    suspend fun resendCodeForRegister(advertiserId: Int): Results<Any?>
    suspend fun confirmCodeForRegister(advertiserId: Int, code: String): Results<StepperModel?>
    suspend fun register(
        advertiserId: Int,
        name: String,
        email: String,
        password: String
    ): Results<UserModel?>

    suspend fun checkPhoneForForgetPassword(value: String): Results<StepperModel?>
    suspend fun resendCodeForForgetPassword(advertiserId: Int): Results<Any?>
    suspend fun confirmCodeForForgetPassword(
        advertiserId: Int,
        code: String
    ): Results<StepperModel?>

    suspend fun resetPassword(
        advertiserId: Int,
        password: String,
        rePassword: String
    ): Results<Any?>

    suspend fun login(phone: String, password: String): Results<UserModel?>
    suspend fun getHomeData(): Results<HomeModel?>
    suspend fun toggleFavoritesAdvert(advId: Int): Results<Any?>
    suspend fun getSubSectionList(sectionId: Int): Results<List<SectionModel>?>
    suspend fun getAdvertsInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Results<PaginationModel<AdvertModel>?>

    suspend fun getAdvertDetails(advertId: Int): Results<AdvertContentModel?>
    suspend fun addComment(advertId: Int, comment: String): Results<AddCommentModel?>
    fun getCurrentUserId(): Int
    suspend fun deleteComment(commentId: Int): Results<Any?>
    suspend fun addRate(advertId: Int, rate: String): Results<AddRateModel?>
    suspend fun deleteRate(rateId: Int): Results<Any?>
    suspend fun getAdvertOnMap(
        sectionId: Int,
        latitude: Double,
        longitude: Double
    ): Results<PaginationModel<AdvertModel>?>

    suspend fun getAdverterPage(
        adverterId: Int,
        currentPage: Int
    ): Results<PaginationModel<AdvertModel>?>

    suspend fun getAdvertTermsAndConditions(): Results<FixedPageModel?>
    suspend fun createAdvertStepCheckPage(): Results<AdvertStepperModel?>
    suspend fun getPackagesList(): Results<List<PackageModel>?>
    suspend fun loadBanks(): Results<List<BanksModel>?>
    suspend fun getBankInfo(bankAccountId: Int): Results<BanksDetailsModel?>
    suspend fun payPackageBackObject(map: HashMap<String, Any>): Results<AdvertStepperModel?>
    suspend fun payPackageBackString(map: HashMap<String, Any>): Results<String?>
    fun getSystemLanguage(): String
    suspend fun getMainCategories(): Results<List<SectionModel>?>
    suspend fun getSubMainCategories(sectionId: Int): Results<List<SectionModel>?>
    suspend fun createAdvertStepSection(map: HashMap<String, Any>): Results<AdvertStepperModel?>
    suspend fun createAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>
    ): Results<AdvertStepperModel?>
    suspend fun updateAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>,
        deletedImagesIds: MutableList<Int>
    ): Results<Any?>
    suspend fun checkDraftAdverts(): Results<AdvertStepperModel?>
    suspend fun makeNewAdvertId(): Results<AdvertStepperModel?>
    suspend fun getSidesList(): Results<List<SidesModel>?>
    suspend fun getBlocksList(): Results<List<BlocksModel>?>
    suspend fun getAdvertSpecifications(advertId: Int): Results<List<AdvertFeaturesModel>?>
    suspend fun createAdvertStepFeature(map: HashMap<String, Any>): Results<AdvertStepperModel?>
    suspend fun createAdvertStepContact(map: HashMap<String, Any>): Results<Any?>
    fun updateUserLocation(latitude: Double, longitude: Double)
    suspend fun getOrdersInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Results<PaginationModel<OrdersModel>?>

    suspend fun createOrderStepCheckPage(): Results<OrderStepperModel?>
    suspend fun makeNewOrderId(): Results<OrderStepperModel?>
    suspend fun createOrderStepSection(map: java.util.HashMap<String, Any>): Results<OrderStepperModel?>
    suspend fun getOrderSpecifications(orderId: Int): Results<List<AdvertFeaturesModel>?>
    suspend fun createOrderStepFeature(map: LinkedHashMap<String, Any>): Results<OrderStepperModel?>
    suspend fun createOrderStepContact(map: HashMap<String, Any>): Results<Any?>
    suspend fun getOrderDetails(orderId: Int):  Results<OrderContentModel?>
    suspend fun addCommentToOrder(orderId: Int, comment: String): Results<AddCommentModel?>
    suspend fun deleteCommentFromOrder(commentId: Int):  Results<Any?>
    suspend fun getAskHailList(currentPage: Int): Results<PaginationModel<AskHailModel>?>
    suspend fun createAskHail(selectedImage: String?, title: String, desc: String, isPublic: Boolean): Results<Any?>
    suspend fun getAskDetails(askId: Int): Results<AskHailContentModel?>
    suspend fun addCommentToAsk(askId: Int, comment: String): Results<AddCommentModel?>
    suspend fun deleteCommentFromAsk(commentId: Int): Results<Any?>
    suspend fun getFixedPages():  Results<List<FixedPageModel>?>
    fun loadFixedPages() :List<FixedPageModel>?
    suspend fun getFixedPageDetails(fixedPageSlug: String):  Results<FixedPageModel?>
    suspend fun getAboutApp(): Results<AboutAppModel?>
    suspend fun sendContactUs(map: HashMap<String, Any>): Results<Any?>
    suspend fun getServicesList(serviceType: String):  Results<List<ServicesModel>?>
    suspend fun getSettings(): Results<SettingsModel?>
    fun changeLanguage(targetLanguage: String)
    suspend fun changeSettings(
        notification: String,
        comments: String,
        chat: String,
        questions: String,
        fav: String,
        language: String
    ): Results<Any?>

    suspend fun getJobs(): Results<PaginationModel<JobsModel>?>
    suspend fun applyForJob(map: java.util.HashMap<String, Any>): Results<Any?>
    suspend fun snapShotRequest(map: java.util.HashMap<String, Any>): Results<Any?>
    suspend fun getPrayingTime(lat:Double,lng:Double): Results<PrayingTimeModel?>
    suspend fun getSpecialAdvertsList(currentPage: Int, queryMap:HashMap<String,Any>): Results<PaginationModel<AdvertModel>?>
    suspend fun getWeatherInformation(latitude: Double, longitude: Double): Results<WeatherContainerModel?>
    suspend fun getMyFavouritesAdvert(): Results<List<AdvertModel>?>
    suspend fun clearFavouritesAdverts(): Results<Any?>
    suspend fun getMyAdverts(currentPage: Int):  Results<PaginationModel<AdvertModel>?>
    suspend fun editAdvertStepSection(map: HashMap<String, Any>): Results<Any?>
    suspend fun getHighlightInfo(): Results<HighlightInfoModel?>
    suspend fun getHighlightPackages(advertPlace: String): Results<List<PackageModel>?>
    suspend fun payHighlightBackString(map: HashMap<String, Any>): Results<String?>
    suspend fun payHighlightBackObject(map: HashMap<String, Any>):  Results<Any?>
    suspend fun activeOrBlockAdverts(advertId: Int, status : String):  Results<Any?>
    suspend fun showInactiveAdverts(): Results<List<AdvertModel>?>
    suspend fun activeAllInactiveAdverts(): Results<Any?>
    suspend fun deleteAdvert(advertId: Int): Results<Any?>
    suspend fun getMyOrdersList(currentPage: Int): Results<PaginationModel<OrdersModel>?>
    suspend fun deleteOrder(orderId: Int): Results<Any?>
    suspend fun getNotifications(): Results<List<NotificationsModel>?>
    suspend fun getUnreadNotifications(): Results<String?>
    suspend fun removeAllNotifications(): Results<Any?>
    suspend fun removeOneNotification(notifyId: String): Results<Any?>
    suspend fun getMyQuestions(currentPage: Int):  Results<PaginationModel<AskHailModel>?>
    suspend fun deleteQuestion(askId: Int):  Results<Any?>
    suspend fun editQuestion(map: HashMap<String, Any>): Results<Any?>
    suspend fun editingQuestion(
        askId: Int,
        selectedCoverImage: String?,
        myTitle: String?,
        description: String?,
        showNameStatus: String?,
        isDeleteImage:Boolean
    ): Results<Any?>

    suspend fun getUserInfo(): Results<AccountInformationModel?>
    suspend fun editPersonalInfo(name: String, email: String, phone:  String): Results<Any?>
    suspend fun changePassword(map: java.util.HashMap<String, Any>): Results<Any?>
    suspend fun getMyCurrentPackage(): Results<MyPackageModel?>
    suspend fun reNewPackageBackString(map: HashMap<String, Any>): Results<String?>
    suspend fun renewPackageBackObject(map: HashMap<String, Any>): Results<Any?>
    suspend fun updatePackageBackString(map: HashMap<String, Any>): Results<String?>
    suspend fun updatePackageBackObject(map: HashMap<String, Any>): Results<Any?>
    suspend fun replayOnQuestionComment(commentId: Int, replay: String): Results<Any?>
    suspend fun replayOnAdvertComment(commentId: Int, replay: String): Results<Any?>
    suspend fun replayOnOrderComment(commentId: Int, replay: String): Results<Any?>
    suspend fun getMessages(currentPage: Int, showingFilter: String): Results<PaginationModel<MessagesModel>?>
    suspend fun getChatHistory(currentPage: Int, chatId: Int):  Results<ChattingContentModel?>
    fun getApiToken(): String
    fun getFirebaseToken(): String

    suspend fun updateAdvertStepFeature(map: LinkedHashMap<String, Any>): Results<Any?>
    suspend fun updateAdvertStepContact(map: HashMap<String, Any>): Results<Any?>
    suspend fun editOrderStepSection(map: HashMap<String, Any>):  Results<Any?>
    suspend fun editOrderStepFeature(map: LinkedHashMap<String, Any>):  Results<Any?>
    suspend fun editOrderStepContact(map: java.util.HashMap<String, Any>): Results<Any?>
    suspend fun getAdvertSpecialPackageInfo(advertId: Int): Results<MyPackageModel?>
    suspend fun getAdvertComments(advertId: Int, currentPage: Int): Results<PaginationModel<CommentModel>?>
    suspend fun getOrderComments(orderId: Int, currentPage: Int): Results<PaginationModel<CommentModel>?>
    suspend fun getQuestionsComments(questionId: Int, currentPage: Int): Results<PaginationModel<CommentModel>?>
    suspend fun getAdvertRates(advertId: Int, currentPage: Int): Results<PaginationModel<RateModel>?>
    suspend fun createChat(type : String, relatedId: Int): Results<ChattingContentModel?>
    fun saveFireBaseToken(token: String)
    fun loginAsVisitor()
    fun isInVisitorMode() :Boolean
    fun getUserData():UserModel?
    fun getUserLastLocation():Location
    suspend fun logOut() :Results<Any?>


}