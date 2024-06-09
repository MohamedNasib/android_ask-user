package com.wesal.askhail.features.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseFragment
import com.wesal.askhail.core.customViews.CustomLinearLayoutManager
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.NavPhonePage
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.FragmentMessagesBinding
import com.wesal.askhail.features.chatActivity.ChatActivity
import com.wesal.askhail.features.login.LoginActivity
import com.wesal.askhail.features.typePhone.TypePhoneActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.MessagesModel

class MessagesFragment : BaseFragment(), MessagesAdapter.OnMessageClicked {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var showingFilter = "all"
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: MessagesAdapter? = null

    override fun layoutRes(): Int = R.layout.fragment_messages
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MessagesAdapter(mutableListOf(), this@MessagesFragment)
         if (UseCaseImpl().isInVisitorMode()){
            binding.viewVisitor?.visible()
            binding.viewContainer?.gone()
            binding.viewMsgController?.gone()
        }else{
            initViews()
            launching()
        }
        clickers()

    }

    private fun clickers() {
        binding.viewLoginExistAccount?.setOnClickListener {
            context?.sStartActivity<LoginActivity>()
        }
        binding.viewCreateAccount?.setOnClickListener {
            context?.sStartActivity<TypePhoneActivity>(
                ExtraConst.EXTRA_FROM to NavPhonePage.REGISTER
            )
        }
        binding.swMessages.setOnCheckedChangeListener{a,b->
            if (!b){
                showingFilter = "all"
            }else{
                showingFilter = "mine"
            }
            launching()
        }
    }

    private fun initViews() {
        val layoutManager = CustomLinearLayoutManager(requireContext())
        binding.rvMessages?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })
        binding.rvMessages?.layoutManager = layoutManager
        binding.rvMessages?.adapter = adapter
        binding.rvMessages.itemAnimator = null
    }
    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessFragment(
            this@MessagesFragment,
            { UseCaseImpl().getMessages(currentPage,showingFilter) },
            R.drawable.ic_status_empty_chat,
            R.string.empty_chat,
            false
        ) {
            adapter?.clearData()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
        }
    }
    private fun getMoreItems() {
        currentPage++
        adapter?.addLoadingFooter()
        ParaNormalProcess.silentProcessFragment(
            this@MessagesFragment,
            { UseCaseImpl().getMessages(currentPage,showingFilter) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
            isLoading = false
        }
    }
    override fun onMessage(model: MessagesModel) {
        context?.sStartActivity<ChatActivity>(
            ExtraConst.EXTRA_CHAT_ID to model.chatId
        )
    }
}