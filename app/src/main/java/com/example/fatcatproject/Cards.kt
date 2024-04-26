package com.example.fatcatproject

class Cards(var ShuffleStatus: Boolean) {

    object CardsList{
        var ShuffleList: MutableList<Int> = MutableList(52) {0}
        var ShuffleRanks: MutableList<Int> = MutableList(52) {0}
        //функция для распределения карт по номеру
        fun UpdateCards(ShuffleList: MutableList<Int>, ShuffleSigns: MutableList<Int>){
            for(i in(0..ShuffleList.size-1)){
                if(ShuffleList[i]!=0)
                    ShuffleSigns[i]=(ShuffleList[i]-1)/4+2
                else ShuffleSigns[i]=0
            }
        }
    }

    // функция тасовки карт
    fun Shuffle() {
        ShuffleStatus = true
        for (i in (0..51)) {
            CardsList.ShuffleList[i] = i + 1
        }
        repeat(200) {
            val a = (0..51).random()
            val b = (0..51).random()
            if (a != b) {
                var c = CardsList.ShuffleList[a]
                CardsList.ShuffleList[a] = CardsList.ShuffleList[b]
                CardsList.ShuffleList[b] = c
            }
        }
        for(i in(0..47)){
            if(CardsList.ShuffleList[i] == CardsList.ShuffleList[i+1] &&
                CardsList.ShuffleList[i+1] == CardsList.ShuffleList[i+2] &&
                CardsList.ShuffleList[i+2] == CardsList.ShuffleList[i+3])
                Shuffle()
        }
        CardsList.UpdateCards(CardsList.ShuffleList, CardsList.ShuffleRanks)
    }

    //функция расставления карт в правильном порядке (1-52)
    fun UnShuffle(){
        ShuffleStatus = false
        for (i in (0..51)) {
            CardsList.ShuffleList[i - 1] = i
        }
        Cards.CardsList.UpdateCards(CardsList.ShuffleList, CardsList.ShuffleRanks)
    }
}
