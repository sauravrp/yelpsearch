# yelpsearch

      Create an OAuth login with Yelp using http://www.yelp.com/developers/documentation/v2/authentication 
·         After login, search Yelp for business's using http://www.yelp.com/developers/documentation/v2/search_api
·         Results should be displayed like this: 

·         http://d1a3f4spazzrp4.cloudfront.net/rafa.png
·         When the user scrolls to the bottom of the list, the app must request more images and add them to the scrollable list of images.
 
·         For search results, you need only show a business's photo.
·         (Bonus) In case of no internet connection, provide a solution to retain current data(past searches and search results) until internet is available again.


Solution:
Used https://github.com/Yelp/yelp-android to access the yelp apis. Out of the box support for retrofit.
Used Universal Image Loader for caching images
OrmLite for caching in database

Known problems:
- No support on rotation
- Potential issues with reading, writing, updating db using main thread. Only one entry is being written/read at a time so it should behave fine for now. Also, yelp json responses are copied over to another custom model object class to reduce json overhead on storing/retrieveing etc
- Location hardcoded to Austin
- Gson serialization used for storing into database, either break this up into columns or use FlatBuffers for faster serialization/deserialization
