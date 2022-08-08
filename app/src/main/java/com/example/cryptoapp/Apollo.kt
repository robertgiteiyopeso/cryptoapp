package com.example.cryptoapp

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphql-pokemon2.vercel.app")
    .build()