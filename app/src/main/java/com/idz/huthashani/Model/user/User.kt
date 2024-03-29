package com.idz.huthashani.Model.user

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User {
    @PrimaryKey
    var id: String = ""
    var email: String? = ""
    var password: String? = ""
    var fullName: String? = ""
    var imageAvatar: String? = ""

    constructor()

    constructor(email: String?, password: String?, fullName: String?) {
        this.email = email
        this.password = password
        this.fullName = fullName
    }

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }

    constructor(email: String?, password: String?, name: String?, imageAvatar: String?) {
        this.email = email
        this.password = password
        this.fullName = name
        this.imageAvatar = imageAvatar
    }

    constructor(id: String, email: String?, password: String?, fullName: String?, imageAvatar: String?) {
        this.id = id
        this.email = email
        this.password = password
        this.fullName = fullName
        this.imageAvatar = imageAvatar
    }


}
