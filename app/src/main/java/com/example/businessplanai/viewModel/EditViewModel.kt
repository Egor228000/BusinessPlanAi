import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.BusinessEnity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EditViewModel(application: Application) : AndroidViewModel(application) {

    private val businessDao = AppDatabase.getInstance(application).businessDao()

    private val _business = MutableStateFlow<BusinessEnity?>(null)
    val business: StateFlow<BusinessEnity?> = _business


    // Временные значения для редактирования
    var editedTitle = mutableStateOf("")
    var editedDescription = mutableStateOf("")

    fun loadBusinessById(id: Int?) {
        viewModelScope.launch {
            val result = businessDao.getId(id)
            _business.value = result
            result?.let {
                editedTitle.value = it.title
                editedDescription.value = it.description
            }
        }
    }

    fun saveChanges() {
        val current = _business.value ?: return
        val updated = current.copy(
            title = editedTitle.value,
            description = editedDescription.value
        )

        viewModelScope.launch {
            businessDao.insert(updated)
        }
    }
}