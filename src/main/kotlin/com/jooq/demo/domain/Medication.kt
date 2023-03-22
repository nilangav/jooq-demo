package com.jooq.demo.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Medication {
    @Id
    val code: String = ""
    var name: String = ""
    var weight: String = ""
    var image: String = ""
}