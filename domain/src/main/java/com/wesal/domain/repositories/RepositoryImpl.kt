package com.wesal.domain.repositories

import android.location.Location
import com.wesal.domain.core.generateRequestBody
import com.wesal.domain.core.generateRequestBodyForImage
import com.wesal.domain.gateways.cacheGateway.ICacheGateWay
import com.wesal.domain.gateways.cacheGateway.cacheGateway
import com.wesal.domain.gateways.preferencesGateways.PrefMangerImplementer
import com.wesal.domain.gateways.preferencesGateways.preferencesGateway
import com.wesal.domain.gateways.serverGateways.SystemApi
import com.wesal.domain.gateways.serverGateways.systemApi
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
import okhttp3.RequestBody
import retrofit2.Call
import timber.log.Timber
import java.io.File

class RepositoryImpl(
    private val cache: ICacheGateWay = cacheGateway,
    private val prefManger: PrefMangerImplementer = preferencesGateway,
    private val server: SystemApi = systemApi
) : Repository {
    override fun getStartRoute(): EStartRoute {
        val landingStatus = prefManger.getLandingStatus()
        val userLogged = prefManger.getUserLogged()
        val isVisitor = prefManger.isInVisitorMode()
        if (isVisitor){
            return EStartRoute.VISITOR_ROUTE
        }else{
            if (userLogged){
                return EStartRoute.USER_ROUTE
            }else{
                return EStartRoute.LOGIN_ROUTE
            }
        }

    }

    override fun setLandingCompleted() {
        prefManger.setLandingStatus(true)
    }

    override fun clearUserData() {
        prefManger.setUserData(null)
    }

    override fun saveCountryList(value: List<CountryModel>?) {
        cache.saveCounties(value)
    }

    override fun loadCountryList(): List<CountryModel>? {
        return cache.loadCountryList()
    }

    override suspend fun checkPhone(phone: String): Call<Calling<StepperModel>> {
        return server.checkPhone(phone)
    }

    override fun setUserData(value: UserModel?) {
        prefManger.setUserData(value)
    }

    override suspend fun resendCodeForRegister(advertiserId: Int): Call<Calling<Any>> {
        return server.resendCodeForRegister(advertiserId)
    }

    override suspend fun confirmCodeForRegister(
        advertiserId: Int,
        code: String
    ): Call<Calling<StepperModel>> {
        return server.confirmCodeForRegister(advertiserId, code)
    }

    override suspend fun register(
        advertiserId: Int,
        name: String,
        email: String,
        password: String
    ): Call<Calling<UserModel>> {
        val map = hashMapOf<String, Any>()
        map["advertiser_id"] = advertiserId
        map["name"] = name
        map["email"] = email
        map["password"] = password
        map["password_confirmation"] = password
        map["firebase_token"] = prefManger.getFireBaseToken()
        return server.register(map)
    }

    override suspend fun checkPhoneForForgetPassword(value: String): Call<Calling<StepperModel>> {
        return server.checkPhoneForForgetPassword(value)
    }

    override suspend fun resendCodeForForgetPassword(advertiserId: Int): Call<Calling<Any>> {
        return server.resendCodeForForgetPassword(advertiserId)
    }

    override suspend fun confirmCodeForForgetPassword(
        advertiserId: Int,
        code: String
    ): Call<Calling<StepperModel>> {
        return server.confirmCodeForForgetPassword(advertiserId, code)
    }

    override suspend fun resetPassword(
        advertiserId: Int,
        password: String,
        rePassword: String
    ): Call<Calling<Any>> {
        return server.resetPassword(advertiserId, password, rePassword)
    }

    override suspend fun login(phone: String, password: String): Call<Calling<UserModel>> {
        val map = hashMapOf<String, Any>()
        map["mobile"] = phone
        map["password"] = password
        map["firebase_token"] = prefManger.getFireBaseToken()
        return server.login(map)
    }

    override suspend fun getHomeData(): Call<Calling<HomeModel>> {
        return server.getHomeData()
    }

    override suspend fun toggleFavoritesAdvert(advId: Int): Call<Calling<Any>> {
        return server.toggleFavoritesAdvert(advId)
    }

    override suspend fun getSubSectionList(sectionId: Int): Call<Calling<List<SectionModel>>> {
        return server.getSubSectionList(sectionId)
    }

    override suspend fun getAdvertsInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Call<Calling<PaginationModel<AdvertModel>>> {
        return server.getAdvertsInSubSection(sectionId, currentPage, filters)
    }

    override suspend fun getAdvertDetails(advertId: Int): Call<Calling<AdvertContentModel>> {
        return server.getAdvertDetails(advertId)
    }

    override suspend fun addComment(
        advertId: Int,
        comment: String
    ): Call<Calling<AddCommentModel>> {
        val map = hashMapOf<String, Any>()
        map["advertisement_id"] = advertId
        map["comment"] = comment

        return server.addComment(map)
    }

    override fun getCurrentUserId(): Int {
        return prefManger.loadUserData()?.advertiserId ?: -1
    }

    override suspend fun deleteComment(commentId: Int): Call<Calling<Any>> {
        val map = hashMapOf<String, Any>()
        map["comment_id"] = commentId
        return server.deleteComment(map)
    }

    override suspend fun addRate(advertId: Int, rate: String): Call<Calling<AddRateModel>> {
        val map = hashMapOf<String, Any>()
        map["advertisement_id"] = advertId
        map["rate"] = rate

        return server.addRate(map)
    }

    override suspend fun deleteRate(rateId: Int): Call<Calling<Any>> {
        val map = hashMapOf<String, Any>()
        map["rate_id"] = rateId
        return server.deleteRate(map)
    }

    override suspend fun getAdvertOnMap(
        sectionId: Int,
        latitude: Double,
        longitude: Double
    ): Call<Calling<PaginationModel<AdvertModel>>> {
        return server.getAdvertOnMap(sectionId, latitude, longitude)
    }

    override suspend fun getAdverterPage(
        adverterId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<AdvertModel>>> {
        return server.getAdverterPage(adverterId, currentPage)
    }

    override suspend fun getAdvertTermsAndConditions(): Call<Calling<FixedPageModel>> {
        return server.getAdvertTermsAndConditions()
    }

    override suspend fun createAdvertStepCheckPage(): Call<Calling<AdvertStepperModel>> {
        return server.createAdvertStepCheckPage()
    }

    override suspend fun getPackagesList(): Call<Calling<List<PackageModel>>> {
        return server.getPackagesList()
    }

    override suspend fun loadBanks(): Call<Calling<List<BanksModel>>> {
        return server.loadBanks()
    }

    override suspend fun getBankInfo(bankAccountId: Int): Call<Calling<BanksDetailsModel>> {
        return server.getBankInfo(bankAccountId)
    }

    override suspend fun payPackageBackObject(map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>> {
        return server.payPackageBackObject(map)
    }

    override suspend fun payPackageBackString(map: HashMap<String, Any>): Call<Calling<String>> {
        return server.payPackageBackString(map)
    }

    override fun getSystemLanguage(): String {
        return prefManger.getSystemLanguage()
    }

    override suspend fun getMainCategories(): Call<Calling<List<SectionModel>>> {
        return server.getMainCategories()
    }

    override suspend fun getSubMainCategories(sectionId: Int): Call<Calling<List<SectionModel>>> {
        return server.getSubMainCategories(sectionId)
    }

    override suspend fun createAdvertStepSection(map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>> {
        return server.createAdvertStepSection(map)
    }

    override suspend fun createAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>
    ): Call<Calling<AdvertStepperModel>> {

        val map: LinkedHashMap<String, RequestBody> = linkedMapOf()
        map["advertisement_id"] = advertId.toString().generateRequestBody()
        selectedCoverImage?.let {
            map["promotional_image\"; filename=\"photo_1.jpg\" "] =
                File(it).generateRequestBodyForImage()
        }
        list.filterNotNull().forEachIndexed { index, advMediaModel ->
            map["media[$index]['image']\"; filename=\"media_1.jpg\""] =
                File(advMediaModel.mediaImage).generateRequestBodyForImage()
            Timber.e("CodeBlanca ==> Image index $index With Url ${advMediaModel.mediaImage}")
            if (!advMediaModel.mediaVideo.isNullOrEmpty()) {
                map["media[$index]['video']\"; filename=\"vedio_1.mp4\""] =
                    File(advMediaModel.mediaVideo).generateRequestBodyForImage()

                map["media[$index]['video_duration']"] =
                    advMediaModel.videoDuration.toString().generateRequestBody()
                Timber.e("CodeBlanca ==> video index $index With Url ${advMediaModel.mediaVideo}")
            }
        }
        return server.createAdvertStepMedia(map)

    }

    override suspend fun updateAdvertStepMedia(
        advertId: Int,
        selectedCoverImage: String?,
        list: MutableList<AdvMediaModel?>,
        deletedImagesIds: MutableList<Int>
    ): Call<Calling<Any>> {
        val map: LinkedHashMap<String, RequestBody> = linkedMapOf()
        map["advertisement_id"] = advertId.toString().generateRequestBody()
        selectedCoverImage?.let {
            if (!it.startsWith("http"))
                map["promotional_image\"; filename=\"photo_1.jpg\" "] =
                    File(it).generateRequestBodyForImage()

        }
        list.filterNotNull().forEachIndexed { index, advMediaModel ->
            if (!advMediaModel.mediaImage.startsWith("http")) {
                map["added_media[$index]['image']\"; filename=\"media_1.jpg\""] =
                    File(advMediaModel.mediaImage).generateRequestBodyForImage()
                Timber.e("CodeBlanca ==> Image index $index With Url ${advMediaModel.mediaImage}")
                if (!advMediaModel.mediaVideo.isNullOrEmpty()) {
                    map["added_media[$index]['video']\"; filename=\"vedio_1.mp4\""] =
                        File(advMediaModel.mediaVideo).generateRequestBodyForImage()

                    map["added_media[$index]['video_duration']"] =
                        advMediaModel.videoDuration.toString().generateRequestBody()
                    Timber.e("CodeBlanca ==> video index $index With Url ${advMediaModel.mediaVideo}")
                }
            }
        }
        deletedImagesIds.forEachIndexed { index, i ->
            map["deleted_media_ids[$index]"] = i.toString().generateRequestBody()

        }
        if (selectedCoverImage==null){
            map["promotional_image_delete"] = "yes".toString().generateRequestBody()
        }else{
            map["promotional_image_delete"] = "no".toString().generateRequestBody()
        }
        return server.updateAdvertStepMedia(map)
    }

    override suspend fun checkDraftAdverts(): Call<Calling<AdvertStepperModel>> {
        return server.checkDraftAdverts()
    }

    override suspend fun makeNewAdvertId(): Call<Calling<AdvertStepperModel>> {
        val map = hashMapOf<String, Any>()
        map["desire"] = "new"
        return server.makeNewAdvertId(map)
    }

    override suspend fun getSidesList(): Call<Calling<List<SidesModel>>> {
        return server.getSidesList()
    }

    override suspend fun getBlocksList(): Call<Calling<List<BlocksModel>>> {
        return server.getBlocksList()
    }

    override suspend fun getAdvertSpecifications(advertId: Int): Call<Calling<List<AdvertFeaturesModel>>> {
        return server.getAdvertSpecifications(advertId)
    }

    override suspend fun createAdvertStepFeature(map: HashMap<String, Any>): Call<Calling<AdvertStepperModel>> {
        return server.createAdvertStepFeature(map)
    }

    override suspend fun createAdvertStepContact(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.createAdvertStepContact(map)
    }

    override fun updateUserLocation(latitude: Double, longitude: Double) {
        prefManger.saveUserLocation(latitude, longitude)
    }

    override suspend fun getOrdersInSubSection(
        sectionId: Int,
        currentPage: Int,
        filters: HashMap<String, Any>
    ): Call<Calling<PaginationModel<OrdersModel>>> {
        return server.getOrdersInSubSection(sectionId, currentPage, filters)
    }

    override suspend fun createOrderStepCheckPage(): Call<Calling<OrderStepperModel>> {
        return server.createOrderStepCheckPage()
    }

    override suspend fun makeNewOrderId(): Call<Calling<OrderStepperModel>> {
        val map = hashMapOf<String, Any>()
        map["desire"] = "new"
        return server.makeNewOrderId(map)
    }

    override suspend fun createOrderStepSection(map: HashMap<String, Any>): Call<Calling<OrderStepperModel>> {
        return server.createOrderStepSection(map)
    }

    override suspend fun getOrderSpecifications(orderId: Int): Call<Calling<List<AdvertFeaturesModel>>> {
        return server.getOrderSpecifications(orderId)
    }

    override suspend fun createOrderStepFeature(map: java.util.LinkedHashMap<String, Any>): Call<Calling<OrderStepperModel>> {
        return server.createOrderStepFeature(map)
    }

    override suspend fun createOrderStepContact(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.createOrderStepContact(map)
    }

    override suspend fun getOrderDetails(orderId: Int): Call<Calling<OrderContentModel>> {
        return server.getOrderDetails(orderId)
    }

    override suspend fun addCommentToOrder(
        orderId: Int,
        comment: String
    ): Call<Calling<AddCommentModel>> {
        return server.addCommentToOrder(orderId, comment)
    }

    override suspend fun deleteCommentFromOrder(commentId: Int): Call<Calling<Any>> {
        return server.deleteCommentFromOrder(commentId)
    }

    override suspend fun getAskHailList(currentPage: Int): Call<Calling<PaginationModel<AskHailModel>>> {
        return server.getAskHailList(currentPage)
    }

    override suspend fun createAskHail(
        selectedImage: String?,
        title: String,
        desc: String,
        public: Boolean
    ): Call<Calling<Any>> {
        val map: LinkedHashMap<String, RequestBody> = linkedMapOf()
        map["title"] = title.generateRequestBody()
        map["description"] = desc.generateRequestBody()
        selectedImage?.let {
            map["image\"; filename=\"photo_1.jpg\" "] =
                File(it).generateRequestBodyForImage()
        }
        map["show_name_status"] = (if (public) "active" else "block").generateRequestBody()

        return server.createAskHail(map)
    }

    override suspend fun getAskDetails(askId: Int): Call<Calling<AskHailContentModel>> {
        return server.getAskDetails(askId)
    }

    override suspend fun addCommentToAsk(
        askId: Int,
        comment: String
    ): Call<Calling<AddCommentModel>> {
        return server.addCommentToAsk(askId, comment)
    }

    override suspend fun deleteCommentFromAsk(commentId: Int): Call<Calling<Any>> {
        return server.deleteCommentFromAsk(commentId)
    }

    override suspend fun getFixedPages(): Call<Calling<List<FixedPageModel>>> {
        return server.getFixedPages()
    }

    override fun cacheFixedPages(value: List<FixedPageModel>?) {
        cache.saveFixedPages(value)
    }

    override fun loadFixedPages(): List<FixedPageModel>? {
        return cache.loadFixedPages()
    }

    override suspend fun getFixedPageDetails(fixedPageSlug: String): Call<Calling<FixedPageModel>> {
        return server.getFixedPageDetails(fixedPageSlug)
    }

    override suspend fun getAboutApp(): Call<Calling<AboutAppModel>> {
        return server.getAboutApp()
    }

    override suspend fun sendContactUs(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.sendContactUs(map)
    }

    override suspend fun getServicesList(serviceType: String): Call<Calling<List<ServicesModel>>> {
        return server.getServicesList(serviceType)
    }

    override suspend fun getSettings(): Call<Calling<SettingsModel>> {
        return server.getSettings()
    }

    override fun changeLanguage(targetLanguage: String) {
        prefManger.setSystemLanguage(targetLanguage)
    }

    override suspend fun changeSettings(
        notification: String,
        comments: String,
        chat: String,
        questions: String,
        fav: String,
        language: String
    ): Call<Calling<Any>> {
        val map = hashMapOf<String, Any>()
        map["all_notifications_status"] = notification
        map["new_comments_status"] = comments
        map["chat_status"] = chat
        map["question_replies_status"] = questions
        map["favorite_status"] = fav
        map["language_used"] = language
        return server.changeSettings(map)

    }
    override suspend fun createAdvertStepSocial(map: java.util.HashMap<String, Any>): Call<Calling<Any>> {
        return server.createAdvertStepSocial(map)
    }

    override suspend fun editAdvertStepSocial(map: java.util.HashMap<String, Any>): Call<Calling<Any>> {
        return server.editAdvertStepSocial(map)
    }
    override suspend fun getJobs(): Call<Calling<PaginationModel<JobsModel>>> {
        return server.getJobs()
    }

    override suspend fun applyForJob(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.applyForJob(map)
    }

    override suspend fun snapShotRequest(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.snapShotRequest(map)
    }

    override suspend fun getPrayingTime(lat: Double, lng: Double): Call<Calling<PrayingTimeModel>> {
        val tsLong = System.currentTimeMillis() / 1000
        val currentTime = tsLong.toString()
        val url = "https://api.aladhan.com/v1/timings/$currentTime"
        return server.getPrayingTime(url, lat, lng, 4)
    }

    override suspend fun getWeatherInformation(
        latitude: Double,
        longitude: Double
    ): Call<WeatherContainerModel> {
        val url =
            "https://api.openweathermap.org/data/2.5/onecall?appid=0b547e03fd739c2a9a45508500b97204&lat=$latitude&lon=$longitude&lang=ar&exclude=current,minutely,hourly&units=metric"
        return server.getWeatherInformation(url)
    }

    override suspend fun getSpecialAdvertsList(
        currentPage: Int,
        queryMap: HashMap<String, Any>
    ): Call<Calling<PaginationModel<AdvertModel>>> {
        return server.getSpecialAdvertsList(currentPage, queryMap)
    }

    override suspend fun getMyFavouritesAdvert(): Call<Calling<List<AdvertModel>>> {
        return server.getMyFavouritesAdvert()
    }

    override suspend fun clearFavouritesAdverts(): Call<Calling<Any>> {
        return server.clearFavouritesAdverts()
    }

    override suspend fun getMyAdverts(currentPage: Int): Call<Calling<PaginationModel<AdvertModel>>> {
        return server.getMyAdverts(currentPage)
    }

    override suspend fun editAdvertStepSection(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.editAdvertStepSection(map)
    }

    override suspend fun getHighlightInfo(): Call<Calling<HighlightInfoModel>> {
        return server.getHighlightInfo()
    }

    override suspend fun getHighlightPackages(advertPlace: String): Call<Calling<List<PackageModel>>> {
        return server.getHighlightPackages(advertPlace)
    }

    override suspend fun payHighlightBackString(map: HashMap<String, Any>): Call<Calling<String>> {
        return server.payHighlightBackString(map)
    }

    override suspend fun payHighlightBackObject(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.payHighlightBackObject(map)
    }

    override suspend fun activeOrBlockAdverts(advertId: Int, status: String): Call<Calling<Any>> {
        return server.activeOrBlockAdverts(advertId, status)
    }

    override suspend fun showInactiveAdverts(): Call<Calling<List<AdvertModel>>> {
        return server.showInactiveAdverts()
    }

    override suspend fun activeAllInactiveAdverts(): Call<Calling<Any>> {
        return server.activeAllInactiveAdverts()
    }

    override suspend fun deleteAdvert(advertId: Int): Call<Calling<Any>> {
        return server.deleteAdvert(advertId)
    }

    override suspend fun getMyOrdersList(currentPage: Int): Call<Calling<PaginationModel<OrdersModel>>> {
        return server.getMyOrdersList(currentPage)
    }

    override suspend fun deleteOrder(orderId: Int): Call<Calling<Any>> {
        return server.deleteOrder(orderId)
    }

    override suspend fun getNotifications(): Call<Calling<List<NotificationsModel>>> {
        return server.getNotifications()
    }

    override suspend fun getUnreadNotifications(): Call<Calling<String>> {
        return server.getUnreadNotifications()
    }

    override suspend fun removeAllNotifications(): Call<Calling<Any>> {
        return server.removeAllNotifications()
    }

    override suspend fun removeOneNotification(notifyId: String): Call<Calling<Any>> {
        return server.removeOneNotification(notifyId)
    }

    override suspend fun getMyQuestions(currentPage: Int): Call<Calling<PaginationModel<AskHailModel>>> {
        return server.getMyQuestions(currentPage)
    }

    override suspend fun deleteQuestion(askId: Int): Call<Calling<Any>> {
        return server.deleteQuestion(askId)
    }

    override suspend fun editQuestion(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.editQuestion(map)
    }

    override suspend fun editingQuestion(
        askId: Int,
        selectedCoverImage: String?,
        myTitle: String?,
        description: String?,
        showNameStatus: String?,
        deleteImage: Boolean
    ): Call<Calling<Any>> {
        val map: LinkedHashMap<String, RequestBody> = linkedMapOf()
        map["question_id"] = askId.toString().generateRequestBody()
        selectedCoverImage?.let {
            if (!it.startsWith("http")) {
                if (!it.isNullOrEmpty()) {
                    map["image\"; filename=\"photo_1.jpg\" "] =
                        File(it).generateRequestBodyForImage()
                }
            }

        }
        myTitle?.let {
            map["title"] = it.generateRequestBody()
        }
        description?.let {
            map["description"] = it.generateRequestBody()
        }
        showNameStatus?.let {
            map["show_name_status"] = it.generateRequestBody()
        }
        map["delete_image"] = deleteImage.toString().generateRequestBody()

        return server.editingQuestion(map)
    }

    override suspend fun getUserInfo(): Call<Calling<AccountInformationModel>> {
        return server.getUserInfo()
    }

    override suspend fun editPersonalInfo(
        name: String,
        email: String,
        phone: String
    ): Call<Calling<Any>> {
        return server.editPersonalInfo(name, email, phone)
    }

    override suspend fun changePassword(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.changePassword(map)
    }

    override suspend fun getMyCurrentPackage(): Call<Calling<MyPackageModel>> {
        return server.getMyCurrentPackage()
    }

    override suspend fun reNewPackageBackString(map: HashMap<String, Any>): Call<Calling<String>> {
        return server.reNewPackageBackString(map)
    }

    override suspend fun renewPackageBackObject(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.renewPackageBackObject(map)
    }

    override suspend fun updatePackageBackString(map: HashMap<String, Any>): Call<Calling<String>> {
        return server.updatePackageBackString(map)
    }

    override suspend fun updatePackageBackObject(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.updatePackageBackObject(map)
    }

    override suspend fun replayOnQuestionComment(
        commentId: Int,
        replay: String
    ): Call<Calling<Any>> {
        val map = hashMapOf<String, Any>()
        map["comment_id"] = commentId
        map["reply"] = replay
        return server.replayOnQuestionComment(map)
    }

    override suspend fun replayOnAdvertComment(commentId: Int, replay: String): Call<Calling<Any>> {
        val map = hashMapOf<String, Any>()
        map["comment_id"] = commentId
        map["reply"] = replay
        return server.replayOnAdvertComment(map)
    }

    override suspend fun replayOnOrderComment(commentId: Int, replay: String): Call<Calling<Any>> {
        val map = hashMapOf<String, Any>()
        map["comment_id"] = commentId
        map["reply"] = replay
        return server.replayOnOrderComment(map)
    }

    override fun getMessages(
        currentPage: Int,
        showingFilter: String
    ): Call<Calling<PaginationModel<MessagesModel>>> {
        return server.getMessages(currentPage, showingFilter)
    }

    override suspend fun getChatHistory(
        currentPage: Int,
        chatId: Int
    ): Call<Calling<ChattingContentModel>> {
        return server.getChatHistory(currentPage, chatId)
    }

    override fun getApiToken(): String {
        return prefManger.loadApiToken()
    }
    override fun getFirebaseToken(): String {
        return prefManger.getFireBaseToken()
    }

    override suspend fun updateAdvertStepFeature(map: LinkedHashMap<String, Any>): Call<Calling<Any>> {
        return server.updateAdvertStepFeature(map)
    }

    override suspend fun updateAdvertStepContact(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.updateAdvertStepContact(map)
    }

    override suspend fun editOrderStepSection(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.editOrderStepSection(map)
    }

    override suspend fun editOrderStepFeature(map: LinkedHashMap<String, Any>): Call<Calling<Any>> {
        return server.editOrderStepFeature(map)
    }

    override suspend fun editOrderStepContact(map: HashMap<String, Any>): Call<Calling<Any>> {
        return server.editOrderStepContact(map)
    }

    override suspend fun getAdvertSpecialPackageInfo(advertId: Int): Call<Calling<MyPackageModel>> {
        return server.getAdvertSpecialPackageInfo(advertId)
    }

    override suspend fun getAdvertComments(
        advertId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>> {
        return server.getAdvertComments(advertId,currentPage)
    }

    override suspend fun getOrderComments(
        orderId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>> {
        return server.getOrderComments(orderId,currentPage)
    }

    override suspend fun getQuestionsComments(
        questionId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<CommentModel>>> {
        return server.getQuestionsComments(questionId,currentPage)
    }

    override suspend fun getAdvertRates(
        advertId: Int,
        currentPage: Int
    ): Call<Calling<PaginationModel<RateModel>>> {
        return server.getAdvertRates(advertId,currentPage)
    }

    override suspend fun createChat(
        type: String,
        relatedId: Int
    ): Call<Calling<ChattingContentModel>> {
        return server.createChat(type,relatedId)
    }

    override fun saveFireBaseToken(token: String) {
        prefManger.setFireBaseToken(token)
    }

    override fun loginAsVisitor() {
        prefManger.setInVisitorMode(true)
    }

    override fun isInVisitorMode(): Boolean {
        return  prefManger.isInVisitorMode()
    }

    override fun getuserData(): UserModel? {
        return prefManger.loadUserData()
    }

    override fun getUserLastLocation(): Location {
        return prefManger.getUserLocation()
    }

    override suspend fun logOut(): Call<Calling<Any>> {
        return server.logOut()
    }
}