package com.salkcoding.langextractor.lang

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import java.io.*

enum class EnumLang(private val lang: String, private val table: HashMap<String, String>) {

    AF_ZA("af_za", HashMap()),
    AR_SA("ar_sa", HashMap()),
    AST_ES("ast_es", HashMap()),
    AZ_AZ("az_az", HashMap()),
    BA_RU("ba_ru", HashMap()),
    BAR("bar", HashMap()),
    BE_BY("be_by", HashMap()),
    BG_BG("bg_bg", HashMap()),
    BR_FR("br_fr", HashMap()),
    BRB("brb", HashMap()),
    BS_BA("bs_ba", HashMap()),
    CA_ES("ca_es", HashMap()),
    CS_CZ("cs_cz", HashMap()),
    CY_GB("cy_gb", HashMap()),
    DA_DK("da_dk", HashMap()),
    DE_AT("de_at", HashMap()),
    DE_CH("de_ch", HashMap()),
    DE_DE("de_de", HashMap()),
    EL_GR("el_gr", HashMap()),
    EN_AU("en_au", HashMap()),
    EN_CA("en_ca", HashMap()),
    EN_GB("en_gb", HashMap()),
    EN_NZ("en_nz", HashMap()),
    EN_PT("en_pt", HashMap()),
    EN_UD("en_ud", HashMap()),
    ENP("enp", HashMap()),
    ENWS("enws", HashMap()),
    EO_UY("eo_uy", HashMap()),
    ES_AR("es_ar", HashMap()),
    ES_CL("es_cl", HashMap()),
    ES_EC("es_ec", HashMap()),
    ES_ES("es_es", HashMap()),
    ES_MX("es_mx", HashMap()),
    ES_UY("es_uy", HashMap()),
    ES_VE("es_ve", HashMap()),
    ESAN("esan", HashMap()),
    ET_EE("et_ee", HashMap()),
    EU_ES("eu_es", HashMap()),
    FA_IR("fa_ir", HashMap()),
    FI_FI("fi_fi", HashMap()),
    FIL_PH("fil_ph", HashMap()),
    FO_FO("fo_fo", HashMap()),
    FR_CA("fr_ca", HashMap()),
    FR_FR("fr_fr", HashMap()),
    FRA_DE("fra_de", HashMap()),
    FUR_IT("fur_it", HashMap()),
    FY_NL("fy_nl", HashMap()),
    GA_IE("ga_ie", HashMap()),
    GD_GB("gd_gb", HashMap()),
    GL_ES("gl_es", HashMap()),
    HAW_US("haw_us", HashMap()),
    HE_IL("he_il", HashMap()),
    HI_IN("hi_in", HashMap()),
    HR_HR("hr_hr", HashMap()),
    HU_HU("hu_hu", HashMap()),
    HY_AM("hy_am", HashMap()),
    ID_ID("id_id", HashMap()),
    IG_NG("ig_ng", HashMap()),
    IO_EN("io_en", HashMap()),
    IS_IS("is_is", HashMap()),
    ISV("isv", HashMap()),
    IT_IT("it_it", HashMap()),
    JA_JP("ja_jp", HashMap()),
    JBO_EN("jbo_en", HashMap()),
    KA_GE("ka_ge", HashMap()),
    KK_KZ("kk_kz", HashMap()),
    KN_IN("kn_in", HashMap()),
    KO_KR("ko_kr", HashMap()),
    KSH("ksh", HashMap()),
    KW_GB("kw_gb", HashMap()),
    LA_LA("la_la", HashMap()),
    LB_LU("lb_lu", HashMap()),
    LI_LI("li_li", HashMap()),
    LMO("lmo", HashMap()),
    LOL_US("lol_us", HashMap()),
    LT_LT("lt_lt", HashMap()),
    LV_LV("lv_lv", HashMap()),
    LZH("lzh", HashMap()),
    MK_MK("mk_mk", HashMap()),
    MN_MN("mn_mn", HashMap()),
    MS_MY("ms_my", HashMap()),
    MT_MT("mt_mt", HashMap()),
    NDS_DE("nds_de", HashMap()),
    NL_BE("nl_be", HashMap()),
    NL_NL("nl_nl", HashMap()),
    NN_NO("nn_no", HashMap()),
    NO_NO("no_no", HashMap()),
    OC_FR("oc_fr", HashMap()),
    OVD("ovd", HashMap()),
    PL_PL("pl_pl", HashMap()),
    PT_BR("pt_br", HashMap()),
    PT_PT("pt_pt", HashMap()),
    QYA_AA("qya_aa", HashMap()),
    RO_RO("ro_ro", HashMap()),
    RPR("rpr", HashMap()),
    RU_RU("ru_ru", HashMap()),
    RY_UA("ry_ua", HashMap()),
    SE_NO("se_no", HashMap()),
    SK_SK("sk_sk", HashMap()),
    SL_SI("sl_si", HashMap()),
    SO_SO("so_so", HashMap()),
    SQ_AL("sq_al", HashMap()),
    SR_SP("sr_sp", HashMap()),
    SV_SE("sv_se", HashMap()),
    SXU("sxu", HashMap()),
    SZL("szl", HashMap()),
    TA_IN("ta_in", HashMap()),
    TH_TH("th_th", HashMap()),
    TL_PH("tl_ph", HashMap()),
    TLH_AA("tlh_aa", HashMap()),
    TOK("tok", HashMap()),
    TR_TR("tr_tr", HashMap()),
    TT_RU("tt_ru", HashMap()),
    UK_UA("uk_ua", HashMap()),
    VAL_ES("val_es", HashMap()),
    VEC_IT("vec_it", HashMap()),
    VI_VN("vi_vn", HashMap()),
    YI_DE("yi_de", HashMap()),
    YO_NG("yo_ng", HashMap()),
    ZH_CN("zh_cn", HashMap()),
    ZH_HK("zh_hk", HashMap()),
    ZH_TW("zh_tw", HashMap()),
    ZLM_ARAB("zlm_arab", HashMap());

    companion object {
        val langFolder = File("./", "lang")
        private val hashFolder = File(System.getenv("APPDATA"), "/.minecraft/assets/objects")
        private lateinit var hashTable: HashTable

        fun loadHashTable(versionFile: File) {
            hashTable = HashTable()
            hashTable.loadHashTable(versionFile)
        }
    }


    fun extractLangFile() {
        hashTable.table.entries.forEach { (lang, hash) ->
            val dataFolder = File(hashFolder, hash.substring(0, 2))
            if (!dataFolder.exists()) {
                println("Skipped $lang: $lang is not existed!")
                return@forEach
            }

            val dataFile = File(dataFolder, hash)
            if (!dataFile.exists()) {
                println("Skipped $lang: $lang is not existed!")
                return@forEach
            }

            val enumLang = EnumLang.valueOf(lang.uppercase())
            BufferedReader(FileReader(dataFile)).use { reader ->
                val json = JsonParser.parseReader(reader).asJsonObject
                json.entrySet().forEach { (key, value) ->
                    enumLang.table[key] = value.asString
                }
            }
        }
    }

    fun saveLangFile() {
        if (!langFolder.exists()) {
            try {
                langFolder.mkdirs()
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }

        val langFile = File(langFolder, "${this.lang}.json")
        if (!langFile.exists()) {
            try {
                langFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }

        BufferedWriter(FileWriter(langFile)).use { writer ->
            val gson = GsonBuilder().disableHtmlEscaping().create()
            val json = JsonObject()
            this.table.entries.forEach { (key, value) ->
                json.addProperty(key, value)
            }
            writer.write(gson.toJson(json))
        }
    }

    override fun toString(): String {
        return this.lang
    }
}