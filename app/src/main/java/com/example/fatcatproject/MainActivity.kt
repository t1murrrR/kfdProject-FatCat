package com.example.fatcatproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fatcatproject.PlayVSBotViewModel.info.AskCard
import com.example.fatcatproject.PlayVSBotViewModel.info.Bot
import com.example.fatcatproject.PlayVSBotViewModel.info.BotCards
import com.example.fatcatproject.PlayVSBotViewModel.info.BotRankCards
import com.example.fatcatproject.PlayVSBotViewModel.info.ListTableCount
import com.example.fatcatproject.PlayVSBotViewModel.info.ListTableRanks
import com.example.fatcatproject.PlayVSBotViewModel.info.PlayerRankCards
import com.example.fatcatproject.PlayVSBotViewModel.info.PlayerСards
import com.example.fatcatproject.PlayVSBotViewModel.info.botFound
import com.example.fatcatproject.PlayVSBotViewModel.info.count
import com.example.fatcatproject.PlayVSBotViewModel.info.player1
import com.example.fatcatproject.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {

    lateinit var binding: ActivityMainBinding

    val viewModel: PlayVSBotViewModel by viewModels()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun ShowImage(place: ImageView, i: Int) {
        place.setImageResource(i)
        place.visibility = View.VISIBLE
    }

    fun ShowCardsIfNotFound(placeList: List<ImageView>, imageList: List<Int>) {
        if (PlayerRankCards[PlayerСards.size - 1] in ListTableRanks && PlayerRankCards[PlayerСards.size - 1] != 0) {
            for (j in 0..12) {
                if (ListTableRanks[j] == PlayerRankCards[PlayerСards.size - 1]) {
                    if (PlayerRankCards[PlayerСards.size - 1] == ListTableRanks[j]) {
                        ShowImage(
                            placeList[4 * j + ListTableCount[j]],
                            imageList[PlayerСards[PlayerСards.size - 1] - 1]
                        )
                        ListTableCount[j] += 1
                    }
                }
            }
        } else {
            if (PlayerRankCards[PlayerСards.size - 1] != 0) {
                for (j in 0..12) {
                    if (ListTableRanks[j] == 0) {
                        ShowImage(
                            placeList[4 * j],
                            imageList[PlayerСards[PlayerСards.size - 1] - 1]
                        )
                        ListTableRanks[j] = PlayerRankCards[PlayerСards.size - 1]
                        ListTableCount[j] = 1
                        break
                    }
                }
            }
        }
    }

    fun ShowCardsIfFound(placeList: List<ImageView>, imageList: List<Int>) {
        for (i in 0..12) {
            if (PlayerRankCards[PlayerСards.size - 1] == ListTableRanks[i] && ListTableRanks[i] != 0) {
                ListTableCount[i] = 0
                break
            }
        }
        for (j in 0..12) {
            if (ListTableRanks[j] == PlayerRankCards[PlayerСards.size - 1] && ListTableRanks[j] != 0) {
                for (i in 0..<PlayerСards.size) {
                    if (PlayerRankCards[i] == ListTableRanks[j]) {
                        ShowImage(
                            placeList[4 * j + ListTableCount[j]],
                            imageList[PlayerСards[i] - 1]
                        )
                        ListTableCount[j] += 1
                    }
                }
            }
        }
    }

    fun HideCardsIfFour(player: Player, placeList: List<ImageView>) {
        for (i in (2..14)) {
            if (player.RankCards.count { it == i } == 4) {
                for (j in 0..12) {
                    if (ListTableRanks[j] == i) {
                        ListTableCount[j] = 0
                        ListTableRanks[j] = 0
                        for (k in 0..3)
                            placeList[4 * j + k].visibility = View.INVISIBLE
                    }
                }
                player.CountCard -= 4 //количество карт игрока уменьшилось на 4
                player.Points += 1
                for (j in (0..<player.RankCards.size)) { //зануляем эти карты
                    if (player.RankCards[j] == i) {
                        player.RankCards[j] = 0
                        player.PlayerCards[j] = 0
                    }
                }
                binding.textPoints.text = "Player ${player.Points} points"
            }
        }
    }

    fun BotIfFourCards(player: Player) {
        for (i in (2..14)) {
            if (player.RankCards.count { it == i } == 4) {
                player.CountCard -= 4 //количество карт игрока уменьшилось на 4
                player.Points += 1
                for (j in (0..<player.RankCards.size)) { //зануляем эти карты
                    if (player.RankCards[j] == i) {
                        player.RankCards[j] = 0
                        player.PlayerCards[j] = 0
                    }
                }
            }
            binding.textBotPoints.text = "Bot ${Bot.Points} points"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onStart()
        setContentView(binding.root)

        val placeList: List<ImageView> = mutableListOf(
            binding.place11,
            binding.place12,
            binding.place13,
            binding.place14,
            binding.place21,
            binding.place22,
            binding.place23,
            binding.place24,
            binding.place31,
            binding.place32,
            binding.place33,
            binding.place34,
            binding.place41,
            binding.place42,
            binding.place43,
            binding.place44,
            binding.place51,
            binding.place52,
            binding.place53,
            binding.place54,
            binding.place61,
            binding.place62,
            binding.place63,
            binding.place64,
            binding.place71,
            binding.place72,
            binding.place73,
            binding.place74,
            binding.place81,
            binding.place82,
            binding.place83,
            binding.place84,
            binding.place91,
            binding.place92,
            binding.place93,
            binding.place94,
            binding.place101,
            binding.place102,
            binding.place103,
            binding.place104,
            binding.place111,
            binding.place112,
            binding.place113,
            binding.place114,
            binding.place121,
            binding.place122,
            binding.place123,
            binding.place124,
            binding.place131,
            binding.place132,
            binding.place133,
            binding.place134
        )
        val imageList: List<Int> = listOf(
            R.drawable.card_2_h,
            R.drawable.card_2_d,
            R.drawable.card_2_c,
            R.drawable.card_2_s,
            R.drawable.card_3_h,
            R.drawable.card_3_d,
            R.drawable.card_3_c,
            R.drawable.card_3_s,
            R.drawable.card_4_h,
            R.drawable.card_4_d,
            R.drawable.card_4_c,
            R.drawable.card_4_s,
            R.drawable.card_5_h,
            R.drawable.card_5_d,
            R.drawable.card_5_c,
            R.drawable.card_5_s,
            R.drawable.card_6_h,
            R.drawable.card_6_d,
            R.drawable.card_6_c,
            R.drawable.card_6_s,
            R.drawable.card_7_h,
            R.drawable.card_7_d,
            R.drawable.card_7_c,
            R.drawable.card_7_s,
            R.drawable.card_8_h,
            R.drawable.card_8_d,
            R.drawable.card_8_c,
            R.drawable.card_8_s,
            R.drawable.card_9_h,
            R.drawable.card_9_d,
            R.drawable.card_9_c,
            R.drawable.card_9_s,
            R.drawable.card_10_h,
            R.drawable.card_10_d,
            R.drawable.card_10_c,
            R.drawable.card_10_s,
            R.drawable.card_j_h,
            R.drawable.card_j_d,
            R.drawable.card_j_c,
            R.drawable.card_j_s,
            R.drawable.card_q_h,
            R.drawable.card_q_d,
            R.drawable.card_q_c,
            R.drawable.card_q_s,
            R.drawable.card_k_h,
            R.drawable.card_k_d,
            R.drawable.card_k_c,
            R.drawable.card_k_s,
            R.drawable.card_a_h,
            R.drawable.card_a_d,
            R.drawable.card_a_c,
            R.drawable.card_a_s
        )
        val buttonList: List<Button> = listOf(
            binding.buttonPlace1,
            binding.buttonPlace2,
            binding.buttonPlace3,
            binding.buttonPlace4,
            binding.buttonPlace5,
            binding.buttonPlace6,
            binding.buttonPlace7,
            binding.buttonPlace8,
            binding.buttonPlace9,
            binding.buttonPlace10,
            binding.buttonPlace11,
            binding.buttonPlace12,
            binding.buttonPlace13,
        )

        suspend fun ShowCardsStart() = coroutineScope() {
            launch(Dispatchers.Default) {
                for (i in (0..12))
                    buttonList[i].visibility = View.INVISIBLE

                viewModel.Start()

                withContext(Dispatchers.Main) {
                    for (i in (0..<player1.CountCard)) {
                        for (j in (0..12)) {
                            if (ListTableRanks[j] == 0 || ListTableRanks[j] == PlayerRankCards[i]) {
                                ShowImage(
                                    placeList[j * 4 + ListTableCount[j]],
                                    imageList[PlayerСards[i] - 1]
                                )
                                ListTableRanks[j] = PlayerRankCards[i]
                                ListTableCount[j] += 1
                                delay(500)
                                break
                            }
                        }
                    }
                    for (i in (0..12))
                        buttonList[i].visibility = View.VISIBLE
                }
            }
        }
        if (!PlayVSBotViewModel.info.cards.ShuffleStatus) {
            launch { ShowCardsStart() }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onResume() {
        super.onResume()
        setContentView(binding.root)

        val placeList: MutableList<ImageView> = mutableListOf(
            binding.place11,
            binding.place12,
            binding.place13,
            binding.place14,
            binding.place21,
            binding.place22,
            binding.place23,
            binding.place24,
            binding.place31,
            binding.place32,
            binding.place33,
            binding.place34,
            binding.place41,
            binding.place42,
            binding.place43,
            binding.place44,
            binding.place51,
            binding.place52,
            binding.place53,
            binding.place54,
            binding.place61,
            binding.place62,
            binding.place63,
            binding.place64,
            binding.place71,
            binding.place72,
            binding.place73,
            binding.place74,
            binding.place81,
            binding.place82,
            binding.place83,
            binding.place84,
            binding.place91,
            binding.place92,
            binding.place93,
            binding.place94,
            binding.place101,
            binding.place102,
            binding.place103,
            binding.place104,
            binding.place111,
            binding.place112,
            binding.place113,
            binding.place114,
            binding.place121,
            binding.place122,
            binding.place123,
            binding.place124,
            binding.place131,
            binding.place132,
            binding.place133,
            binding.place134
        )
        val imageList: List<Int> = listOf(
            R.drawable.card_2_h,
            R.drawable.card_2_d,
            R.drawable.card_2_c,
            R.drawable.card_2_s,
            R.drawable.card_3_h,
            R.drawable.card_3_d,
            R.drawable.card_3_c,
            R.drawable.card_3_s,
            R.drawable.card_4_h,
            R.drawable.card_4_d,
            R.drawable.card_4_c,
            R.drawable.card_4_s,
            R.drawable.card_5_h,
            R.drawable.card_5_d,
            R.drawable.card_5_c,
            R.drawable.card_5_s,
            R.drawable.card_6_h,
            R.drawable.card_6_d,
            R.drawable.card_6_c,
            R.drawable.card_6_s,
            R.drawable.card_7_h,
            R.drawable.card_7_d,
            R.drawable.card_7_c,
            R.drawable.card_7_s,
            R.drawable.card_8_h,
            R.drawable.card_8_d,
            R.drawable.card_8_c,
            R.drawable.card_8_s,
            R.drawable.card_9_h,
            R.drawable.card_9_d,
            R.drawable.card_9_c,
            R.drawable.card_9_s,
            R.drawable.card_10_h,
            R.drawable.card_10_d,
            R.drawable.card_10_c,
            R.drawable.card_10_s,
            R.drawable.card_j_h,
            R.drawable.card_j_d,
            R.drawable.card_j_c,
            R.drawable.card_j_s,
            R.drawable.card_q_h,
            R.drawable.card_q_d,
            R.drawable.card_q_c,
            R.drawable.card_q_s,
            R.drawable.card_k_h,
            R.drawable.card_k_d,
            R.drawable.card_k_c,
            R.drawable.card_k_s,
            R.drawable.card_a_h,
            R.drawable.card_a_d,
            R.drawable.card_a_c,
            R.drawable.card_a_s
        )
        val buttonList: List<Button> = listOf(
            binding.buttonPlace1,
            binding.buttonPlace2,
            binding.buttonPlace3,
            binding.buttonPlace4,
            binding.buttonPlace5,
            binding.buttonPlace6,
            binding.buttonPlace7,
            binding.buttonPlace8,
            binding.buttonPlace9,
            binding.buttonPlace10,
            binding.buttonPlace11,
            binding.buttonPlace12,
            binding.buttonPlace13,
        )

        fun BlockButtons() {
            for (i in (0..12)) {
                buttonList[i].visibility = View.INVISIBLE
            }
        }

        fun UnBlockButtons() {
            for (i in (0..12)) {
                if (ListTableRanks[i] != 0) {
                    buttonList[i].visibility = View.VISIBLE
                }
            }
        }

        fun isWinner(): Boolean {
            if (player1.Points == 7) {
                player1.WinnerStatus = true
                return true
            }
            if (Bot.Points == 7) {
                Bot.WinnerStatus = true
                return true
            }
            return false
        }

        fun isNoCards(player: Player): Boolean {
            if (player.CountCard == 0) {
                count += 1
                return true
            }
            return false
        }

        fun MessageWinner() {
            if (player1.WinnerStatus) {
                binding.textAnswer.text = "YOU WIN"
                binding.textAnswer.visibility = View.VISIBLE
            }
            if (Bot.WinnerStatus) {
                binding.textAnswer.text = "BOT WINS"
                binding.textAnswer.visibility = View.VISIBLE
            }
        }

        val animation1List: List<Int> = listOf(
            R.drawable.frame0000,
            R.drawable.frame0001,
            R.drawable.frame0002,
            R.drawable.frame0003,
            R.drawable.frame0004,
            R.drawable.frame0005,
            R.drawable.frame0006,
            R.drawable.frame0007,
            R.drawable.frame0008,
            R.drawable.frame0009,
            R.drawable.frame0010,
            R.drawable.frame0011,
            R.drawable.frame0012,
            R.drawable.frame0013,
        )

        suspend fun animation1(){
            for(i in 0..13){
                binding.imageAnimation1.setImageResource(animation1List[i])
                delay(70)
            }
            binding.imageAnimation1.setImageResource(animation1List[4])
            delay(80)
            binding.imageAnimation1.setImageResource(animation1List[0])
        }

        fun Game() {
            for (i in (0..12)) {
                buttonList[i].setOnClickListener {
                    if (isWinner()) {
                        MessageWinner()
                    } else {
                        //если мой ход
                        if (count % 2 == 0) {
                            lifecycleScope.launch {
                                withContext(Dispatchers.Main) {
                                    //выбор карты
                                    for (t in 0..<player1.RankCards.size) {
                                        if (player1.RankCards[t] == ListTableRanks[i]) {
                                            AskCard = t
                                            break
                                        }
                                    }
                                    binding.textView.text = "У вас есть ${ListTableRanks[i]}?"
                                    BlockButtons()
                                    delay(1000)
                                    //если нашлась
                                    if (viewModel.isFound(
                                            player1,
                                            Bot
                                        )
                                    ) {
                                        binding.textView.text = "нашло"
                                        if (isWinner()) {
                                            MessageWinner()
                                        }
                                        UnBlockButtons()
                                        ShowCardsIfFound(placeList, imageList)
                                        delay(1000)
                                        HideCardsIfFour(player1, placeList)
                                    }
                                    //если не нашлась
                                    else {
                                        GlobalScope.launch {
                                            withContext(Dispatchers.Main) {
                                                binding.textView.text = "не нашло"
                                                if (isWinner()) {
                                                    MessageWinner()
                                                }
                                                ShowCardsIfNotFound(placeList, imageList)
                                                delay(1000)
                                                HideCardsIfFour(player1, placeList)
                                                //ход бота
                                                do {
                                                    BotIfFourCards(Bot)
                                                    if (isNoCards(Bot))
                                                        break
                                                    //выбор карты бота
                                                    do {
                                                        AskCard =
                                                            (0..<Bot.PlayerCards.size).random()
                                                    } while (Bot.RankCards[AskCard] == 0)

                                                    binding.textView.text =
                                                        "бот сходил, искал ${Bot.RankCards[AskCard]}"
                                                    delay(2000)

                                                    if (viewModel.isFound(Bot, player1)) {
                                                        for (k in (0..12)) {
                                                            if (ListTableRanks[k] == BotRankCards[AskCard] && BotRankCards[AskCard] != 0) {
                                                                ListTableRanks[k] = 0
                                                                ListTableCount[k] = 0

                                                                for (g in (0..3)) {
                                                                    placeList[4 * k + g].visibility =
                                                                        View.INVISIBLE
                                                                }
                                                            }
                                                        }
                                                        binding.textView.text = "бот нашел карту"
                                                        if (isWinner()) {
                                                            MessageWinner()
                                                        }
                                                        delay(1000)
                                                    } else {
                                                        binding.textView.text = "бот не нашел карту. Ваш ход"
                                                        animation1()
                                                    }
                                                    if (isWinner()) {
                                                        MessageWinner()
                                                    }

                                                } while (botFound)
                                                UnBlockButtons()
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            binding.textView.text = "не твой ход"
                        }
                    }
                }
            }
        }
        launch { Game() }
    }
}

