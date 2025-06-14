package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

data class MarkerDetailState(
    val preloading: Boolean = true,
    val marker: MarkerUI? = null,
    val folders: List<FolderUI> = emptyList(),
    val showAddTagDialog: Boolean = false,
    val showRemoveTagDialog: Boolean = false,
    val removeTag: String? = null,
    val editing: Boolean = false
) {

    fun requireRemoveTag() = requireNotNull(removeTag)

}