#Online-calculator-application
This is a single activity application with two framents where each fragment is in its directroy.

Calculation Fragment
- UI => Contains text area, a recycler view for showing result and two buttons one for submitting result and other for history fragment
- The fragment has logic of fetching the data using retrofit from WolfRam API service and than saving the data into the room database
- Fragment has two viewmodels
- One for fetching the data on the basis of query
- Second for saving the data into database

History Fragment
- UI => It has recycler view for showing the histroy and two buttons one for navigating back and other for getting the latest history item at the top
- It has only one viewmodel associated with it for getting data from database

CalaulationViewmodel
- Used kotlin flow for handling the data
- Used a sealed class for handling the state of the flow
- Didn't used repository as we were fetching only one response so avoid writing extra code

Calculation History Viewmodel
- It fetches and save data into the database
- Used Objects of LiveData in it
- Used the repository pattern
- Also the data with data class having an id, date in long for query, date in string for showing and the expression
- Library or APi Used

Retrofit
Room database
Hilt
coroutines
flows
viewBinding
Also used DiffUtil with recycler view


Testing
- Write the test for database
- Was writing the test for fragment and ran into errors and was facing something new so still working on it
- Link to signed apk and video recording of application on screen : https://app.mediafire.com/rux8amsx8tzbt
