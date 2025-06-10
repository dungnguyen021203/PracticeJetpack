package com.example.movieapp.movie.common.data

interface ApiMapper<Domain, Entity> {
    fun mapToDomain(apiDto: Entity) : Domain
}