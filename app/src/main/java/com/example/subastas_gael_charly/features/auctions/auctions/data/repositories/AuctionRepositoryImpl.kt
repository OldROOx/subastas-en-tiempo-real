package com.example.subastas_gael_charly.features.auctions.auctions.data.repositories

class AuctionRepositoryImpl @Inject constructor(
    private val api: AuctionApi,
    private val dao: AuctionDao,
    private val ws: WebSocketDataSource
) : AuctionRepository {

    // La UI observa este Flow directo de la base de datos local
    override fun getAuctionsStream(): Flow<List<Auction>> =
        dao.getAllAuctions().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshAuctions() {
        val response = api.getAuctions()
        if (response.isSuccessful) {
            val entities = response.body()?.auctions?.map { it.toEntity() } ?: emptyList()
            dao.insertAuctions(entities) // Se guarda en DB local (SSOT)
        }
    }

    fun startListeningUpdates() {
        ws.connect { auctionId, newPrice ->
            // Al recibir actualización por WebSocket, actualizamos Room
            CoroutineScope(Dispatchers.IO).launch {
                dao.updatePrice(auctionId, newPrice)
            }
        }
    }
}