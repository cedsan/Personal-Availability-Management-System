package isel.leic.ps.OutputModel.RepresentationHelper

import com.google.code.siren4j.component.Action
import com.google.code.siren4j.component.Link
import com.google.code.siren4j.component.builder.ActionBuilder
import com.google.code.siren4j.component.builder.FieldBuilder
import com.google.code.siren4j.component.builder.LinkBuilder
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.meta.FieldType
import isel.leic.ps.Domain.Schedule.ScheduleEnum
import isel.leic.ps.OutputModel.LocationOM
import isel.leic.ps.OutputModel.ObservableOM
import isel.leic.ps.OutputModel.ObserverOM
import isel.leic.ps.OutputModel.ScheduleOM
import isel.leic.ps.SystemConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment


class ObservableRepresentationHelper {
    companion object {



        val CREATE_OBSERVABLE_ENDPOINT = SystemConfig.HOST_HOME+"/observable"
        val GET_OBSERVABLE_SELF_LINK = SystemConfig.HOST_HOME+"/observable/%d"
        val GET_OBSERVABLE_OBSERVERS_ASSOCIATED = SystemConfig.HOST_HOME+"/observable/%d/observer"
        val GET_OBSERVABLE_SCHEDULES_ASSOCIATED = SystemConfig.HOST_HOME+"/observable/%d/schedule"
        val GET_OBSERVABLE_LOCATION_ASSOCIATED = SystemConfig.HOST_HOME+"/observable/%d/location"
        val GET_OBSERVABLE_SCHEDULE_SELF = SystemConfig.HOST_HOME+"/observable/%d/schedule/%d?type=%s"
        val GET_OBSERVABLE_OBSERVER_DETAIL_SELF_LINK = SystemConfig.HOST_HOME+"/observable/%d/observer/%d"
        val OBSERVABLE_TURN_AVAILABLE_LINK = SystemConfig.HOST_HOME+"/observable/%d/available"
        val OBSERVABLE_TURN_UNAVAILABLE_LINK = SystemConfig.HOST_HOME+"/observable/%d/unavailable"
        val OBSERVABLE_ENTER_LOCATION_LINK = SystemConfig.HOST_HOME+"/observable/%d/entered-location"
        val OBSERVABLE_GLOBAL_VIS_RESTRICTION_LINK = SystemConfig.HOST_HOME+"/observable/%d/global-vis-restriction-config"
        val OBSERVABLE_VISIBILITY_RESTRICTION_ON_OBSERVER_LINK = SystemConfig.HOST_HOME+"/observable/%d/observer/%d/restriction"

        val updateScheduleActionAccordingToScheduleTypeMapper: HashMap<ScheduleEnum, (Int, Int) -> Action> = HashMap()

        init {
            updateScheduleActionAccordingToScheduleTypeMapper.put(ScheduleEnum.WEEKDAY, this::getWeeklyScheduleUpdateAction)
        }



        fun  getAddNewObservableAction(): Action? =
                ActionBuilder.newInstance()
                        .setMethod(ActionImpl.Method.POST)
                        .setHref(CREATE_OBSERVABLE_ENDPOINT)
                        .setName("create-observable")
                        .setTitle("Create observable")
                        .setType(MediaType.APPLICATION_JSON_VALUE)
                        .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("name").setType(FieldType.TEXT).build(),
                                FieldBuilder.newInstance().setRequired(true).setName("number").setType(FieldType.NUMBER).build(),
                                FieldBuilder.newInstance().setRequired(true).setName("email").setType(FieldType.TEXT).build(),
                                FieldBuilder.newInstance().setRequired(true).setName("avatar_url").setType(FieldType.TEXT).build()))
                        .build()

        fun  getObservableLinks(observableOM: ObservableOM): MutableCollection<Link>? = arrayListOf(
                LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_SELF_LINK, observableOM.id)).build(),
                LinkBuilder.newInstance().setRelationship("observers").setHref(String.format(GET_OBSERVABLE_OBSERVERS_ASSOCIATED, observableOM.id)).build(),
                LinkBuilder.newInstance().setRelationship("schedules").setHref(String.format(GET_OBSERVABLE_SCHEDULES_ASSOCIATED, observableOM.id)).build(),
                LinkBuilder.newInstance().setRelationship("location").setHref(String.format(GET_OBSERVABLE_LOCATION_ASSOCIATED, observableOM.id)).build(),
                LinkBuilder.newInstance().setRelationship("global-visibility-restriction").setHref(String.format(OBSERVABLE_GLOBAL_VIS_RESTRICTION_LINK, observableOM.id)).build())


        fun  getSendObservableLocationAction(locationOM: LocationOM, observableId: Int): MutableCollection<Action>? = arrayListOf(ActionBuilder.newInstance()
        .setMethod(ActionImpl.Method.POST)
        .setHref(String.format(GET_OBSERVABLE_LOCATION_ASSOCIATED, observableId))
        .setName("update-location")
        .setTitle("Update location")
        .setType(MediaType.APPLICATION_JSON_VALUE)
        .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("latitude").setType(FieldType.NUMBER).build(),
        FieldBuilder.newInstance().setRequired(true).setName("longitude").setType(FieldType.NUMBER).build()))
        .build())

        fun getObservableScheduleRemoveAllAction(observableId: Int) = ActionBuilder.newInstance()
                .setMethod(ActionImpl.Method.DELETE)
                .setHref(String.format(GET_OBSERVABLE_SCHEDULES_ASSOCIATED, observableId))
                .setName("remove-all-schedules")
                .setTitle("Remove All Schedules")
                .build()

        fun  getObservableAddNewWeeklyScheduleAction(id: Int): Action = ActionBuilder.newInstance()
                .setMethod(ActionImpl.Method.POST)
                .setHref(String.format(GET_OBSERVABLE_SCHEDULES_ASSOCIATED, id))
                .setName("add-schedule")
                .setTitle("Add Schedule")
                .setType(MediaType.APPLICATION_JSON_VALUE)
                .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("dayOfWeek").setType(FieldType.NUMBER).setMin(0).setMax(7).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("startAvailabilityHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("startAvailabilityMinute").setType(FieldType.NUMBER).setMin(0).setMax(59).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("endAvailabilityHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("endAvailabilityMinute").setType(FieldType.NUMBER).setMin(0).setMax(59).build(),
                        FieldBuilder.newInstance().setRequired(false).setName("availabilityLocationLatitude").setType(FieldType.NUMBER).build(),
                        FieldBuilder.newInstance().setRequired(false).setName("availabilityLocationLongitude").setType(FieldType.NUMBER).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("type").setType(FieldType.TEXT).setValue(ScheduleEnum.WEEKDAY.label).build()))
                .build()

        fun getObservableScheduleRemoveByIdAction(id: Int, id1: Int, scheduleEnumType: ScheduleEnum): Action = ActionBuilder.newInstance()
                .setMethod(ActionImpl.Method.DELETE)
                .setHref(String.format(GET_OBSERVABLE_SCHEDULE_SELF, id, id1, scheduleEnumType.label))
                .setName("remove-weeklyschedule")
                .setTitle("Remove Weekly Schedule")
                .build()

        fun getWeeklyScheduleUpdateAction(observableId: Int, scheduleId: Int): Action = ActionBuilder.newInstance()
                .setMethod(ActionImpl.Method.PUT)
                .setHref(String.format(GET_OBSERVABLE_SCHEDULE_SELF, observableId, scheduleId, ScheduleEnum.WEEKDAY.label))
                .setName("update-schedule")
                .setTitle("Update Schedule")
                .setType(MediaType.APPLICATION_JSON_VALUE)
                .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("dayOfWeek").setType(FieldType.NUMBER).setMin(0).setMax(7).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("startAvailabilityHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("startAvailabilityMinute").setType(FieldType.NUMBER).setMin(0).setMax(59).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("endAvailabilityHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("endAvailabilityMinute").setType(FieldType.NUMBER).setMin(0).setMax(59).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("type").setType(FieldType.TEXT).setValue(ScheduleEnum.WEEKDAY.label).build()))
                .build()

        fun getObservableSelfLink(o: ObservableOM): Link {
            return LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_SELF_LINK, o.id)).build()
        }
        fun getObservableSelfLink(observableId: Int): Link {
            return LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_SELF_LINK, observableId)).build()
        }
        fun getObservableScheduleSelfLink(observableId: Int, schedule: ScheduleOM): Link? = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_SCHEDULE_SELF, observableId, schedule.id, schedule.type)).build()
        fun getObservableScheduleCollectionSelfLink(observableId: Int) = LinkBuilder.newInstance().setRelationship("schedules").setHref(String.format(GET_OBSERVABLE_SCHEDULES_ASSOCIATED, observableId)).build()
        fun getObservableScheduleAddAction(id: Int): Collection<Action> = arrayListOf(getObservableAddNewWeeklyScheduleAction(id))
        fun getObservableScheduleUpdateAction(id: Int, scheduleId: Int, scheduleEnumType: ScheduleEnum): Action?  {
            return updateScheduleActionAccordingToScheduleTypeMapper[scheduleEnumType]!!(id, scheduleId)
        }

        fun getObservableBecameAvailableAction(observableId: Int): Action {
            return ActionBuilder.newInstance()
                    .setMethod(ActionImpl.Method.POST)
                    .setHref(String.format(OBSERVABLE_TURN_AVAILABLE_LINK, observableId))
                    .setName("make-available")
                    .setTitle("Make Observable Available")
                    .build()
        }

        fun getObservableBecameUnavailableAction(observableId: Int): Action {
            return ActionBuilder.newInstance()
                    .setMethod(ActionImpl.Method.POST)
                    .setHref(String.format(OBSERVABLE_TURN_UNAVAILABLE_LINK, observableId))
                    .setName("make-unavailable")
                    .setTitle("Make Observable Unavailable")
                    .build()
        }

        fun getObservableEnteredLocationAction(observableId: Int): Action {
            return ActionBuilder.newInstance()
                    .setMethod(ActionImpl.Method.POST)
                    .setHref(String.format(OBSERVABLE_ENTER_LOCATION_LINK, observableId))
                    .setName("enter-scheduled-location")
                    .setTitle("Enter Scheduled Location")
                    .build()
        }


        fun getObservableGlobalVisibilityRestrictionLink(observableId: Int) = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(OBSERVABLE_GLOBAL_VIS_RESTRICTION_LINK, observableId)).build()
        fun getObservableObserverSelfLink(observableId: Int, observerOM: ObserverOM) = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_OBSERVER_DETAIL_SELF_LINK, observableId, observerOM.id)).build()
        fun getObservableObserverSelfLink(observableId: Int, observerID: Int) = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_OBSERVER_DETAIL_SELF_LINK, observableId, observerID)).build()
        fun getObservableActions(observableId: Int): MutableCollection<Action>? {
            return arrayListOf(getObservableBecameAvailableAction(observableId),
                               getObservableBecameUnavailableAction(observableId),
                               getObservableEnteredLocationAction(observableId))
        }

        fun getVisibilityRestrictionOfObservableOnObserverLink(observableID: Int, observerID: Int): Link? {
            return LinkBuilder.newInstance().setRelationship("vis_restriction").setHref(String.format(OBSERVABLE_VISIBILITY_RESTRICTION_ON_OBSERVER_LINK, observableID, observerID)).build()
        }

        fun  getAllObservableSelfLink(): Link = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(CREATE_OBSERVABLE_ENDPOINT)).build()
        fun getCollectionObserversOfObservableSelfLink(id: Int): Link {
            return LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_OBSERVERS_ASSOCIATED, id)).build()
        }

        fun getObservableLocationSelfLink(id: Int): Link? {
            return LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_LOCATION_ASSOCIATED, id)).build()
        }
    }
}