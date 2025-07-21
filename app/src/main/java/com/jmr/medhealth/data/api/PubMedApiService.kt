package com.jmr.medhealth.data.api

import retrofit2.http.GET
import retrofit2.http.Query

// Simplified model of the PubMed API JSON response
data class PubMedResponse(val esearchresult: ESearchResult)
data class ESearchResult(val idlist: List<String>)
// A full implementation would need another call to fetch article summaries by ID

interface PubMedApiService {
    @GET("entrez/eutils/esearch.fcgi?db=pubmed&retmode=json")
    suspend fun searchArticles(
        @Query("term") searchTerm: String,
        @Query("retmax") count: Int = 20
    ): PubMedResponse
}