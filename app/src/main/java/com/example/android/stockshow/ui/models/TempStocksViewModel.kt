//package com.example.android.stockshow.ui.models
//
//import android.util.Log
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.android.stockshow.data.CompanyRepository
//import com.example.android.stockshow.data.clients.QuoteClientInstance
//import com.example.android.stockshow.data.response.CompanyProfile
//import com.example.android.stockshow.data.response.StockItem
//import io.reactivex.disposables.CompositeDisposable
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.util.*
//
//class TempStocksViewModel(private val repo: CompanyRepository) : ViewModel() {
//
//    //clients
//    private val quoteClient = QuoteClientInstance.getClient()
//
//    private val disposeBag: CompositeDisposable = CompositeDisposable()
//
//    //storage
//    var stockMap: MutableLiveData<TreeMap<String, StockItem>> =
//        MutableLiveData<TreeMap<String, StockItem>>()
//    private var companyList: List<CompanyProfile>? = repo.companyList
//
//    private val mTickerArray = arrayListOf(
//        "MMM",
//        "AOS",
//        "ABT",
//        "ABBV",
//        "ABMD",
//        "ACN",
//        "ATVI",
//        "ADBE",
//        "AAP",
//        "AMD",
//        "AES",
//        "AFL",
//        "A",
//        "APD",
//        "AKAM",
//        "ALK",
//        "ARE",
//        "ALB",
//        "ALXN",
//        "ALGN",
//        "ALLE",
//        "LNT",
//        "ALL",
//        "GOOGL",
//        "GOOG",
//        "MO",
//        "AMZN",
//        "AMCR",
//        "AEE",
//        "AAL",
//        "AEP",
//        "AXP",
//        "AIG",
//        "AMT",
//        "AWK",
//        "AMP",
//        "ABC",
//        "AME",
//        "AMGN",
//        "APH",
//        "ADI",
//        "ANSS",
//        "ANTM",
//        "AON",
//        "APA",
//        "AAPL",
//        "AMAT",
//        "APTV",
//        "ADM",
//        "ANET",
//        "AJG",
//        "AIZ",
//        "T",
//        "ATO",
//        "ADSK",
//        "ADP",
//        "AZO",
//        "AVB",
//        "AVY",
//        "BKR",
//        "BLL",
//        "BAC",
//        "BAX",
//        "BDX",
//        "BRK.B",
//        "BBY",
//        "BIO",
//        "BIIB",
//        "BLK",
//        "BA",
//        "BKNG",
//        "BWA",
//        "BXP",
//        "BSX",
//        "BMY",
//        "AVGO",
//        "BR",
//        "BF.B",
//        "CHRW",
//        "COG",
//        "CDNS",
//        "CZR",
//        "CPB",
//        "COF",
//        "CAH",
//        "KMX",
//        "CCL",
//        "CARR",
//        "CTLT",
//        "CAT",
//        "CBOE",
//        "CBRE",
//        "CDW",
//        "CE",
//        "CNC",
//        "CNP",
//        "CERN",
//        "CF",
//        "SCHW",
//        "CHTR",
//        "CVX",
//        "CMG",
//        "CB",
//        "CHD",
//        "CI",
//        "CINF",
//        "CTAS",
//        "CSCO",
//        "C",
//        "CFG",
//        "CTXS",
//        "CME",
//        "CMS",
//        "KO",
//        "CTSH",
//        "CL",
//        "CMCSA",
//        "CMA",
//        "CAG",
//        "COP",
//        "ED",
//        "STZ",
//        "CPRT",
//        "GLW",
//        "CTVA",
//        "COST",
//        "CCI",
//        "CSX",
//        "CMI",
//        "CVS",
//        "DHI",
//        "DHR",
//        "DRI",
//        "DVA",
//        "DE",
//        "DAL",
//        "XRAY",
//        "DVN",
//        "DXCM",
//        "FANG",
//        "DLR",
//        "DFS",
//        "DISCA",
//        "DISCK",
//        "DISH",
//        "DG",
//        "DLTR",
//        "D",
//        "DPZ",
//        "DOV",
//        "DOW",
//        "DTE",
//        "DUK",
//        "DRE",
//        "DD",
//        "DXC",
//        "EMN",
//        "ETN",
//        "EBAY",
//        "ECL",
//        "EIX",
//        "EW",
//        "EA",
//        "EMR",
//        "ENPH",
//        "ETR",
//        "EOG",
//        "EFX",
//        "EQIX",
//        "EQR",
//        "ESS",
//        "EL",
//        "ETSY",
//        "RE",
//        "EVRG",
//        "ES",
//        "EXC",
//        "EXPE",
//        "EXPD",
//        "EXR",
//        "XOM",
//        "FFIV",
//        "FB",
//        "FAST",
//        "FRT",
//        "FDX",
//        "FIS",
//        "FITB",
//        "FRC",
//        "FE",
//        "FISV",
//        "FLT",
//        "FLIR",
//        "FMC",
//        "F",
//        "FTNT",
//        "FTV",
//        "FBHS",
//        "FOXA",
//        "FOX",
//        "BEN",
//        "FCX",
//        "GPS",
//        "GRMN",
//        "IT",
//        "GNRC",
//        "GD",
//        "GE",
//        "GIS",
//        "GM",
//        "GPC",
//        "GILD",
//        "GPN",
//        "GL",
//        "GS",
//        "GWW",
//        "HAL",
//        "HBI",
//        "HIG",
//        "HAS",
//        "HCA",
//        "PEAK",
//        "HSIC",
//        "HES",
//        "HPE",
//        "HLT",
//        "HFC",
//        "HOLX",
//        "HD",
//        "HON",
//        "HRL",
//        "HST",
//        "HWM",
//        "HPQ",
//        "HUM",
//        "HBAN",
//        "HII",
//        "IEX",
//        "IDXX",
//        "INFO",
//        "ITW",
//        "ILMN",
//        "INCY",
//        "IR",
//        "INTC",
//        "ICE",
//        "IBM",
//        "IFF",
//        "IP",
//        "IPG",
//        "INTU",
//        "ISRG",
//        "IVZ",
//        "IPGP",
//        "IQV",
//        "IRM",
//        "JBHT",
//        "JKHY",
//        "J",
//        "SJM",
//        "JNJ",
//        "JCI",
//        "JPM",
//        "JNPR",
//        "KSU",
//        "K",
//        "KEY",
//        "KEYS",
//        "KMB",
//        "KIM",
//        "KMI",
//        "KLAC",
//        "KHC",
//        "KR",
//        "LB",
//        "LHX",
//        "LH",
//        "LRCX",
//        "LW",
//        "LVS",
//        "LEG",
//        "LDOS",
//        "LEN",
//        "LLY",
//        "LNC",
//        "LIN",
//        "LYV",
//        "LKQ",
//        "LMT",
//        "L",
//        "LOW",
//        "LUMN",
//        "LYB",
//        "MTB",
//        "MRO",
//        "MPC",
//        "MKTX",
//        "MAR",
//        "MMC",
//        "MLM",
//        "MAS",
//        "MA",
//        "MXIM",
//        "MKC",
//        "MCD",
//        "MCK",
//        "MDT",
//        "MRK",
//        "MET",
//        "MTD",
//        "MGM",
//        "MCHP",
//        "MU",
//        "MSFT",
//        "MAA",
//        "MHK",
//        "TAP",
//        "MDLZ",
//        "MPWR",
//        "MNST",
//        "MCO",
//        "MS",
//        "MSI",
//        "MSCI",
//        "NDAQ",
//        "NTAP",
//        "NFLX",
//        "NWL",
//        "NEM",
//        "NWSA",
//        "NWS",
//        "NEE",
//        "NLSN",
//        "NKE",
//        "NI",
//        "NSC",
//        "NTRS",
//        "NOC",
//        "NLOK",
//        "NCLH",
//        "NOV",
//        "NRG",
//        "NUE",
//        "NVDA",
//        "NVR",
//        "NXPI",
//        "ORLY",
//        "OXY",
//        "ODFL",
//        "OMC",
//        "OKE",
//        "ORCL",
//        "OTIS",
//        "PCAR",
//        "PKG",
//        "PH",
//        "PAYX",
//        "PAYC",
//        "PYPL",
//        "PENN",
//        "PNR",
//        "PBCT",
//        "PEP",
//        "PKI",
//        "PRGO",
//        "PFE",
//        "PM",
//        "PSX",
//        "PNW",
//        "PXD",
//        "PNC",
//        "POOL",
//        "PPG",
//        "PPL",
//        "PFG",
//        "PG",
//        "PGR",
//        "PLD",
//        "PRU",
//        "PEG",
//        "PSA",
//        "PHM",
//        "PVH",
//        "QRVO",
//        "QCOM",
//        "PWR",
//        "DGX",
//        "RL",
//        "RJF",
//        "RTX",
//        "O",
//        "REG",
//        "REGN",
//        "RF",
//        "RSG",
//        "RMD",
//        "RHI",
//        "ROK",
//        "ROL",
//        "ROP",
//        "ROST",
//        "RCL",
//        "SPGI",
//        "CRM",
//        "SBAC",
//        "SLB",
//        "STX",
//        "SEE",
//        "SRE",
//        "NOW",
//        "SHW",
//        "SPG",
//        "SWKS",
//        "SNA",
//        "SO",
//        "LUV",
//        "SWK",
//        "SBUX",
//        "STT",
//        "STE",
//        "SYK",
//        "SIVB",
//        "SYF",
//        "SNPS",
//        "SYY",
//        "TMUS",
//        "TROW",
//        "TTWO",
//        "TPR",
//        "TGT",
//        "TEL",
//        "TDY",
//        "TFX",
//        "TER",
//        "TSLA",
//        "TXN",
//        "TXT",
//        "BK",
//        "CLX",
//        "COO",
//        "HSY",
//        "MOS",
//        "TRV",
//        "DIS",
//        "TMO",
//        "TJX",
//        "TSCO",
//        "TT",
//        "TDG",
//        "TRMB",
//        "TFC",
//        "TWTR",
//        "TYL",
//        "TSN",
//        "USB",
//        "UDR",
//        "ULTA",
//        "UAA",
//        "UA",
//        "UNP",
//        "UAL",
//        "UPS",
//        "URI",
//        "UNH",
//        "UHS",
//        "UNM",
//        "VLO",
//        "VAR",
//        "VTR",
//        "VRSN",
//        "VRSK",
//        "VZ",
//        "VRTX",
//        "VFC",
//        "VIAC",
//        "VTRS",
//        "V",
//        "VNO",
//        "VMC",
//        "WRB",
//        "WBA",
//        "WMT",
//        "WM",
//        "WAT",
//        "WEC",
//        "WFC",
//        "WELL",
//        "WST",
//        "WDC",
//        "WU",
//        "WAB",
//        "WRK",
//        "WY",
//        "WHR",
//        "WMB",
//        "WLTW",
//        "WYNN",
//        "XEL",
//        "XLNX",
//        "XYL",
//        "YUM",
//        "ZBRA",
//        "ZBH",
//        "ZION"
//    )
//
//    private suspend fun fetchStocks() {
//        val map = TreeMap<String, StockItem>()
//        for (i in 0..30) {
//            withContext(Dispatchers.IO) {
//                val companyProfile =
//                    quoteClient?.retrofitQuoteInstance?.getCompanyProfile(mTickerArray[i])
//                delay(34)
//                val quote = quoteClient?.retrofitQuoteInstance?.getQuote(mTickerArray[i])
//                val stockItem = StockItem(
//                    companyProfile!!.name,
//                    companyProfile.logo,
//                    companyProfile.ticker,
//                    quote!!.latestPrice,
//                    quote.previousClose,
//                    false
//                )
//                map[mTickerArray[i]] = stockItem
//                stockMap.postValue(map)
//                Log.i("fetchStocks", stockItem.toString())
//                delay(34)
//            }
//        }
//    }
//
//    private fun insertCompaniesToDB() {
//        for (company in companyList!!) {
//            repo.insert(company)
//        }
//    }
//
////    private suspend fun createStocks() = withContext(Dispatchers.IO) {
////        val map = TreeMap<String, StockItem>()
////
////        for (company in companyList!!) {
////            val disposable = quoteClient?.retrofitQuoteInstance?.getQuote(company.ticker)
////                ?.subscribeOn(Schedulers.io())
////                ?.observeOn(AndroidSchedulers.mainThread())
////                ?.subscribe({ quote ->
////                    val stockItem = StockItem(
////                        company.name,
////                        company.logo,
////                        company.ticker,
////                        quote.latestPrice,
////                        quote.previousClose,
////                        false
////                    )
////
////                    Log.i("createStocks", stockItem.toString())
////
////                    map[company.ticker] = stockItem
////                    stockMap.value = map
////                },
////                    { throwable ->
////                        Log.i(
////                            Log.getStackTraceString(throwable),
////                            throwable.message.toString()
////                        )
////                    })
////
////            disposeBag.add(disposable!!)
////
////            delay(34)
////        }
////    }
//
//    override fun onCleared() {
//        super.onCleared()
//        disposeBag.clear()
//    }
//
//    fun setupStockMap() {
////        if (companyList.isNullOrEmpty()) {
////            Log.i("setupStockMap", "companyList.isNullOrEmpty()")
////            viewModelScope.launch {
////                fetchStocks()
////            }
////        } else {
////            Log.i("setupStockMap", "companyList is not isNullOrEmpty()")
////            viewModelScope.launch {
////                //createStocks()
////            }
////        }
//        viewModelScope.launch {
//            fetchStocks()
//        }
//    }
//}