package com.example.local.mapper

/**
 * Interface genérica para Mappers
 * Define o contrato de conversão entre tipos
 */
// Entity = tipo gerado pelo SQLDelight
// Domain = User (domínio)
// Dto    = UserDto (remoto/API)
interface Mapper<Entity, Domain, Dto> {
    fun mapEntityToDomain(entity: Entity): Domain  // SQLDelight → Domínio
    fun mapDomainToEntity(domain: Domain): Entity  // Domínio → SQLDelight
    fun mapDtoToDomain(dto: Dto): Domain           // API → Domínio
    fun mapDomainToDto(domain: Domain): Dto        // Domínio → API
}