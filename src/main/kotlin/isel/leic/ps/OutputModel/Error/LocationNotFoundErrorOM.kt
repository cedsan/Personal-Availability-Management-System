package isel.leic.ps.OutputModel.Error

import com.google.code.siren4j.annotations.Siren4JEntity

class LocationNotFoundErrorOM {
    val type = "http://localhost::8080/probs/no-location-submitted"
    val title = "No submitted location."
    val detail = "The observable has no location yet submitted. Try again later"
}