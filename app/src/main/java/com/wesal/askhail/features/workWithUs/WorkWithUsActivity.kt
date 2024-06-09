package com.wesal.askhail.features.workWithUs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.ActivityWorkWithUsBinding
import com.wesal.askhail.features.workApply.WorkApplyActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.base.PaginationModel
import com.wesal.entities.models.JobsModel

class WorkWithUsActivity : BaseActivity(), JobsAdapter.OnJob {
    lateinit var binding: ActivityWorkWithUsBinding;
    override fun layoutResource(): Int =R.layout.activity_work_with_us
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkWithUsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.work_with_us))
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            {UseCaseImpl().getJobs()}
        ){
            updateUi(it)
        }
    }

    private fun updateUi(it: PaginationModel<JobsModel>?) {
        val data = it?.data
        data?.let {
            binding.rvJobs.layoutManager = LinearLayoutManager(this)
            val adapter = JobsAdapter(it.toMutableList(),this)
            binding.rvJobs.adapter = adapter
        }
    }
    override fun onJob(model: JobsModel) {
        sStartActivity<WorkApplyActivity>(
            ExtraConst.EXTRA_MODEL to model
        )

    }
}