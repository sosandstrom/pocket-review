# Pocket-Reviews

Have you developed a cool app showing various items and product, e.g. restaurants, photos, hotels, games or movies, and you like your users to be able to rate them but have nowhere to store all those stars? No backend buddies to help you out?

## Overview
Pocket-reviews is a lightweight, easy to use and ready to go review and rating service tailored for mobile applications.

Pocket-reveiws consist of:

* A backend service hosted on Google App Engine  
* iOS SDK (coming soon)
* Android SDK (coming soon)  
* Javascript SDK (coming soon)  

Developers can use any of the supported SDKs to quickly integrate the service into their application or use the REST-API provided by the backend and implement the network requests them self.

The service is divided into the following groups of features:

* Rating and Reviews - Rate and review a product    
* Likes - Like a product (+1)
* Comments - Added comments to a product
* Favourites - Add a product to my favourites
* Thumbs up and down
* Feedback - Give feedback on a product
* Ask a Question - Ask a question/opinion from another user
* Poll - Create a poll (still under development)
* Mark as inappropriate

A high level description of the REST end-points can be found [here](https://github.com/sosandstrom/pocket-review/wiki/REST-end-points).

### Unique product id

Each method must include a unique product id provided by the application. The application might receive this id from some other service being integrated, e.g. a product catalog or a movie listing web service. It is up to the application to provide this id and ensure uniqueness and consistency throughout requests.

### Anonymous rating and reviews
The application can perform all methods either anonymously or providing a unique user id.

If a unique user id is provided it will be possible to ask for ratings, likes and comments for a specific user, e.g. my review, my likes, my favourites. Very handy at times.

The SDKs provide functionality for automatically generating a unique user id that will persist through application restarts and application upgrades. The application may also provide their own user id if they have a better way of identifying the user, e.g. through registration or login.

### Location tagged ratings and reviews
The application can optionally supply a position (latitude and longitude) of the product being rated, liked or commented. This will allow the application to perform nearby operations, e.g. get nearby top average ratings, get nearby most liked items. Extremely useful and will increase user experience a lot.

## Deployment
The backend code is divided into a service part and a ready-made GAE backend part.

Developers have several deployment options:

* Using the already deployed and free to use GAE instance at *TBD*. Please note that this instance will only provide services as long as the hosting cost is below the daily budget set by the authors.
* Deploy their own instance of Pocket-Reviews on GAE. This will give the developer exclusive control of who can access the service and the budget and cost from the GAE dashboard. For setting up you own GAE instance, please find instructions here *TBD*.
* Integrate the service into an existing GAE project [here](https://github.com/sosandstrom/pocket-review/wiki/Integrate-into-existing-service)

## API documentation
Additional details about the APIs for respective SDK can be found below:

[REST API](http://sosandstrom.github.com/pocket-review/apidocs/api.html)  
iOS (coming soon)
Android (coming soon)  
Javascript (coming soon)

