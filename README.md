# Pocket-Reviews

**Under development comming soon..**

Have you developed a cool app showing various items and product, e.g. restaurants, photos, hotels, and you like your users to be able to rate them but have nowhere to store all those stars? Not backend buddies t help you out?

## Overview
Pocket-reviews is a lightweight, easy to use and ready to go review and rating service tailored for mobile applications.

Pocket-reveiws consist of:

* A backend service hosted on Google App Engine  
* iOS SDK  
* Android SDK (coming soon)  
* Javascript SDK (coming soon)  

Developers can use any of the supported SDKs to quickly integrate the service into their application or use the REST-API provided by the backend and implement the network requests them self.

The service is divided into the following groups of features:

* Ratings - Rate a product  
* Reviews - Write a review for a product  
* Likes - Like a product  
* Favorites - Add a product to my favorites

### Product id

Each method must include a unique product id provided by the application. The application might receive this id from some other service being integrated, e.g. a product catalog. It is up to the application to provide this id and ensure uniqueness and consistency through requests.

### Anonymous rating and reviews
The application can perform all functions either anonymoulsy or providing a unique user identifier.

If a unique user id is provided it will be possible to ask for ratings and reviews for a specific user, e.g. my review, my likes, my favorites.

The SDKs provide functionality for automatically generating a unique user identifier that will persist through application restarts. The application may also provide their own user id if they have a better way of identifying the user, e.g. through registration or login.

### Location tagged ratings and reviews
The application can optionally supply a position (latitude and longitude) of the product being rated. This will allow the application to perform nearby operations, e.g. get nearby top average ratings. It is important that it is the products location that is provided during a rating and reivew, not the device location.

## Deployment
The backend is running on Google Appe Engine. 

The backend code is divided into a service and a ready-made GAE backend.

Users have several deploy options:

* Using the already deployed and free to use GAE instance at TBD. Please note that this instance will only provide service as long as hosting cost is below the daily GAE budget set by the authors.
* Deploy their own instace of the GAE backend. This will give the developer full control of the budget and cost from the GAE dashboard. For setting up you own GAE instance, please find instrucitons here TBD.
* Integrat the service into an exsisting GAE project [here](https://github.com/sosandstrom/pocket-review/wiki/Integrate-into-existing-service)

## API documentation
The additional details can be found here documentation for the respective SDK

[REST API](http://pocket-reviews.appspot.com/doc/rest-api/api.html)  
[iOS](http://pocket-reviews.appspot.com/doc/ios/index.html)  
Android (coming soon)  
Javascript (coming soon)

