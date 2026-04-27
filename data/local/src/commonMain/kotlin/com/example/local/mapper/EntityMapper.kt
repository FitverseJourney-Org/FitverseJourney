package com.example.local.mapper


interface EntityMapper<Entity, Domain> {
    fun mapEntityToDomain(entity: Entity): Domain
    fun mapDomainToEntity(domain: Domain): Entity
}