package com.example.diseaseprediction.api.responses

import com.google.gson.annotations.SerializedName

data class DetailItemResponse(

    @field:SerializedName("DetailItemResponse")
    val detailItemResponse: List<DetailItemResponseItem>
)

data class DiseasesDrugsItem(

    @field:SerializedName("drugs_slug")
    val drugsSlug: String? = null,

    @field:SerializedName("drugs_name")
    val drugsName: String? = null
)

data class DetailItemResponseItem(

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("diseases_drugs")
    val diseasesDrugs: List<DiseasesDrugsItem?>? = null
)
