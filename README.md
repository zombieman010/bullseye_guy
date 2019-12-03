# bullseye_guy

## Overview 
Simple API that calls and external API and stores data to a NoSQL data store. 

## Build
Application uses Maven 3.0 or greater to build. 

## Test 
Tests were created with Junit and Mockito. Tests can be run through maven. 

## Docker Build
When you execute maven install a docker image will be built. Canidates for docker hub will be pushed to npautzke/bullseye-guy:<tag>

## Data Store 
NoSQL data store used in Mongo DB. The application.yml file will have to be modified to hock up to your mongo database. 
