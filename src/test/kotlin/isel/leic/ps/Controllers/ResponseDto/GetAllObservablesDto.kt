package isel.leic.ps.Controllers.ResponseDto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetAllObservablesDto(val properties: InnerProperty) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class InnerProperty(val observableList: Array<InnerObservableListItem>){

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class InnerObservableListItem(val test: Int = 1)
    }


}