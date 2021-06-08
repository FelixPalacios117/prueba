package com.guevara.parcial2.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guevara.parcial2.R
import com.guevara.parcial2.data.entity.Pregunta
import com.guevara.parcial2.data.entity.Respuesta
import com.guevara.parcial2.repository.TriviaRepository
import kotlinx.coroutines.launch

class TriviaViewModel(private val repository: TriviaRepository): ViewModel() {
    var respuestas= repository.cargarRespuestas()
    var preguntastexto=MutableLiveData("")
    val puntostexto= MutableLiveData("")
    val respuestaTexto=MutableLiveData("")
    val esCorrecta=MutableLiveData(false)
    val eventoBoton=MutableLiveData("Agregar pregunta")
    private val _actual= MutableLiveData<Int>()
    val actual: LiveData<Int> get()=_actual
    private val formPages:List<Int> =listOf(R.id.mainFragment,R.id.preguntasFragment)
    init{
        _actual.value=0
    }
    fun configurarpreguntas(){
        _actual.apply {
            value=1
        }
    }
    fun getActual()=formPages[actual.value?:0]
    fun agregar(){
        viewModelScope.launch{
            try {
                if(eventoBoton.value.equals("Agregar pregunta")){
                    agregarPregunta()
                    eventoBoton.apply {
                        value="Agregar respuestas"
                    }
                }else{
                    agregarRespuesta()
                }
            }
            catch(e:Exception){

            }
        }
    }
    fun agregarRespuesta() {
        viewModelScope.launch {
            try {
                var id=repository.obtenerMaxIdRespuesta()
                if(id==null){
                    id=0
                }
                repository.agregarRespuesta(id+1,respuestaTexto.value.toString(),esCorrecta.value?:false)
            } catch (e:Exception) {

            } finally {
            }

        }
    }
    fun agregarPregunta(){
        viewModelScope.launch {
            try {
                var id=repository.obtenerMaximoIdPregunta()
                if(id==null){
                    id=0
                }
                repository.agregarPregunta(id+1,preguntastexto.value.toString(),puntostexto.value!!.toInt())
            } catch (e:Exception) {
            } finally {
            }
        }
    }
}