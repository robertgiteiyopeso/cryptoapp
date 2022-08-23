package com.example.cryptoapp.ancientcryptoapp.domain

import com.google.gson.annotations.SerializedName
import java.util.*

data class CryptoCoinDetailsModel(
    val id: String = "",
    val name: String = "",
    val symbol: String = "",
    val rank: Int = 0,
    @SerializedName("is_new")
    val isNew: Boolean = false,
    @SerializedName("is_active")
    val isActive: Boolean = false,
    val type: String = "",
    val contract: String = "",
    val platform: String = "",
    val contracts: List<ContractModel> = emptyList(),
    val parent: ParentModel = ParentModel(),
    val tags: List<TagModel> = emptyList(),
    val team: List<TeamModel> = emptyList(),
    val description: String = "",
    val message: String = "",
    @SerializedName("open_source")
    val openSource: Boolean = false,
    @SerializedName("started_at")
    val startedAt: Date = Date(),
    @SerializedName("development_status")
    val developmentStatus: String = "",
    @SerializedName("hardware_wallet")
    val hardwareWallet: Boolean = false,
    @SerializedName("proof_type")
    val proofType: String = "",
    @SerializedName("org_structure")
    val orgStructure: String = "",
    @SerializedName("hash_algorithm")
    val hashAlgorithm: String = "",
    val links: LinksModel = LinksModel(),
    @SerializedName("links_extended")
    val linksExtended: List<LinkExtendedModel> = emptyList(),
    val whitepaper: WhitepaperModel = WhitepaperModel(),
    @SerializedName("first_data_at")
    val firstDataAt: Date = Date(),
    @SerializedName("last_data_at")
    val lastDataAt: Date = Date()
)