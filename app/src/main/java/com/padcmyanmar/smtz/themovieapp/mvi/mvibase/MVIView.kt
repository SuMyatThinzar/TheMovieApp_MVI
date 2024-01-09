package com.padcmyanmar.smtz.themovieapp.mvi.mvibase

interface MVIView<S : MVIState> {
    fun render(state: S)            //MainState:MVIState , MovieDetailsState:MVIState  //State ရဲ.Type က Screen ၁ ခုချင်းစီအလိုက် ပြောင်း‌နေမှာ
}