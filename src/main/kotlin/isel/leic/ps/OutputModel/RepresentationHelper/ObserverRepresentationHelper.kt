package isel.leic.ps.OutputModel.RepresentationHelper

import com.google.code.siren4j.component.Action
import com.google.code.siren4j.component.Link
import com.google.code.siren4j.component.builder.ActionBuilder
import com.google.code.siren4j.component.builder.FieldBuilder
import com.google.code.siren4j.component.builder.LinkBuilder
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.meta.FieldType
import isel.leic.ps.Domain.Subscription.SubscriptionEnum
import isel.leic.ps.OutputModel.ObserverOM
import isel.leic.ps.OutputModel.ScheduleOM
import isel.leic.ps.OutputModel.SubscriptionOM
import isel.leic.ps.OutputModel.WeekDaySubscriptionOM
import isel.leic.ps.SystemConfig
import org.springframework.http.MediaType

class ObserverRepresentationHelper {

    companion object {
        // Templates
        val CREATE_OBSERVER_ENDPOINT = SystemConfig.HOST_HOME+"/observer"
        val GET_OBSERVER_SELF_LINK = SystemConfig.HOST_HOME+"/observer/%d"
        val GET_OBSERVER_OBSERVED_ENDPOINT = SystemConfig.HOST_HOME+"/observer/%d/observable/%d"
        val GET_OBSERVER_ALL_OBSERVABLES_ENPOINT = SystemConfig.HOST_HOME+"/observer/%d/observable"
        val GET_OBSERVER_SUBSCRIPTIONS_TO_OBSERVABLE_ENDPOINT = SystemConfig.HOST_HOME+"/observer/%d/observable/%d/subscription"
        val GET_OBSERVER_LOCATION_TO_OBSERVABLE_ENDPOINT = SystemConfig.HOST_HOME+"/observer/%d/observable/%d/location"
        val GET_OBSERVER_SCHEDULE_TO_OBSERVABLE_ENDPOINT = SystemConfig.HOST_HOME+"/observer/%d/observable/%d/schedule"
        val GET_OBSERVER_SUBSCRIPTION_SELF_LINK = SystemConfig.HOST_HOME+"/observer/%d/observable/%d/subscription/%d?type=%s"

        val GET_OBSERVER_OBSERVABLE_SCHEDULE_BY_ID = SystemConfig.HOST_HOME+"/observer/%d/observable/%d/schedule/%d?type=%s"
        val OBSERVER_REGISTRATION_TOKEN_ENDPOINT =  SystemConfig.HOST_HOME +"/%d/notification/token"

        val updateSubscriptionActionAccordingToTypeMapper: HashMap<String, (Int, Int, Int, String) -> Action>
        = HashMap()

        init {
            updateSubscriptionActionAccordingToTypeMapper.put(SubscriptionEnum.WEEKDAY.label, this::getWeekdayUpdateAction)
        }

        // Link relations
        val GET_OBSERVABLES_REL = "observables"

        // Links
        fun getObservableAssociatedWithObserverSelfLink(observerID: Int, observableID: Int): Link = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVER_OBSERVED_ENDPOINT, observerID, observableID)).build()

        fun getObserverAllObservablesWithVisibilityAssociation(observerID: Int, linkRel: String = "self"): Link = LinkBuilder.newInstance().setRelationship(linkRel).setHref(String.format(GET_OBSERVER_ALL_OBSERVABLES_ENPOINT, observerID)).build()

        fun getObservableFromObserverSubscriptionLink(observerID: Int, observableID: Int, isSelf: Boolean = false): Link =
                LinkBuilder.newInstance().setRelationship(if(isSelf) "self" else "subscriptions").setHref(String.format(GET_OBSERVER_SUBSCRIPTIONS_TO_OBSERVABLE_ENDPOINT, observerID, observableID)).build()

        fun getObservableFromObserverSchedulesLink(observerID: Int, observableID: Int, isSelf: Boolean = false): Link =
                LinkBuilder.newInstance().setRelationship(if(isSelf) "self" else "schedules").setHref(String.format(GET_OBSERVER_SCHEDULE_TO_OBSERVABLE_ENDPOINT, observerID, observableID)).build()

        fun getObservableFromObserverLocationLink(observerID: Int, observableID: Int, isSelf: Boolean = false): Link =
                LinkBuilder.newInstance().setRelationship(if(isSelf) "self" else "location").setHref(String.format(GET_OBSERVER_LOCATION_TO_OBSERVABLE_ENDPOINT, observerID, observableID)).build()

        fun  getObserverSubscriptionToObservableSelfLink(observerID: Any, observableID: Int, sub: SubscriptionOM): Link? {
            return LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVER_SUBSCRIPTION_SELF_LINK, observerID, observableID, sub.id, sub.type)).build()
        }

        fun getObservableFromObserverScheduleLink(observerID: Int, observableID: Int, schedule: ScheduleOM): Link? {
            return LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVER_OBSERVABLE_SCHEDULE_BY_ID, observerID, observableID, schedule.id, schedule.type)).build()
        }
        // Actions


        fun getAddNewObserverActionTogetAllObserverAction(): Action =
            ActionBuilder.newInstance()
                    .setMethod(ActionImpl.Method.POST)
                    .setHref(CREATE_OBSERVER_ENDPOINT)
                    .setName("create-observer")
                    .setTitle("Create observer")
                    .setType(MediaType.APPLICATION_JSON_VALUE)
                    .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("name").setType(FieldType.TEXT).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("number").setType(FieldType.NUMBER).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("email").setType(FieldType.TEXT).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("avatar_url").setType(FieldType.TEXT).build()))
                    .build()

        fun getObserverSelfLink(o: ObserverOM): Link = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVER_SELF_LINK, o.id)).build()
        fun getObservableFromObserverSubscriptionCollectionRemoveAllAction(observerID: Int, observableID: Int): Action? = ActionBuilder.newInstance()
                .setMethod(ActionImpl.Method.DELETE)
                .setHref(String.format(GET_OBSERVER_SUBSCRIPTIONS_TO_OBSERVABLE_ENDPOINT, observerID, observableID))
                .setName("remove-all-subscriptions")
                .setTitle("Remove All Subscriptions")
                .build()

        fun getObservableFromObserverSubscriptionCollectionAddNewAction(observerID: Int, observableID: Int): ArrayList<Action> = arrayListOf(
                getWeekdayCreateAction(observerID, observableID), getDefaultSubscriptionCreateAction(observerID, observableID))

        fun  getSubscriptionOfObserverOnObservableActions(observerID: Int, observableID: Int, subscriptionID: Int, subscriptionType: String): MutableCollection<Action>? {
            val actions = arrayListOf(
                    ActionBuilder.newInstance()
                            .setMethod(ActionImpl.Method.DELETE)
                            .setHref(String.format(GET_OBSERVER_SUBSCRIPTION_SELF_LINK, observerID, observableID, subscriptionID, subscriptionType))
                            .setName("remove-subscription")
                            .setTitle("Remove Subscription")
                            .build())
            if((subscriptionType == SubscriptionEnum.DEFAULT.label).not())
                actions.add(updateSubscriptionActionAccordingToTypeMapper[subscriptionType]!!(observerID, observableID, subscriptionID, subscriptionType))
            return actions
        }

        private fun getWeekdayUpdateAction(observerID: Int, observableID: Int, subscriptionID: Int, subscriptionType: String): Action {
            return ActionBuilder.newInstance()
                    .setMethod(ActionImpl.Method.PUT)
                    .setHref(String.format(GET_OBSERVER_SUBSCRIPTION_SELF_LINK, observerID, observableID, subscriptionID, subscriptionType))
                    .setName("update-subscription")
                    .setTitle("Update Subscription")
                    .setType(MediaType.APPLICATION_JSON_VALUE)
                    .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("dayOfWeek").setType(FieldType.NUMBER).setMin(0).setMax(7).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("startWatchHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("startWatchMinutes").setType(FieldType.NUMBER).setMin(0).setMax(59).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("endWatchHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("type").setType(FieldType.TEXT).setValue(subscriptionType).build(),
                            FieldBuilder.newInstance().setRequired(true).setName("endWatchMinutes").setType(FieldType.NUMBER).setMin(0).setMax(59).build()))
                    .build()
        }

        private fun getWeekdayCreateAction(observerID: Int, observableID: Int): Action = ActionBuilder.newInstance()
                .setMethod(ActionImpl.Method.POST)
                .setHref(String.format(GET_OBSERVER_SUBSCRIPTIONS_TO_OBSERVABLE_ENDPOINT, observerID, observableID))
                .setName("create-weekday-subscription")
                .setTitle("Create Weekday Subscription")
                .setType(MediaType.APPLICATION_JSON_VALUE)
                .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("dayOfWeek").setType(FieldType.NUMBER).setMin(0).setMax(7).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("startWatchHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("startWatchMinutes").setType(FieldType.NUMBER).setMin(0).setMax(59).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("endWatchHour").setType(FieldType.NUMBER).setMin(0).setMax(23).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("type").setType(FieldType.TEXT).setValue(SubscriptionEnum.WEEKDAY.label).build(),
                        FieldBuilder.newInstance().setRequired(true).setName("endWatchMinutes").setType(FieldType.NUMBER).setMin(0).setMax(59).build()))
                .build()

        private fun getDefaultSubscriptionCreateAction(observerID: Int, observableID: Int) = ActionBuilder.newInstance()
                .setMethod(ActionImpl.Method.POST)
                .setHref(String.format(GET_OBSERVER_SUBSCRIPTIONS_TO_OBSERVABLE_ENDPOINT, observerID, observableID))
                .setName("create-default-subscription")
                .setTitle("Create default Subscription")
                .setType(MediaType.APPLICATION_JSON_VALUE)
                .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("type").setType(FieldType.TEXT).setValue(SubscriptionEnum.DEFAULT.label).build()))
                .build()

        fun getObserverRegistrationTokenPutAction(observerID: Int) = ActionBuilder.newInstance()
            .setMethod(ActionImpl.Method.PUT)
                    .setHref(String.format(OBSERVER_REGISTRATION_TOKEN_ENDPOINT, observerID))
                    .setName("update-registration-token")
                    .setTitle("Update Registration Token")
                    .setType(MediaType.APPLICATION_JSON_VALUE)
                    .addFields(arrayListOf(FieldBuilder.newInstance().setRequired(true).setName("user_id").setType(FieldType.NUMBER).build(),
                                           FieldBuilder.newInstance().setRequired(true).setName("registration_token").setType(FieldType.TEXT).build()))
                    .build()
    }
}