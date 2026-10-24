package com.lucasdias.gametrackr.feature.app.help

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class HelpFaq(
    @param:StringRes val questionRes: Int,
    @param:StringRes val answerRes: Int,
) {
    ADD_GAME(R.string.help_faq_1_question, R.string.help_faq_1_answer),
    PLAYTIME(R.string.help_faq_2_question, R.string.help_faq_2_answer),
    FRIENDS(R.string.help_faq_3_question, R.string.help_faq_3_answer),
    PRIVACY(R.string.help_faq_4_question, R.string.help_faq_4_answer),
}
