package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.Model
import com.google.gson.annotations.SerializedName

class ModelResponse(
    val id: Int,
    val name: String,
    @SerializedName("unit_count")
    val unitCount: Int,
    @SerializedName("terrain")
    val isTerrain: Boolean,
    @SerializedName("get_last_image_url")
    val imagePath: String?,

    @SerializedName("hours_spent")
    val hoursSpent: Double?,

    @SerializedName("user_status_id")
    val statusId: Int,
    @SerializedName("user_status_name")
    val statusName: String,

    @SerializedName("battlescribe_unit_id")
    val battleScribeUnitId: Int?,
    @SerializedName("battlescribe_unit_name")
    val battleScribeUnitName: String?,

    @SerializedName("kill_team_id")
    val killTemId: Int?,
    @SerializedName("kill_team_name")
    val killTemName: String?,

    val groups: List<ModelGroupResponse>?

) {
    fun mapToModel(): Model {
        return Model(
            id =id,
            name =name,
            unitCount = unitCount,
            isTerrain = isTerrain,
            hoursSpent = hoursSpent ?: 0.0,

            statusId = statusId,
            statusName = statusName,
            lastImagePath = "http://10.0.2.2:8000$imagePath",
            battleScribeUnitId = battleScribeUnitId,
            battleScribeUnitName = battleScribeUnitName,
            killTeamId = killTemId,
            killTeamName = killTemName,

            groups = groups?.map { it2 -> it2.toModelGroup() }?.toList() ?: emptyList()
        )
    }
}


/**
 ,
 *

 *  "groups": [
 *    {
 *      "id": 5,
 *      "name": "Некроны"
 *    }
 *  ],

 *  "hours_spent": 3.3166666666666664
 */