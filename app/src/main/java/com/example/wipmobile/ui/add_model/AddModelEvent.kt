package com.example.wipmobile.ui.add_model

import java.util.Date

sealed class AddModelEvent {
    class SaveModel(val successCallback: () -> Unit): AddModelEvent()
    class NameChanged(val name: String): AddModelEvent()
    class UnitCountChanged(val count: Int): AddModelEvent()
    class TerrainChanged(val isTerrain: Boolean): AddModelEvent()
    class KillTeamChanged(val killTeamName: String): AddModelEvent()
    class BattleScribeChanged(val bsName: String): AddModelEvent()
    class StatusChanged(val statusName: String): AddModelEvent()
    class BuyDateChanged(val date: Date): AddModelEvent()
    class GroupsChanged(val groups: Array<String>): AddModelEvent()
    data object ClearError: AddModelEvent()
}