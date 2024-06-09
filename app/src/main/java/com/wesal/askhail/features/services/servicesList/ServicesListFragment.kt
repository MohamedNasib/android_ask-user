package com.wesal.askhail.features.services.servicesList
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseFragment
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.FragmentMessagesBinding
import com.wesal.askhail.databinding.FragmentServiceBinding
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.ServicesModel
import timber.log.Timber

class ServicesListFragment : BaseFragment(), ServicesAdapter.OnServices {
    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private var serviceType: String = ""
    override fun layoutRes(): Int = R.layout.fragment_service
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serviceType = it.getString(ExtraConst.EXTRA_SERVICE_TYPE, "")
            Timber.e("Service Type ==> $serviceType")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessFragment(
            this,
            {UseCaseImpl().getServicesList(serviceType)},
            R.drawable.ic_status_empty_ask,
            R.string.empty_service,
            false
        ){
            it?.let { q->
                binding.rvServices?.layoutManager = LinearLayoutManager(activity)
                val adapter = ServicesAdapter(q.toMutableList(),this@ServicesListFragment)
                binding.rvServices?.adapter=adapter
            }
        }

    }

    override fun onServices(model: ServicesModel) {
        val uri = Uri.parse(model.serviceFile)
        // Create request for android download manager
        val downloadManager =
            requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(uri)
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or
                    DownloadManager.Request.NETWORK_MOBILE
        )
        context?.toasting("Start Downloading")
        request.setTitle("Data Download")
        request.setDescription("Android Data download using DownloadManager.")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            uri.lastPathSegment
        );
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadManager.enqueue(request)
    }
}