# NCC Timesheet Manager Backend

## Project Description

This application allows for the backend connection of the NCC Timesheet app to run successfully. The server connects to the SQL database
for accessing user and worksite data.

## Getting Started

To spin up the app, run the Application.java file. This will run an instance of the server locally on the user's device.
Authentication is also required in order to access the database. An account can either be created or logged into to obtain a bearer token.
These tokens will be used as a cookie in every API call afterward.

For frontend testers, they can also use [Swagger UI](**/swagger-ui.html) to host the endpoint calls. Authentication will still
need to be obtained for swagger calls.

## Users

For the users end points there are endpoints that allow the referencing of all the users in the database or querying them
individually by ID number. Each user object contains all the necessary data regarding their account on the server.

## Worksites

Worksites work similarly to users aside from that worksites only contain a worksite name and worksite ID. Endpoint calls can retrieve
a list of all existing worksites or find a specific worksite by its ID.
