package com.wesal.askhail.features.chatActivity

import android.os.Bundle
import android.widget.PopupMenu
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.customViews.CustomLinearLayoutManager
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.ActivityChangePasswordBinding
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.orderDetails.OrderDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.ChatDetailsModel
import com.wesal.entities.models.MessagesDataModel
import kotlinx.coroutines.launch
import timber.log.Timber

class ChatActivity : BaseActivity(), MySocket.OnNewMsgArrived {
    lateinit var binding: ActivityChatBinding;
    private lateinit var language: String
    private var chatInfoModel: ChatDetailsModel? = null
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: ChatAdapter? = ChatAdapter(mutableListOf())
    private var chatId: Int = -1
    private var myUserToken = ""
    override fun layoutResource(): Int =R.layout.activity_chat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        chatId = intent.getIntExtra(ExtraConst.EXTRA_CHAT_ID,-1)
        myUserToken= UseCaseImpl().getApiToken()
        language = UseCaseImpl().getSystemLanguage()
        adapter?.currentUserId = UseCaseImpl().getCurrentUserId()
        setToolbar()
        setWhiteActivity()
        setUpRecycler()
        loadingChatHistory()
        clickers()
    }

    private fun clickers() {
        binding.ivSend.setOnClickListener {
            if (!binding.etMessage.value().isEmpty()){
                MySocket.sendTextMessage(chatId,binding.etMessage.value(),myUserToken,language)
                binding.etMessage.setText("")
            }
        }

        binding.ivMore.setOnClickListener {
            if (chatInfoModel!=null){
                val pop = PopupMenu(this@ChatActivity, binding.ivMore)
                pop.inflate(R.menu.menu_chat)
                pop.setOnMenuItemClickListener {item->
                    when (item.itemId) {
                        R.id.option_details->{
                        goToChatAboutDetails()
                        }
                    }
                    true
                }
                pop.show()

            }
        }
    }

    private fun goToChatAboutDetails() {
        chatInfoModel?.let {
            when(it.chatType){
                "advertisement"->{
                    sStartActivity<AdvertDetailsActivity>(
                        ExtraConst.EXTRA_ADVERT_ID to it.chatAdvOrderId
                    )
                }
                "order"->{
                    sStartActivity<OrderDetailsActivity>(
                        ExtraConst.EXTRA_ORDER_ID to it.chatAdvOrderId
                    )
                }
            }
        }

    }

    private fun setUpRecycler() {
        val layoutManager = CustomLinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        binding.rvChat.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })
        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.adapter = adapter
        binding.rvChat.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                Timber.e("TEST DELAY addOnLayoutChangeListener")
                binding.rvChat.postDelayed({ binding.rvChat.smoothScrollToPosition(0) }, 100)
            }
        }
    }
    private fun updateChatInfo() {
        binding.tvTalkUserName.text = chatInfoModel?.chatOtherName
        binding.tvRelatedName.text = chatInfoModel?.chatTitle

    }
    private fun loadingChatHistory() {
        currentPage = 1
        ParaNormalProcess.viewProcessActivity(
            this@ChatActivity,
            { UseCaseImpl().getChatHistory(currentPage, chatId) }
        ) {
            chatInfoModel = it?.chatDetails
            updateChatInfo()
            adapter?.clearData()
            adapter?.addMoreInList(it?.messagesData)
            it?.let {
                isLastPage = !it.messagesPagination.hasMorePages
            }
            binding.rvChat.scrollToPosition(0)
        }
    }
    private fun getMoreItems() {
        currentPage++
        adapter?.addLoadingFooter()
        ParaNormalProcess.silentProcessActivity(
            this@ChatActivity,
            { UseCaseImpl().getChatHistory(currentPage, chatId) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.messagesData)
            it?.let {
                isLastPage = !it.messagesPagination.hasMorePages
            }
            isLoading = false
        }
    }
    override fun onPause() {
        Timber.e("Pausing ====>>>>>>>>>>")
        super.onPause()
        try {
            if (chatId != 0)
                MySocket.disconnect(chatId, myUserToken)
        } catch (e: Exception) {
        }
    }
    override fun onStart() {
        super.onStart()
        startChatObserving()
    }
    private fun startChatObserving() {
        MySocket.authorizeSocket(myUserToken, chatId)
        MySocket.startObserveChat(chatId, this@ChatActivity)
    }
    override fun onBackPressed() {
        MySocket.disconnect(chatId, myUserToken)
        super.onBackPressed()
    }
    override fun onNewMsg(model: MessagesDataModel) {
        scopeMain.launch {
            adapter?.addOneMsg(model)
            binding.rvChat.scrollToPosition(0)
        }

    }
}