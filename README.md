# Weather App Challenge: Instructions

## Initial Setup
### Configure Gradle 
* Add necessary dependencies for `Jetpack Compose`, `Retrofit`, `Coroutines`, `UnitTesting` and `Data Storage` in the build.gradle files.

## App Architecture
### Create a Model
* Define data classes for the information via `Postman`
### Create a Repository
* Implement a repository class that will use `Retrofit` to fetch data from the API.
### Create a ViewModel
* Implement a `ViewModel` that will use the repository to load data and expose it to the UI.

## User Interface
### Create Composable functions  
* Create UI screens with `JetPack Compose`
### Implement Navigation 
* Use `Jetpack Navigation` to navigate between different screens.

##  Location Access
### Request Location Permissions 
* Ask the user for location access permissions.
### Get the Current Location 
* Use the fused location provider to get the user's current location.
### Fetch Data 
* Use the location coordinates to fetch data from the API.

## Data Caching
### Implement Data Caching
* Use `Jetpack DataStore` to cache data.

## Testing
### Write Unit Tests 
* Use `JUnit` and `Mockito` to write unit tests.