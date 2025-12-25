package com.barryburgle.gameapp.model.enums

import com.barryburgle.gameapp.model.stat.CategoryHistogram

enum class CountryEnum(val flag: String, val alpha3: String, val countryName: String) {
    ABW("🇦🇼", "ABW", "Aruba"),
    AFG("🇦🇫", "AFG", "Afghanistan"),
    AGO("🇦🇴", "AGO", "Angola"),
    AIA("🇦🇮", "AIA", "Anguilla"),
    ALA("🇦🇽", "ALA", "Åland Islands"),
    ALB("🇦🇱", "ALB", "Albania"),
    AND("🇦🇩", "AND", "Andorra"),
    ARE("🇦🇪", "ARE", "United Arab Emirates"),
    ARG("🇦🇷", "ARG", "Argentina"),
    ARM("🇦🇲", "ARM", "Armenia"),
    ASM("🇦🇸", "ASM", "American Samoa"),
    ATA("🇦🇶", "ATA", "Antarctica"),
    ATG("🇦🇬", "ATG", "Antigua and Barbuda"),
    AUS("🇦🇺", "AUS", "Australia"),
    AUT("🇦🇹", "AUT", "Austria"),
    AZE("🇦🇿", "AZE", "Azerbaijan"),
    BDI("🇧🇮", "BDI", "Burundi"),
    BEL("🇧🇪", "BEL", "Belgium"),
    BEN("🇧🇯", "BEN", "Benin"),
    BFA("🇧🇫", "BFA", "Bonaire, Sint Eustatius and Saba"),
    BGD("🇧🇩", "BGD", "Bangladesh"),
    BGR("🇧🇬", "BGR", "Bulgaria"),
    BHR("🇧🇭", "BHR", "Bahrain"),
    BHS("🇧🇸", "BHS", "Bahamas"),
    BIH("🇧🇦", "BIH", "Bosnia and Herzegovina"),
    BLM("🇧🇱", "BLM", "Saint Barthélemy"),
    BLR("🇧🇾", "BLR", "Belarus"),
    BLZ("🇧🇿", "BLZ", "Belize"),
    BMU("🇧🇲", "BMU", "Bermuda"),
    BOL("🇧🇴", "BOL", "Bolivia, Plurinational State of"),
    BRB("🇧🇧", "BRB", "Brazil"),
    BRN("🇧🇳", "BRN", "Barbados"),
    BTN("🇧🇹", "BTN", "Brunei Darussalam"),
    BVT("🇧🇻", "BVT", "Bhutan"),
    BWA("🇧🇼", "BWA", "Bouvet Island"),
    CAF("🇨🇫", "CAF", "Central African Republic"),
    CAN("🇨🇦", "CAN", "Canada"),
    CCK("🇨🇨", "CCK", "Cocos (Keeling) Islands"),
    CHE("🇨🇭", "CHE", "Switzerland"),
    CHL("🇨🇱", "CHL", "Chile"),
    CN("🇨🇳", "CHN", "China"),
    HK("🇭🇰", "CHN", "Hong Kong"),
    CIV("🇨🇮", "CIV", "Côte d'Ivoire"),
    CMR("🇨🇲", "CMR", "Cameroon"),
    COG("🇨🇬", "COG", "Congo, Democratic Republic of the"),
    COK("🇨🇰", "COK", "Cook Islands"),
    COL("🇨🇴", "COL", "Colombia"),
    COM("🇰🇲", "COM", "Comoros"),
    CPT("🇨🇵", "CPT", "French Southern Territories"),
    CPV("🇨🇻", "CPV", "Cabo Verde"),
    CRI("🇨🇷", "CRI", "Costa Rica"),
    CUB("🇨🇺", "CUB", "Cuba"),
    CUW("🇨🇼", "CUW", "Curaçao"),
    CXR("🇨🇽", "CXR", "Christmas Island"),
    CYM("🇰🇾", "CYM", "Cayman Islands"),
    CYP("🇨🇾", "CYP", "Cyprus"),
    CZE("🇨🇿", "CZE", "Czechia"),
    DEU("🇩🇪", "DEU", "Germany"),
    DJI("🇩🇯", "DJI", "Djibouti"),
    DMA("🇩🇲", "DMA", "Dominica"),
    DOM("🇩🇰", "DOM", "Dominican Republic"),
    DNK("🇩🇰", "DNK", "Denmark"),
    DZA("🇩🇿", "DZA", "Algeria"),
    ECU("🇪🇨", "ECU", "Ecuador"),
    EGY("🇪🇬", "EGY", "Egypt"),
    ERI("🇪🇷", "ERI", "Eritrea"),
    ESH("🇪🇭", "ESH", "Western Sahara"),
    ESP("🇪🇸", "ESP", "Spain"),
    EST("🇪🇪", "EST", "Estonia"),
    ETH("🇪🇹", "ETH", "Ethiopia"),
    FIN("🇫🇮", "FIN", "Finland"),
    FJI("🇫🇯", "FJI", "Fiji"),
    FLK("🇫🇰", "FLK", "Falkland Islands"),
    FRA("🇫🇷", "FRA", "France"),
    FRO("🇫🇴", "FRO", "Faroe Islands"),
    FSM("🇫🇲", "FSM", "Micronesia"),
    GAB("🇬🇦", "GAB", "Gabon"),
    GBR("🇬🇧", "GBR", "Great Britain"),
    GEO("🇬🇪", "GEO", "Georgia"),
    GGY("🇬🇬", "GGY", "Guernsey"),
    GHA("🇬🇭", "GHA", "Ghana"),
    GIB("🇬🇮", "GIB", "Gibraltar"),
    GIN("🇬🇳", "GIN", "Guinea"),
    GLP("🇬🇵", "GLP", "Guadeloupe"),
    GMB("🇬🇲", "GMB", "Gambia"),
    GNB("🇬🇼", "GNB", "Guinea-Bissau"),
    GNQ("🇬🇶", "GNQ", "Equatorial Guinea"),
    GRC("🇬🇷", "GRC", "Greece"),
    GRD("🇬🇩", "GRD", "Grenada"),
    GRL("🇬🇱", "GRL", "Greenland"),
    GTM("🇬🇹", "GTM", "Guatemala"),
    GUF("🇬🇫", "GUF", "French Guiana"),
    GUM("🇬🇺", "GUM", "Guam"),
    GUY("🇬🇾", "GUY", "Guyana"),
    HKG("🇭🇰", "HKG", "Hong Kong"),
    HMD("🇭🇲", "HMD", "Heard Island and McDonald Islands"),
    HND("🇭🇳", "HND", "Honduras"),
    HRV("🇭🇷", "HRV", "Croatia"),
    HTI("🇭🇹", "HTI", "Haiti"),
    HUN("🇭🇺", "HUN", "Hungary"),
    IDN("🇮🇩", "IDN", "Indonesia"),
    IMN("🇮🇲", "IMN", "Isle of Man"),
    IND("🇮🇳", "IND", "India"),
    IOT("🇮🇴", "IOT", "British Indian Ocean Territory"),
    IRL("🇮🇪", "IRL", "Ireland"),
    IRN("🇮🇷", "IRN", "Iran"),
    IRQ("🇮🇶", "IRQ", "Iraq"),
    ISL("🇮🇸", "ISL", "Iceland"),
    ISR("🇮🇱", "ISR", "Israel"),
    ITA("🇮🇹", "ITA", "Italy"),
    JAM("🇯🇲", "JAM", "Jamaica"),
    JEY("🇯🇪", "JEY", "Jersey"),
    JOR("🇯🇴", "JOR", "Jordan"),
    JPN("🇯🇵", "JPN", "Japan"),
    KAZ("🇰🇿", "KAZ", "Kazakhstan"),
    KEN("🇰🇪", "KEN", "Kenya"),
    KGZ("🇰🇬", "KGZ", "Kyrgyzstan"),
    KHM("🇰🇭", "KHM", "Cambodia"),
    KIR("🇰🇮", "KIR", "Kiribati"),
    KNA("🇰🇳", "KNA", "Saint Kitts and Nevis"),
    KOR("🇰🇷", "KOR", "Republic of Korea"),
    KWT("🇰🇼", "KWT", "Kuwait"),
    LA("🇱🇦", "LA", "Lao People's Democratic Republic"),
    LBN("🇱🇧", "LBN", "Lebanon"),
    LBR("🇱🇷", "LBR", "Liberia"),
    LBY("🇱🇾", "LBY", "Libya"),
    LCA("🇱🇨", "LCA", "Saint Lucia"),
    LIE("🇱🇮", "LIE", "Liechtenstein"),
    LKA("🇱🇰", "LKA", "Sri Lanka"),
    LSO("🇱🇸", "LSO", "Lesotho"),
    LTU("🇱🇹", "LTU", "Lithuania"),
    LUX("🇱🇺", "LUX", "Luxembourg"),
    LVA("🇱🇻", "LVA", "Latvia"),
    MAC("🇲🇴", "MAC", "Macao"),
    MAF("🇲🇫", "MAF", "Saint Martin (French part)"),
    MAR("🇲🇦", "MAR", "Morocco"),
    MCO("🇲🇨", "MCO", "Monaco"),
    MDA("🇲🇩", "MDA", "Republic of Moldova"),
    MDG("🇲🇬", "MDG", "Madagascar"),
    MDV("🇲🇻", "MDV", "Maldives"),
    MEX("🇲🇽", "MEX", "Mexico"),
    MHL("🇲🇭", "MHL", "Marshall Islands"),
    MKD("🇲🇰", "MKD", "Republic of North Macedonia"),
    MLI("🇲🇱", "MLI", "Mali"),
    MLT("🇲🇹", "MLT", "Malta"),
    MMR("🇲🇲", "MMR", "Myanmar"),
    MNE("🇲🇪", "MNE", "Montenegro"),
    MNG("🇲🇳", "MNG", "Mongolia"),
    MNP("🇲🇵", "MNP", "Northern Mariana Islands"),
    MOZ("🇲🇿", "MOZ", "Mozambique"),
    MRT("🇲🇷", "MRT", "Mauritania"),
    MSR("🇲🇸", "MSR", "Montserrat"),
    MTQ("🇲🇶", "MTQ", "Martinique"),
    MUS("🇲🇺", "MUS", "Mauritius"),
    MWI("🇲🇼", "MWI", "Malawi"),
    MYS("🇲🇾", "MYS", "Malaysia"),
    MYT("🇾🇹", "MYT", "Mayotte"),
    NAM("🇳🇦", "NAM", "Namibia"),
    NCL("🇳🇨", "NCL", "New Caledonia"),
    NER("🇳🇪", "NER", "Niger"),
    NFK("🇳🇫", "NFK", "Norfolk Island"),
    NGA("🇳🇬", "NGA", "Nigeria"),//
    NIC("🇳🇮", "NIC", "Nicaragua"),
    NIU("🇳🇺", "NIU", "Niue"),
    NLD("🇳🇱", "NLD", "Netherlands"),
    NOR("🇳🇴", "NOR", "Norway"),
    NPL("🇳🇵", "NPL", "Nepal"),
    NRU("🇳🇷", "NRU", "Nauru"),
    NZL("🇳🇿", "NZL", "New Zealand"),
    OMN("🇴🇲", "OMN", "Oman"),
    PAK("🇵🇰", "PAK", "Pakistan"),
    PAN("🇵🇦", "PAN", "Panama"),
    PCN("🇵🇳", "PCN", "Pitcairn"),
    PER("🇵🇪", "PER", "Peru"),
    PHL("🇵🇭", "PHL", "Philippines"),
    PLW("🇵🇼", "PLW", "Palau"),
    PNG("🇵🇬", "PNG", "Papua New Guinea"),
    POL("🇵🇱", "POL", "Poland"),
    PRI("🇵🇷", "PRI", "Puerto Rico"),
    PRK("🇰🇵", "PRK", "Democratic People's Republic of Korea"),
    PRT("🇵🇹", "PRT", "Portugal"),
    PRY("🇵🇾", "PRY", "Paraguay"),
    PSE("🇵🇸", "PSE", "Palestine"),
    PYF("🇵🇫", "PYF", "French Polynesia"),
    QAT("🇶🇦", "QAT", "Qatar"),
    REU("🇷🇪", "REU", "Réunion"),
    ROU("🇷🇴", "ROU", "Romania"),
    RUS("🇷🇺", "RUS", "Russia"),
    RWA("🇷🇼", "RWA", "Rwanda"),
    SAU("🇸🇦", "SAU", "Saudi Arabia"),
    SDN("🇸🇩", "SDN", "Sudan"),
    SEN("🇸🇳", "SEN", "Senegal"),
    SGP("🇸🇬", "SGP", "Singapore"),
    SGS("🇬🇸", "SGS", "South Georgia and the South Sandwich Islands"),
    SHN("🇸🇭", "SHN", "Saint Helena, Ascension and Tristan da Cunha"),
    SJM("🇸🇯", "SJM", "Svalbard and Jan Mayen"),
    SLB("🇸🇧", "SLB", "Solomon Islands"),
    SLE("🇸🇱", "SLE", "Sierra Leone"),
    SLV("🇸🇻", "SLV", "El Salvador"),
    SMR("🇸🇲", "SMR", "San Marino"),
    SOM("🇸🇴", "SOM", "Somalia"),
    SPM("🇵🇲", "SPM", "Saint Pierre and Miquelon"),
    SRB("🇷🇸", "SRB", "Serbia"),
    SSD("🇸🇸", "SSD", "South Sudan"),
    STP("🇸🇹", "STP", "Sao Tome and Principe"),
    SUR("🇸🇷", "SUR", "Suriname"),
    SVK("🇸🇰", "SVK", "Slovakia"),
    SVN("🇸🇮", "SVN", "Slovenia"),
    SWE("🇸🇪", "SWE", "Sweden"),
    SWZ("🇸🇿", "SWZ", "Eswatini"),
    SXM("🇸🇽", "SXM", "Sint Maarten (Dutch part)"),
    SYC("🇸🇨", "SYC", "Seychelles"),
    SYR("🇸🇾", "SYR", "Syrian Arab Republic"),
    TCA("🇹🇨", "TCA", "Turks and Caicos Islands"),
    TCD("🇹🇩", "TCD", "Chad"),
    TGO("🇹🇬", "TGO", "Togo"),
    THA("🇹🇭", "THA", "Thailand"),
    TJK("🇹🇯", "TJK", "Tajikistan"),
    TKL("🇹🇰", "TKL", "Tokelau"),
    TKM("🇹🇲", "TKM", "Turkmenistan"),
    TLS("🇹🇱", "TLS", "Timor-Leste"),//
    TON("🇹🇴", "TON", "Tonga"),
    TTO("🇹🇹", "TTO", "Trinidad and Tobago"),
    TUN("🇹🇳", "TUN", "Tunisia"),
    TUR("🇹🇷", "TUR", "Turkey"),
    TUV("🇹🇻", "TUV", "Tuvalu"),
    TWN("🇹🇼", "TWN", "Taiwan"),
    TZA("🇹🇿", "TZA", "United Republic of Tanzania"),
    UGA("🇺🇬", "UGA", "Uganda"),
    UKR("🇺🇦", "UKR", "Ukraine"),
    UMI("🇺🇲", "UMI", "United States Minor Outlying Islands"),
    URY("🇺🇾", "URY", "Uruguay"),
    USA("🇺🇸", "USA", "United States of America"),
    UZB("🇺🇿", "UZB", "Uzbekistan"),
    VAT("🇻🇦", "VAT", "Holy See"),
    VCT("🇻🇨", "VCT", "Saint Vincent and the Grenadines"),
    VEN("🇻🇪", "VEN", "Venezuela "),
    VGB("🇻🇬", "VGB", "Virgin Islands"),
    VIR("🇻🇮", "VIR", "Virgin Islands"),
    VNM("🇻🇳", "VNM", "Viet Nam"),
    VUT("🇻🇺", "VUT", "Vanuatu"),
    WLF("🇼🇫", "WLF", "Wallis and Futuna"),
    WSM("🇼🇸", "WSM", "Samoa"),
    YEM("🇾🇪", "YEM", "Yemen"),
    ZAF("🇿🇦", "ZAF", "South Africa"),
    ZMB("🇿🇲", "ZMB", "Zambia"),
    ZWE("🇿🇼", "ZWE", "Zimbabwe");

    companion object {

        private val orderedEnum: List<CountryEnum> = values().sortedBy { it.countryName }

        fun getFlagByAlpha3(alpha3: String): String {
            var foundFlag = "\uD83C\uDFF3\uFE0F"
            for (country in values()) {
                if (country.alpha3 == alpha3) {
                    foundFlag = country.flag
                }
            }
            return foundFlag
        }

        fun getCountryNameByAlpha3(alpha3: String): String {
            var foundCountryName = ""
            for (country in values()) {
                if (country.alpha3 == alpha3) {
                    foundCountryName = country.countryName
                }
            }
            return foundCountryName
        }

        fun getCountriesOrderedByName(
            mostPopularLeadsNationalities: List<CategoryHistogram>,
            showMostPopular: Boolean
        ): List<CountryEnum> {
            if (!showMostPopular) {
                return orderedEnum
            }
            val mostPopularOrderedEnum: MutableList<CountryEnum> = mutableListOf()
            for (nationality in mostPopularLeadsNationalities) {
                for (country in values()) {
                    if (country.alpha3 == nationality.category) {
                        mostPopularOrderedEnum.add(country)
                    }
                }
            }
            mostPopularOrderedEnum.addAll(orderedEnum)
            return mostPopularOrderedEnum
        }

        fun getInsertCountries(
            mostPopularLeadsNationalities: List<CategoryHistogram>,
            showMostPopular: Boolean,
            contained: String
        ): List<CountryEnum> {
            if (!contained.isEmpty()) {
                return values().filter { it.countryName.contains(contained, ignoreCase = true) }
            }
            return getCountriesOrderedByName(mostPopularLeadsNationalities, showMostPopular)
        }


    }
}