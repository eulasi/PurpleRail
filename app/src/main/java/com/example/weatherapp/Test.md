ViewModel Tests:
Test getWeatherByCityName with a valid city name and verify that weatherData is updated correctly.
Test getWeatherByCityName with an invalid city name and verify that weatherData remains unchanged or shows an error.
Test getWeatherByCoordinates with valid coordinates and verify that weatherData is updated correctly.

Repository Tests:
Test the getWeatherByCityName method in your repository with a valid city name and verify that it returns the correct WeatherResponse.
Test the getWeatherByCityName method with an invalid city name and verify that it handles the error gracefully.

DataStore Tests:
Test saving and retrieving the city name in CityStorage.
Test saving and retrieving the image key in WeatherImageStorage.

UI Tests:
Test that the search button triggers the weather data fetch.
Test that entering a city name and clicking the search button updates the UI with the weather information.
Test that the app requests location permissions and fetches weather data based on the current location if granted.

Error Handling Tests:
Test how the app handles network errors or API failures.
Test how the app behaves when location permissions are denied.