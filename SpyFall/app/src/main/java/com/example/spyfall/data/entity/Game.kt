package com.example.spyfall.data.entity


class Game {

    val host: String?
    val status: GameStatus?

    constructor() {
        host = null
        status = null
    }


    constructor(host: String, status: GameStatus) {
        this.host = host
        this.status = status
    }
}
