# Pocket-Reviews

**Under development comming soon..**

## Overview
Pocket-reviews is a lightweight, easy to use and ready to go review and rating service tailored for mobile applications.

Pocket-reveiws consist of:

* A backend service hosted on Google App Engine
* iOS SDK
* Javascript SDK (coming soon)
* Android SDK (coming soon)

Developers can use any of the supported SDKs to quickly integrate the service into their application or use the REST-API provided by the backend and implement the network requests them self.

The service is divided into the following groups of functions:

* Ratings - rate (1-5) a product
* Reviews - write a review for a product
* Likes - like a product

Each function must include a unique product id provided by the application (the application might receive this id from some other service being integrated, e.g. a product catalog).

### Anonymous ratings
The application can perform all functions either anonymoulsy or providing a unique user identifier.

If a unique user id is provided the service will ensure that the same user will not be able to rate or review the same product more the once. It will also be possible to ask for ratings and reviews for a specific user (my ratings).

The SDKs provide functionality for automatically generating a unique user identifier that will persist through application starts. The application may also provide their own user id if they have a better way of identifying the user, e.g. through registration or login.


### Position tagged ratings
The application can optionally supply a position (latitude and longitude) of the product being rated. This will allow the application to perform nearby operations, e.g. get nearby top ratings.

## API documentation
The additional details can be found here documentation for the respective SDK

[REST API](http://pocket-reviews.appspot.com/doc/rest-api/api.html)

[iOS](http://pocket-reviews.appspot.com/doc/ios/index.html)

Android (coming soon)

Javascript (coming soon)

