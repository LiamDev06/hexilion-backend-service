# hexilion-backend-service
`hexilion-backend-service` is a REST API built with Java and SpringBoot initially planned to act as the main backend service and API for [Hexilion Land](https://land.hexilion.net) Minecraft network.

## Features
- HTTP endpoints for CRUD operations with player data, game data, configurations, server instances and more.
- API-Key authentication to ensure valid requests.
- Wrapped Mongo Java driver to support multiple Mongo instances for types `MASTER`, `GAME` and `NETWORK`.
- Custom built responses for both success and error requests.

## Purpose
I originally created this application for the Minecraft project [Hexilion Land](https://land.hexilion.net) where it was intended to be used as the main API and backend service for the network. As development on the project continued, we realized more and more how we'd benefit better from a services-architecture rather than a single monolith API and thus made the decision to switch to a microservices solution with each service responsible for a part of the network's backend. This also allowed us to build services not directly falling under RESTful principals like network-queue, game instancing and load-balancing services.  

As a result, this project is no longer in-use and I am therefore putting it on GitHub as an example of my recent work.
