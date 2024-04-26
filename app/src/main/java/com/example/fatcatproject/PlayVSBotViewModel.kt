package com.example.fatcatproject

import androidx.lifecycle.ViewModel

class PlayVSBotViewModel() : ViewModel() {

    object info{
        var cards: Cards = Cards(false)
        val PlayerName: String = "Player1"
        var PlayerСards: MutableList<Int> = MutableList(7) { 0 }
        var PlayerRankCards: MutableList<Int> = MutableList<Int>(7) { 0 }
        var BotCards: MutableList<Int> = MutableList(7) { 0 }
        var BotRankCards: MutableList<Int> = MutableList<Int>(7) { 0 }
        var player1: Player = Player(PlayerName, PlayerСards, PlayerRankCards, 0, false, 0)
        var Bot: Player = Player("Bot", BotCards, BotRankCards, 0, false, 0)
        var AskCard: Int = -1
        var count: Int = 0
        var ScannedValue: Int = -1
        var ListTableRanks: MutableList<Int> = MutableList<Int>(13) { 0 }
        var ListTableCount: MutableList<Int> = MutableList<Int>(13) { 0 }
        var botFound: Boolean = false
    }

    //поиск карты
    fun isFound(player1: Player, player2: Player): Boolean {
        //карта не нашлась
        if (player1.RankCards[info.AskCard] in player2.RankCards && player1.RankCards[info.AskCard] != 0) {
            for (i in (0..<player2.RankCards.size)) { //просматриваем все карты игрока 2
                if (player2.RankCards[i] == player1.RankCards[info.AskCard]) {
                    player1.PlayerCards.add(player2.PlayerCards[i]) // добавление данной карты 1 игроку
                    player1.RankCards.add(player2.RankCards[i])
                    player2.PlayerCards[i] = 0
                    player2.RankCards[i] = 0
                    player1.CountCard += 1
                    player2.CountCard -= 1
                }
            }
            info.botFound = true
            return true // продолжение хода этого игрока
        }
        // карта не нашлась
        else {
            info.botFound = false
            info.count += 1
            for (i in (0..51)) {
                if (Cards.CardsList.ShuffleList[i] != 0) {
                    player1.RankCards.add(Cards.CardsList.ShuffleRanks[i]) //игрок получает верхнюю карту колоды
                    player1.PlayerCards.add(Cards.CardsList.ShuffleList[i])
                    player1.CountCard += 1 //количество его карт увеличилось на 1
                    Cards.CardsList.ShuffleList[i] = 0; //обнуление верхней карты колоды
                    Cards.CardsList.UpdateCards(
                        Cards.CardsList.ShuffleList,
                        Cards.CardsList.ShuffleRanks
                    ) //обновление нуумерации карт
                    break
                }
            }
            return false // переход хода
        }
    }

    //проверка есть ли у игрока 4 карты одной ценности
    fun isFourCards(player: Player){
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
        }
    }

    // проверка на победу
    fun isWinner(): Boolean {
        if (info.player1.CountCard == 0 && info.Bot.CountCard == 0) {
            if (info.player1.Points > info.Bot.Points) { // количество очков первого больше, чем у второго
                info.player1.WinnerStatus = true
            } else {
                info.Bot.WinnerStatus = true // количество очков первого меньше, чем у второго
            }
            return true
        }
        return false
    }

    // проверка кончились ли игрока карты
    fun isNoCards(playerOne: Player, playerTwo: Player) {
        if (playerOne.CountCard == 0)
            PlayerMove(playerTwo, playerOne)
    }


    // ход игрока
    fun PlayerMove(playerOne: Player, playerTwo: Player) {

        //проверка на победу
        if (isWinner()) return

        // проверка наличия у игрока 4 карт одго знака
        isFourCards(playerOne)
        isFourCards(playerTwo)
        //проверяем кончились ли карты у игрока
        isNoCards(playerOne, playerTwo)

        //считывание выбора игрока
        if (info.count % 2 == 0) {
            info.AskCard = info.ScannedValue
        }
        else {
            info.AskCard = (0..<info.Bot.CountCard).random()
            println(info.AskCard + 1)
        }

        // просит несуществующую карту
        if (info.AskCard ==- 1 || playerOne.PlayerCards[info.AskCard] == 0) {
            print("BAD! Try again\n")
            PlayerMove(playerOne, playerTwo)
        }

        //переход хода или продолжение хода данного игрока
        if (info.AskCard!=-1) {
            if (isFound(playerOne, playerTwo))
                PlayerMove(playerOne, playerTwo)
            else {
                PlayerMove(playerTwo, playerOne)
            }
        }
        return
    }

    // начало игры
    fun Start() {
        info.cards.Shuffle() //рандом карт
        info.player1.GetCards(info.player1) // раздача карт 1 и 2 игрокам
        info.Bot.GetCards(info.Bot)
        Cards.CardsList.UpdateCards(Cards.CardsList.ShuffleList, Cards.CardsList.ShuffleRanks)
    }
}
