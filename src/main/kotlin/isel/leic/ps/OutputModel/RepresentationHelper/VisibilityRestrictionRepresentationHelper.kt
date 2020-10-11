package isel.leic.ps.OutputModel.RepresentationHelper

import com.google.code.siren4j.component.Action
import com.google.code.siren4j.component.builder.ActionBuilder
import com.google.code.siren4j.component.builder.FieldBuilder
import com.google.code.siren4j.component.builder.LinkBuilder
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.meta.FieldType
import isel.leic.ps.SystemConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType


class VisibilityRestrictionRepresentationHelper {
    companion object {

        val GET_OBSERVABLE_OBSERVER_VISIB_RESTRICTION_SELF_LINK = SystemConfig.HOST_HOME+"/observable/%d/observer/%d/restriction"
        val OBSERVABLE_GLOBAL_VIS_RESTRICTION_LINK = SystemConfig.HOST_HOME+"/observable/%d/global-vis-restriction-config"

        fun  getVisibilityRestrictionOfObservableOnObserverSelfLink(observableID: Int, observerID: Int) = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(GET_OBSERVABLE_OBSERVER_VISIB_RESTRICTION_SELF_LINK, observableID, observerID)).build()
        fun getObservableGlobalVisibilityRestrictionLink(observableId: Int) = LinkBuilder.newInstance().setRelationship("self").setHref(String.format(OBSERVABLE_GLOBAL_VIS_RESTRICTION_LINK, observableId)).build()

        fun  getUpdateObservableGlobalVisibilityRestrcitionAction(observableID: Int): Action? =
                ActionBuilder.newInstance()
                        .setMethod(ActionImpl.Method.PUT)
                        .setHref(String.format(OBSERVABLE_GLOBAL_VIS_RESTRICTION_LINK, observableID))
                        .setName("update-global-visibility-restriction")
                        .setTitle("Update Global Visibility Restriction")
                        .setType(MediaType.APPLICATION_JSON_VALUE)
                        .addFields(
                                arrayListOf(
                                        FieldBuilder.newInstance().setRequired(true).setName("canSeeLocation").setType(FieldType.CHECKBOX).build(),
                                        FieldBuilder.newInstance().setRequired(true).setName("canSeeWhenAvailable").setType(FieldType.CHECKBOX).build(),
                                        FieldBuilder.newInstance().setRequired(true).setName("canSeeSchedule").setType(FieldType.CHECKBOX).build()
                                )
                        )
                        .build()

        fun  getUpdateObservableVisibilityRestrcitionOnObservableAction(observableID: Int, observerID: Int): Action? =
                ActionBuilder.newInstance()
                        .setMethod(ActionImpl.Method.PUT)
                        .setHref(String.format(GET_OBSERVABLE_OBSERVER_VISIB_RESTRICTION_SELF_LINK, observableID, observerID))
                        .setName("update-visibility-restriction")
                        .setTitle("Update Observer Visibility Restriction")
                        .setType(MediaType.APPLICATION_JSON_VALUE)
                        .addFields(
                                arrayListOf(
                                        FieldBuilder.newInstance().setRequired(true).setName("canSeeLocation").setType(FieldType.CHECKBOX).build(),
                                        FieldBuilder.newInstance().setRequired(true).setName("canSeeWhenAvailable").setType(FieldType.CHECKBOX).build(),
                                        FieldBuilder.newInstance().setRequired(true).setName("canSeeSchedule").setType(FieldType.CHECKBOX).build()
                                )
                        )
                        .build()

    }
}