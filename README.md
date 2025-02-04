Features:

Product List Display: The app uses a lazy grid to display products in a grid layout.

Search Bar: A search bar enables users to filter products based on the title, with real-time updates.

Network Connectivity Check: The app checks the device's network connectivity to handle scenarios like no internet connection or timeout.

Swipe to Refresh: Users can swipe to refresh the product list and re-fetch the data from the API.

Loading Indicators: A circular progress indicator is displayed while fetching the data.

Error Handling: Errors are displayed using a Snackbar to inform the user about issues like network errors.

ProductListScreen :

Network Connection Check: Uses connectivityState to determine the device's network status and displays appropriate messages or UI based on the connectivity.

Product Filtering: Filters the displayed products based on the search query entered by the user.

Loading State: Displays a loading spinner while data is being fetched.

Swipe to Refresh: Uses SwipeRefresh to allow users to refresh the list by pulling down.

Snackbar for Errors: Displays any errors related to network issues or API failures.

ProductCard  -- # Title/Price/Category/Image with loading /Rating

Dependencies:

Retrofit        #Functionalities for networking  ( 'com.squareup.retrofit2:retrofit:2.9.0')

Coil    #Image loading   ('io.coil-kt:coil-compose:2.2.0')

Accompanist SwipeRefresh #UI behavior  (com.google.accompanist:accompanist-swiperefresh:0.28.0)
