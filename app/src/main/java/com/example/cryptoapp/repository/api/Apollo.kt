package com.example.cryptoapp.repository.api

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphql-pokemon2.vercel.app")
    .build()