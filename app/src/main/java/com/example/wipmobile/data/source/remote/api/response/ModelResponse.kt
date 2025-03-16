package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.source.remote.api.RemoteHost
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

    @SerializedName("user_status")
    val status: UserStatusResponse,

    @SerializedName("battlescribe_unit")
    val battleScribeUnit: BattleScribeUnitResponse?,

    @SerializedName("kill_team")
    val killTem: KillTeamResponse?,

    val groups: List<ModelGroupResponse>?

) {
    fun mapToModel(): Model {
        return Model(
            id =id,
            name =name,
            unitCount = unitCount,
            isTerrain = isTerrain,
            hoursSpent = hoursSpent ?: 0.0,

            status = status.mapToModel(),
            lastImagePath = "${RemoteHost.HOST}$imagePath",

            battleScribeUnit = battleScribeUnit?.mapToModel(),
            killTeam = killTem?.mapToModel(),

            groups = groups?.map { it2 -> it2.mapToModel() }?.toList() ?: emptyList()
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