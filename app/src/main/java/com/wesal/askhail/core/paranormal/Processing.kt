package com.wesal.askhail.core.paranormal

object Processing {
//    fun selectingCountry(ac: BaseActivity, callback :(value: CountryModel)->Unit) {
//        val loadedCountries = UseCaseImpl().loadCountries()
//        if (loadedCountries!=null){
//            SingleChoiceDialog(ac,
//                ac.getString(R.string.select_your_country),
//                loadedCountries,
//                object : OnSingleDialogSelected<CountryModel> {
//                    override fun onSelected(value: CountryModel) {
//                        callback.invoke(value)
//                    }
//                })
//        }else{
//            ParaNormalProcess.loadingProcessActivity(
//                ac,
//                {UseCaseImpl().getCountries()}
//            ){
//                SingleChoiceDialog(ac,
//                    ac.getString(R.string.select_your_country),
//                    it,
//                    object : OnSingleDialogSelected<CountryModel> {
//                        override fun onSelected(value: CountryModel) {
//                            callback.invoke(value)
//                        }
//                    })
//            }
//
//        }
//    }
//    fun selectingCity(ac: BaseActivity, countryId:Int, callback :(value: CityModel)->Unit) {
//        ParaNormalProcess.loadingProcessActivity(
//                ac,
//                { UseCaseImpl().getCities(countryId) }
//        ) {
//            SingleChoiceDialog(ac,
//                    ac.getString(R.string.select_your_city),
//                    it,
//                    object : OnSingleDialogSelected<CityModel> {
//                        override fun onSelected(value: CityModel) {
//                            callback.invoke(value)
//                        }
//                    })
//        }
//    }

}