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

<img width="307" alt="PR_Permissions" src="https://github.com/eulasi/PurpleRain/assets/96310496/41a1c939-823b-4b3c-8572-e4e92c3bc2b5">
<img width="307" alt="PR_Search" src="https://github.com/eulasi/PurpleRain/assets/96310496/83fb86a2-bc95-4ed3-ad0a-21c191f48896">
<img width="307" alt="PR_Info" src="https://github.com/eulasi/PurpleRain/assets/96310496/c4afe4a0-f1fb-4e99-b1a7-591d16d47c36">
