package com.wesal.domain.useCases

import android.location.Location
import android.util.Log
import com.wesal.domain.core.casesHandler.Results
import com.wesal.domain.core.casesHandler.ServicesTransform
import com.wesal.domain.core.casesHandler.WeatherServicesTransform
import com.wesal.domain.repositories.Repository
import com.wesal.domain.repositories.RepositoryImpl
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

class UseCaseImpl(
    private val repository: Repository = RepositoryImpl()
) : UseCase {
    //region AUTHORIZATION
    override fun getStartRoute(): EStartRoute {
        return repository.getStartRoute()
    }

    override fun setLandingCompleted() {
        repository.setLandingCompleted()
    }

    override fun clearUserData() {
        repository.clearUserData()
    }

    override fun loadCountries(): List<CountryModel>? {
        return repository.loadCountryList()
    }

    override suspend fun checkPhone(phone: String): Results<StepperModel?> {
        return ServicesTransform.transform(repository.checkPhone(phone))
    }

    override suspend fun resendCodeForRegister(advertiserId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.resendCodeForRegister(advertiserId))
    }

    override suspend fun confirmCodeForRegister(
        advertiserId: Int,
        code: String
    ): Results<StepperModel?> {
        return ServicesTransform.transform(repository.confirmCodeForRegister(advertiserId, code))
    }

    override suspend fun register(
        advertiserId: Int,
        name: String,
        email: String,
        password: String
    ): Results<UserModel?> {
        val data =
            ServicesTransform.transform(repository.register(advertiserId, name, email, password))
        when (data) {
            is Results.Success -> {
                repository.setUserData(data.value)
            }
            else -> {}
        }
        return data
    }

    override suspend fun checkPhoneForForgetPassword(value: String): Results<StepperModel?> {
        return ServicesTransform.transform(repository.checkPhoneForForgetPassword(value))
    }

    override suspend fun resendCodeForForgetPassword(advertiserId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.resendCodeForForgetPassword(advertiserId))
    }

    override suspend fun confirmCodeForForgetPassword(
        advertiserId: Int,
        code: String
    ): Results<StepperModel?> {
        return ServicesTransform.transform(
            repository.confirmCodeForForgetPassword(
                advertiserId,
                code
            )
        )
    }

    override suspend fun resetPassword(
        advertiserId: Int,
        password: String,
        rePassword: String
    ): Results<Any?> {
        return ServicesTransform.transform(
            repository.resetPassword(
                advertiserId,
                password,
                rePassword
            )
        )
    }

    override suspend fun login(phone: String, password: String): Results<UserModel?> {
        val data = ServicesTransform.transform(repository.login(phone, password))
        when (data) {
            is Results.Success -> {
                repository.setUserData(data.value)
            }
            is Results.Error -> {
               Log.i("error login" , "login error ")
            }
        }
        return data
    }

    override suspend fun getHomeData(): Results<HomeModel?> {
        return ServicesTransform.transform(repository.getHomeData())
    }

    override suspend fun toggleFavoritesAdvert(advId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.toggleFavoritesAdvert(advId))
    }

    override suspend fun getSubSectionList(sectionId: Int): Results<List<SectionModel>?> {
        return ServicesTransform.transform(repository.getSubSectionList(sectionId))
    }

    override suspend fun getAdvertsInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Results<PaginationModel<AdvertModel>?> {
        return ServicesTransform.transform(
            repository.getAdvertsInSubSection(
                sectionId,
                currentPage,
                filters
            )
        )
    }

    override suspend fun getAdvertDetails(advertId: Int): Results<AdvertContentModel?> {
        return ServicesTransform.transform(repository.getAdvertDetails(advertId))
    }

    override suspend fun addComment(advertId: Int, comment: String): Results<AddCommentModel?> {
        return ServicesTransform.transform(repository.addComment(advertId, comment))
    }

    override fun getCurrentUserId(): Int {
        return repository.getCurrentUserId()
    }

    override suspend fun deleteComment(commentId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.deleteComment(commentId))
    }

    override suspend fun addRate(advertId: Int, rate: String): Results<AddRateModel?> {
        return ServicesTransform.transform(repository.addRate(advertId, rate))
    }

    override suspend fun deleteRate(rateId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.deleteRate(rateId))
    }

    override suspend fun getAdvertOnMap(
        sectionId: Int,
        latitude: Double,
        longitude: Double
    ): Results<PaginationModel<AdvertModel>?> {
        return ServicesTransform.transform(
            repository.getAdvertOnMap(
                sectionId,
                latitude,
                longitude
            )
        )
    }

    override suspend fun getAdverterPage(
        adverterId: Int,
        currentPage: Int
    ): Results<PaginationModel<AdvertModel>?> {
        return ServicesTransform.transform(repository.getAdverterPage(adverterId, currentPage))
    }

    override suspend fun getAdvertTermsAndConditions(): Results<FixedPageModel?> {
        return ServicesTransform.transform(repository.getAdvertTermsAndConditions())
    }

    override suspend fun createAdvertStepCheckPage(): Results<AdvertStepperModel?> {
        return ServicesTransform.transform(repository.createAdvertStepCheckPage())
    }

    override suspend fun getPackagesList(): Results<List<PackageModel>?> {
        return ServicesTransform.transform(repository.getPackagesList())
    }

    override suspend fun loadBanks(): Results<List<BanksModel>?> {
        return ServicesTransform.transform(repository.loadBanks())
    }

    override suspend fun getBankInfo(bankAccountId: Int): Results<BanksDetailsModel?> {
        return ServicesTransform.transform(repository.getBankInfo(bankAccountId))
    }

    override suspend fun payPackageBackObject(map: HashMap<String, Any>): Results<AdvertStepperModel?> {
        return ServicesTransform.transform(repository.payPackageBackObject(map))
    }

    override suspend fun payPackageBackString(map: HashMap<String, Any>): Results<String?> {
        return ServicesTransform.transform(repository.payPackageBackString(map))
    }

    override fun getSystemLanguage(): String {
        return repository.getSystemLanguage()
    }

    override suspend fun getMainCategories(): Results<List<SectionModel>?> {
        return ServicesTransform.transform(repository.getMainCategories())
    }

    override suspend fun getSubMainCategories(sectionId: Int): Results<List<SectionModel>?> {
        return ServicesTransform.transform(repository.getSubMainCategories(sectionId))
    }

    override suspend fun createAdvertStepSection(map: HashMap<String, Any>): Results<AdvertStepperModel?> {
        return ServicesTransform.transform(repository.createAdvertStepSection(map))
    }

    override suspend fun createAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>
    ): Results<AdvertStepperModel?> {
        return ServicesTransform.transform(
            repository.createAdvertStepMedia(
                advertId,
                selectedCoverImage,
                list
            )
        )
    }

    override suspend fun updateAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>,
        deletedImagesIds: MutableList<Int>
    ): Results<Any?> {
        return ServicesTransform.transform(
            repository.updateAdvertStepMedia(
                advertId,
                selectedCoverImage,
                list,
                deletedImagesIds
            )
        )
    }

    override suspend fun checkDraftAdverts(): Results<AdvertStepperModel?> {
        return ServicesTransform.transform(repository.checkDraftAdverts())
    }

    override suspend fun makeNewAdvertId(): Results<AdvertStepperModel?> {
        return ServicesTransform.transform(repository.makeNewAdvertId())
    }

    override suspend fun getSidesList(): Results<List<SidesModel>?> {
        return ServicesTransform.transform(repository.getSidesList())
    }

    override suspend fun getBlocksList(): Results<List<BlocksModel>?> {
        return ServicesTransform.transform(repository.getBlocksList())
    }

    override suspend fun getAdvertSpecifications(advertId: Int): Results<List<AdvertFeaturesModel>?> {
        return ServicesTransform.transform(repository.getAdvertSpecifications(advertId))
    }

    override suspend fun createAdvertStepFeature(map: HashMap<String, Any>): Results<AdvertStepperModel?> {
        return ServicesTransform.transform(repository.createAdvertStepFeature(map))
    }

    override suspend fun createAdvertStepContact(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.createAdvertStepContact(map))
    }

    override fun updateUserLocation(latitude: Double, longitude: Double) {
        repository.updateUserLocation(latitude, longitude)
    }

    override suspend fun getOrdersInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Results<PaginationModel<OrdersModel>?> {
        return ServicesTransform.transform(
            repository.getOrdersInSubSection(
                sectionId,
                currentPage,
                filters
            )
        )
    }

    override suspend fun createOrderStepCheckPage(): Results<OrderStepperModel?> {
        return ServicesTransform.transform(repository.createOrderStepCheckPage())
    }

    override suspend fun makeNewOrderId(): Results<OrderStepperModel?> {
        return ServicesTransform.transform(repository.makeNewOrderId())
    }

    override suspend fun createOrderStepSection(map: HashMap<String, Any>): Results<OrderStepperModel?> {
        return ServicesTransform.transform(repository.createOrderStepSection(map))
    }

    override suspend fun getOrderSpecifications(orderId: Int): Results<List<AdvertFeaturesModel>?> {
        return ServicesTransform.transform(repository.getOrderSpecifications(orderId))
    }

    override suspend fun createOrderStepFeature(map: LinkedHashMap<String, Any>): Results<OrderStepperModel?> {
        return ServicesTransform.transform(repository.createOrderStepFeature(map))
    }

    override suspend fun createOrderStepContact(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.createOrderStepContact(map))
    }

    override suspend fun getOrderDetails(orderId: Int): Results<OrderContentModel?> {
        return ServicesTransform.transform(repository.getOrderDetails(orderId))
    }

    override suspend fun addCommentToOrder(
        orderId: Int,
        comment: String
    ): Results<AddCommentModel?> {
        return ServicesTransform.transform(repository.addCommentToOrder(orderId, comment))
    }

    override suspend fun deleteCommentFromOrder(commentId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.deleteCommentFromOrder(commentId))
    }

    override suspend fun getAskHailList(currentPage: Int): Results<PaginationModel<AskHailModel>?> {
        return ServicesTransform.transform(repository.getAskHailList(currentPage))
    }

    override suspend fun createAskHail(
        selectedImage: String?,
        title: String,
        desc: String,
        isPublic: Boolean
    ): Results<Any?> {
        return ServicesTransform.transform(
            repository.createAskHail(
                selectedImage,
                title,
                desc,
                isPublic
            )
        )
    }

    override suspend fun getAskDetails(askId: Int): Results<AskHailContentModel?> {
        return ServicesTransform.transform(repository.getAskDetails(askId))
    }

    override suspend fun addCommentToAsk(askId: Int, comment: String): Results<AddCommentModel?> {
        return ServicesTransform.transform(repository.addCommentToAsk(askId, comment))
    }

    override suspend fun deleteCommentFromAsk(commentId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.deleteCommentFromAsk(commentId))
    }

    override suspend fun getFixedPages(): Results<List<FixedPageModel>?> {
        val data = ServicesTransform.transform(repository.getFixedPages())
        when (data) {
            is Results.Success -> {
                repository.cacheFixedPages(data.value)
            }
            else -> {}
        }
        return data
    }

    override fun loadFixedPages(): List<FixedPageModel>? {
        return repository.loadFixedPages()
    }

    override suspend fun getFixedPageDetails(fixedPageSlug: String): Results<FixedPageModel?> {
        return ServicesTransform.transform(repository.getFixedPageDetails(fixedPageSlug))
    }

    override suspend fun getAboutApp(): Results<AboutAppModel?> {
        return ServicesTransform.transform(repository.getAboutApp())
    }

    override suspend fun sendContactUs(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.sendContactUs(map))
    }

    override suspend fun getServicesList(serviceType: String): Results<List<ServicesModel>?> {
        return ServicesTransform.transform(repository.getServicesList(serviceType))
    }

    override suspend fun getSettings(): Results<SettingsModel?> {
        return ServicesTransform.transform(repository.getSettings())
    }

    override fun changeLanguage(targetLanguage: String) {
        repository.changeLanguage(targetLanguage)
    }

    override suspend fun changeSettings(
        notification: String,
        comments: String,
        chat: String,
        questions: String,
        fav: String,
        language: String
    ): Results<Any?> {
        return ServicesTransform.transform(
            repository.changeSettings(
                notification, comments, chat, questions, fav, language
            )
        )
    }

    override suspend fun getJobs(): Results<PaginationModel<JobsModel>?> {
        return ServicesTransform.transform(repository.getJobs())
    }

    override suspend fun applyForJob(map: java.util.HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.applyForJob(map))
    }

    override suspend fun snapShotRequest(map: java.util.HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.snapShotRequest(map))
    }

    override suspend fun getPrayingTime(lat: Double, lng: Double): Results<PrayingTimeModel?> {
        return ServicesTransform.transform(repository.getPrayingTime(lat, lng))
    }

    override suspend fun getSpecialAdvertsList(
        currentPage: Int,
        queryMap: HashMap<String, Any>
    ): Results<PaginationModel<AdvertModel>?> {
        return ServicesTransform.transform(repository.getSpecialAdvertsList(currentPage, queryMap))
    }

    override suspend fun getWeatherInformation(
        latitude: Double,
        longitude: Double
    ): Results<WeatherContainerModel?> {
        return WeatherServicesTransform.transform(
            repository.getWeatherInformation(
                latitude,
                longitude
            )
        )
    }

    override suspend fun getMyFavouritesAdvert(): Results<List<AdvertModel>?> {
        return ServicesTransform.transform(repository.getMyFavouritesAdvert())
    }

    override suspend fun clearFavouritesAdverts(): Results<Any?> {
        return ServicesTransform.transform(repository.clearFavouritesAdverts())
    }

    override suspend fun getMyAdverts(currentPage: Int): Results<PaginationModel<AdvertModel>?> {
        return ServicesTransform.transform(repository.getMyAdverts(currentPage))
    }

    override suspend fun editAdvertStepSection(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.editAdvertStepSection(map))
    }

    override suspend fun getHighlightInfo(): Results<HighlightInfoModel?> {
        return ServicesTransform.transform(repository.getHighlightInfo())
    }

    override suspend fun getHighlightPackages(advertPlace: String): Results<List<PackageModel>?> {
        return ServicesTransform.transform(repository.getHighlightPackages(advertPlace))
    }

    override suspend fun payHighlightBackString(map: HashMap<String, Any>): Results<String?> {
        return ServicesTransform.transform(repository.payHighlightBackString(map))
    }

    override suspend fun payHighlightBackObject(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.payHighlightBackObject(map))
    }

    override suspend fun activeOrBlockAdverts(advertId: Int, status: String): Results<Any?> {
        return ServicesTransform.transform(repository.activeOrBlockAdverts(advertId, status))
    }

    override suspend fun showInactiveAdverts(): Results<List<AdvertModel>?> {
        return ServicesTransform.transform(repository.showInactiveAdverts())
    }

    override suspend fun activeAllInactiveAdverts(): Results<Any?> {
        return ServicesTransform.transform(repository.activeAllInactiveAdverts())
    }

    override suspend fun deleteAdvert(advertId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.deleteAdvert(advertId))
    }

    override suspend fun getMyOrdersList(currentPage: Int): Results<PaginationModel<OrdersModel>?> {
        return ServicesTransform.transform(repository.getMyOrdersList(currentPage))
    }

    override suspend fun deleteOrder(orderId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.deleteOrder(orderId))
    }

    override suspend fun getNotifications(): Results<List<NotificationsModel>?> {
        return ServicesTransform.transform(repository.getNotifications())
    }
    override suspend fun getUnreadNotifications(): Results<String?> {
        return ServicesTransform.transform(repository.getUnreadNotifications())
    }

    override suspend fun removeAllNotifications(): Results<Any?> {
        return ServicesTransform.transform(repository.removeAllNotifications())
    }

    override suspend fun removeOneNotification(notifyId: String): Results<Any?> {
        return ServicesTransform.transform(repository.removeOneNotification(notifyId))
    }

    override suspend fun getMyQuestions(currentPage: Int): Results<PaginationModel<AskHailModel>?> {
        return ServicesTransform.transform(repository.getMyQuestions(currentPage))
    }

    override suspend fun deleteQuestion(askId: Int): Results<Any?> {
        return ServicesTransform.transform(repository.deleteQuestion(askId))
    }

    override suspend fun editQuestion(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.editQuestion(map))
    }

    override suspend fun editingQuestion(
        askId: Int,
        selectedCoverImage: String?,
        myTitle: String?,
        description: String?,
        showNameStatus: String?,
        isDeleteImage: Boolean
    ): Results<Any?> {
        return ServicesTransform.transform(
            repository.editingQuestion(
                askId, selectedCoverImage, myTitle, description, showNameStatus, isDeleteImage
            )
        )
    }

    override suspend fun getUserInfo(): Results<AccountInformationModel?> {
        return ServicesTransform.transform(repository.getUserInfo())
    }

    override suspend fun editPersonalInfo(
        name: String,
        email: String,
        phone: String
    ): Results<Any?> {
        return ServicesTransform.transform(repository.editPersonalInfo(name, email, phone))
    }

    override suspend fun changePassword(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.changePassword(map))
    }

    override suspend fun getMyCurrentPackage(): Results<MyPackageModel?> {
        val data =  ServicesTransform.transform(repository.getMyCurrentPackage())
        Log.e("ASDASD","$data")
        return  data;
    }

    override suspend fun reNewPackageBackString(map: HashMap<String, Any>): Results<String?> {
        return ServicesTransform.transform(repository.reNewPackageBackString(map))
    }

    override suspend fun renewPackageBackObject(map: java.util.HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.renewPackageBackObject(map))
    }

    override suspend fun updatePackageBackString(map: HashMap<String, Any>): Results<String?> {
        return ServicesTransform.transform(repository.updatePackageBackString(map))
    }

    override suspend fun updatePackageBackObject(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.updatePackageBackObject(map))
    }

    override suspend fun replayOnQuestionComment(commentId: Int, replay: String): Results<Any?> {
        return ServicesTransform.transform(repository.replayOnQuestionComment(commentId, replay))
    }

    override suspend fun replayOnAdvertComment(commentId: Int, replay: String): Results<Any?> {
        return ServicesTransform.transform(repository.replayOnAdvertComment(commentId, replay))
    }

    override suspend fun replayOnOrderComment(commentId: Int, replay: String): Results<Any?> {
        return ServicesTransform.transform(repository.replayOnOrderComment(commentId, replay))
    }

    override suspend fun getMessages(
        currentPage: Int,
        showingFilter: String
    ): Results<PaginationModel<MessagesModel>?> {
        return ServicesTransform.transform(repository.getMessages(currentPage, showingFilter))
    }

    override suspend fun getChatHistory(
        currentPage: Int,
        chatId: Int
    ): Results<ChattingContentModel?> {
        return ServicesTransform.transform(repository.getChatHistory(currentPage, chatId))
    }

    override fun getApiToken(): String {
        return repository.getApiToken()
    }
    override fun getFirebaseToken(): String {
        return repository.getFirebaseToken()
    }

    override suspend fun updateAdvertStepFeature(map: LinkedHashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.updateAdvertStepFeature(map))
    }

    override suspend fun updateAdvertStepContact(map: java.util.HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.updateAdvertStepContact(map))
    }

    override suspend fun editOrderStepSection(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.editOrderStepSection(map))
    }

    override suspend fun editOrderStepFeature(map: LinkedHashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.editOrderStepFeature(map))
    }

    override suspend fun editOrderStepContact(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.editOrderStepContact(map))
    }

    override suspend fun getAdvertSpecialPackageInfo(advertId: Int): Results<MyPackageModel?> {
        return ServicesTransform.transform(repository.getAdvertSpecialPackageInfo(advertId))
    }

    override suspend fun getAdvertComments(
        advertId: Int,
        currentPage: Int
    ): Results<PaginationModel<CommentModel>?> {
        return ServicesTransform.transform(repository.getAdvertComments(advertId, currentPage))
    }

    override suspend fun getOrderComments(
        orderId: Int,
        currentPage: Int
    ): Results<PaginationModel<CommentModel>?> {
        return ServicesTransform.transform(repository.getOrderComments(orderId, currentPage))
    }

    override suspend fun getQuestionsComments(
        questionId: Int,
        currentPage: Int
    ): Results<PaginationModel<CommentModel>?> {
        return ServicesTransform.transform(repository.getQuestionsComments(questionId, currentPage))
    }

    override suspend fun getAdvertRates(
        advertId: Int,
        currentPage: Int
    ): Results<PaginationModel<RateModel>?> {
        return ServicesTransform.transform(repository.getAdvertRates(advertId, currentPage))
    }

    override suspend fun createChat(type: String, relatedId: Int): Results<ChattingContentModel?> {
        return ServicesTransform.transform(repository.createChat(type, relatedId))
    }

    override fun saveFireBaseToken(token: String) {
        repository.saveFireBaseToken(token)
    }

    override fun loginAsVisitor() {
        repository.loginAsVisitor()
    }

    override fun isInVisitorMode(): Boolean {
        return repository.isInVisitorMode()

    }

    override fun getUserData(): UserModel? {
        return repository.getuserData()
    }

    override fun getUserLastLocation(): Location {
        return repository.getUserLastLocation()
    }

    override suspend fun logOut(): Results<Any?> {
        return ServicesTransform.transform(repository.logOut())
    }

    suspend fun createAdvertStepSocial(map: java.util.HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.createAdvertStepSocial(map))
    }
    suspend fun editAdvertStepSocial(map: HashMap<String, Any>): Results<Any?> {
        return ServicesTransform.transform(repository.editAdvertStepSocial(map))
    }
}
