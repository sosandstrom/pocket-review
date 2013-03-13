<!-- Table of Contents ---------------------------------------------------->
Contents
========
1. com.wadpam.rnr.web.CommentController
  * [addComment()](#addcomment)
  * [deleteComment()](#deletecomment)
  * [getAllCommentsForProduct()](#getallcommentsforproduct)
  * [getComment()](#getcomment)
  * [getMyComments()](#getmycomments)
1. com.wadpam.rnr.web.FavoritesController
  * [addFavorite()](#addfavorite)
  * [deleteFavorite()](#deletefavorite)
  * [getFavorites()](#getfavorites)
1. com.wadpam.rnr.web.FeedbackController
  * [addFeedback()](#addfeedback)
  * [deleteFeedback()](#deletefeedback)
  * [deleteListOfFeedback()](#deletelistoffeedback)
  * [exportFeedback()](#exportfeedback)
  * [exportFeedbackWorker()](#exportfeedbackworker)
  * [getFeedback()](#getfeedback)
  * [getInapproriate()](#getinapproriate)
  * [inappropriate()](#inappropriate)
1. com.wadpam.rnr.web.LikeController
  * [addLike()](#addlike)
  * [deleteLike()](#deletelike)
  * [getAllLikesForProduct()](#getalllikesforproduct)
  * [getLike()](#getlike)
  * [getMyLikes()](#getmylikes)
1. com.wadpam.rnr.web.ProductController
  * [findNearbyProducts()](#findnearbyproducts)
  * [getAllProducts()](#getallproducts)
  * [getMostCommentedProducts()](#getmostcommentedproducts)
  * [getMostLikedProducts()](#getmostlikedproducts)
  * [getMostRatedProducts()](#getmostratedproducts)
  * [getMostThumbsDownProducts()](#getmostthumbsdownproducts)
  * [getMostThumbsUpProducts()](#getmostthumbsupproducts)
  * [getProductInfo()](#getproductinfo)
  * [getProducts()](#getproducts)
  * [getProductsCommentedByUser()](#getproductscommentedbyuser)
  * [getProductsLikedByUser()](#getproductslikedbyuser)
  * [getProductsRatedByUser()](#getproductsratedbyuser)
  * [getProductsThumbedByUser()](#getproductsthumbedbyuser)
  * [getTopRatedProducts()](#gettopratedproducts)
  * [geUserFavoriteProducts()](#geuserfavoriteproducts)
1. com.wadpam.rnr.web.QuestionController
  * [addQuestion()](#addquestion)
  * [answerQuestion()](#answerquestion)
  * [deleteQuestion()](#deletequestion)
  * [getAnwsersToQuestion()](#getanwserstoquestion)
  * [getAskedQuestions()](#getaskedquestions)
  * [getAssignedQuestions()](#getassignedquestions)
  * [getQuestion()](#getquestion)
1. com.wadpam.rnr.web.RatingController
  * [addRating()](#addrating)
  * [deleteLike()](#deletelike)
  * [getAllRatingsForProduct()](#getallratingsforproduct)
  * [getHistogramForProduct()](#gethistogramforproduct)
  * [getLike()](#getlike)
  * [getMyRatings()](#getmyratings)
1. com.wadpam.rnr.web.ThumbsController
  * [addThumbsDown()](#addthumbsdown)
  * [addThumbsUp()](#addthumbsup)
  * [deleteThumbs()](#deletethumbs)
  * [getAllThumbsForProduct()](#getallthumbsforproduct)
  * [getMyThumbs()](#getmythumbs)
  * [getThumbs()](#getthumbs)

<!-- Resource: /person ---------------------------------------------------->
/{domain}
============

**Description**: The comment controller implements all REST methods related to commenting.

**Concrete class**: com.wadpam.rnr.web.CommentController

<!-- Method: findByName() ---------------------------------------------------->		

addComment()
----------------

**Description**: Add a comment to a product.

 If you like to comment on a ratings, you can use the rating id as the product id.
 If you need the ability to comment on both ratings and products you can either use
 two different domains or prefix the productId depending on the type of comment,
 e.g. PROD-3384, RATING-34.

 This method will either redirect to the created comment or the product
 summary depending on the incoming uri.

**Implementing Class**: com.wadpam.rnr.web.CommentController

**REST path**: POST     /{domain}/comment
,     /{domain}/product/comment

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | productId | [String](#string) | domain-unique id for the product to comment |
| query | comment | [String](#string) | the comment |
| query | username | [String](#string) | optional. A unique user name or id.
                 Needed in order to perform user related operations later on. |
| query | latitude | [Float](#float) | optional. -90..90 |
| query | longitude | [Float](#float) | optional -180..180 |
| body (properties separately) | java.lang.String | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to newly created comment or product summary |

**Response Type**: [com.wadpam.rnr.json.JComment](#comwadpamrnrjsonjcomment)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteComment()
----------------

**Description**: Delete a comment with a specific id.

**Implementing Class**: com.wadpam.rnr.web.CommentController

**REST path**: DELETE     /{domain}/comment/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | The unique comment id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Comment deleted |
| 404 | NOK | Comment not found and can not be deleted |

**Response Type**: [com.wadpam.rnr.json.JComment](#comwadpamrnrjsonjcomment)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAllCommentsForProduct()
----------------

**Description**: Returns all comments for a specific product.

**Implementing Class**: com.wadpam.rnr.web.CommentController

**REST path**: GET     /{domain}/comment

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | productId | [String](#string) | the product to look for |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | All comments for product |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getComment()
----------------

**Description**: Get comment details for a specific id.

**Implementing Class**: com.wadpam.rnr.web.CommentController

**REST path**: GET     /{domain}/comment/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | The unique comment id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Comment found |
| 404 | NOK | Comment not found |

**Response Type**: [com.wadpam.rnr.json.JComment](#comwadpamrnrjsonjcomment)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMyComments()
----------------

**Description**: Returns all comments done by a specific user.

**Implementing Class**: com.wadpam.rnr.web.CommentController

**REST path**: GET     /{domain}/comment

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | username | [String](#string) | a unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | All comments for user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Resource: /person ---------------------------------------------------->
/{domain}
============

**Description**: The favorites controller implements all REST methods related to favorites.

**Concrete class**: com.wadpam.rnr.web.FavoritesController

<!-- Method: findByName() ---------------------------------------------------->		

addFavorite()
----------------

**Description**: Add a product as favorite.

**Implementing Class**: com.wadpam.rnr.web.FavoritesController

**REST path**: POST     /{domain}/favorites/{username}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | username | [String](#string) | unique user name or id |
| query | productId | [String](#string) | domain-unique id for the product to add as favorites |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to users favorites |

**Response Type**: [com.wadpam.rnr.json.JFavorites](#comwadpamrnrjsonjfavorites)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"productIds"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteFavorite()
----------------

**Description**: Remove a product from favorites.

**Implementing Class**: com.wadpam.rnr.web.FavoritesController

**REST path**: DELETE     /{domain}/favorites/{username}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | username | [String](#string) | unique user name or id |
| query | productId | [String](#string) | domain-unique id for the product to add as favorites |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to updated users favorites |
| 404 | NOK | Product not a favorite for the user |

**Response Type**: [com.wadpam.rnr.json.JFavorites](#comwadpamrnrjsonjfavorites)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"productIds"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getFavorites()
----------------

**Description**: Get favorites for user.

**Implementing Class**: com.wadpam.rnr.web.FavoritesController

**REST path**: GET     /{domain}/favorites/{username}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | username | [String](#string) | unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Favorites found for user |

**Response Type**: [com.wadpam.rnr.json.JFavorites](#comwadpamrnrjsonjfavorites)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"productIds"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Resource: /person ---------------------------------------------------->
/
============

**Description**: The feedback controller implements all REST methods related to feedback.
 This feature can be used to provide registration form or feedback form.

**Concrete class**: com.wadpam.rnr.web.FeedbackController

<!-- Method: findByName() ---------------------------------------------------->		

addFeedback()
----------------

**Description**: Send a feedback form to the backend.

 The backend can either persist the feedback in the datastore or forward it in an email
 or do both depending on configuration.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: POST     //{domain}/feedback

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | title | [String](#string) | the feedback title |
| query | feedback | [String](#string) | the feedback main description |
| query | referenceId | [String](#string) | a referenceId selected by the app. Could be reference to a
                    product id, application id or other content.
                    Must be unique within the app context. |
| query | category | [String](#string) | an optional category to allow the user the categorize the feedback e.g.
                 "quality", "service", price. Can also be used to allow better post processing
                 of the feedback. |
| query | deviceModel | [String](#string) | optional device model |
| query | deviceOS | [String](#string) | optional device OS |
| query | deviceOSVersion | [String](#string) | optional device OS version |
| query | username | [String](#string) | optional user name of id of the person providing the feedback |
| query | userContact | [String](#string) | optional user contact data of the person providing the feedback.
                    This can be used
              to get back to the user. |
| query | latitude | [Float](#float) | optional latitude of the user when the feedback was given |
| query | longitude | [Float](#float) | optional longitude of the user when the feedback was given |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | The feedback was logged |

**Response Type**: [org.springframework.web.servlet.view.RedirectView](#orgspringframeworkwebservletviewredirectview)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"applicationContext"</b>:ApplicationContext,</div><div>&nbsp;&nbsp;&nbsp;<b>"logger"</b>:Log,</div><div>&nbsp;&nbsp;&nbsp;<b>"messageSourceAccessor"</b>:MessageSourceAccessor</div><div>&nbsp;&nbsp;&nbsp;<b>"servletContext"</b>:ServletContext</div><div>&nbsp;&nbsp;&nbsp;<b>"beanName"</b>:String,</div><div>&nbsp;&nbsp;&nbsp;<b>"contentType"</b>:String,</div><div>&nbsp;&nbsp;&nbsp;<b>"DEFAULT_CONTENT_TYPE"</b>:String,</div><div>&nbsp;&nbsp;&nbsp;<b>"OUTPUT_BYTE_ARRAY_INITIAL_SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;<b>"requestContextAttribute"</b>:String,</div><div>&nbsp;&nbsp;&nbsp;<b>"staticAttributes"</b>:Map</div><div>&nbsp;&nbsp;&nbsp;<b>"url"</b>:String</div><div>&nbsp;&nbsp;&nbsp;<b>"contextRelative"</b>:boolean,</div><div>&nbsp;&nbsp;&nbsp;<b>"encodingScheme"</b>:String,</div><div>&nbsp;&nbsp;&nbsp;<b>"exposeModelAttributes"</b>:boolean,</div><div>&nbsp;&nbsp;&nbsp;<b>"http10Compatible"</b>:boolean,</div><div>&nbsp;&nbsp;&nbsp;<b>"statusCode"</b>:HttpStatus</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteFeedback()
----------------

**Description**: Delete one single user feedback.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: *     //{domain}/feedback/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | id | [Long](#long) | the feedback to delete |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Feedback deleted |
| 404 | NOK | Feedback not found |

**Response Type**: [java.lang.Void](#javalangvoid)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteListOfFeedback()
----------------

**Description**: Delete multiple user feedback.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: *     //{domain}/feedback

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | timestamp | [Long](#long) | Optional. Delete user feedback created/updated before
                  this timestamp |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Feedback deleted |

**Response Type**: [java.util.Map](#javautilmap)

**Response Example**:
{}
				
<!-- Method: findByName() ---------------------------------------------------->		

exportFeedback()
----------------

**Description**: Export all persisted feedback to CSV file and forward it to the
 provided email.

 The actual export is done in a separate task and this method
 will complete immediately.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: GET     //{domain}/feedback/export

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | email | [String](#string) | the email address to send the exported feedback to |
| query | timestamp | [Long](#long) | Optional. Export user feedback created/updated after
                  this timestamp. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Export started |

**Response Type**: [java.lang.Void](#javalangvoid)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

exportFeedbackWorker()
----------------

**Description**: A GAE task for exporting user feedback to CSV format.
 Clients should not use this REST method directly.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: POST     //_worker/{domain}/feedback/export

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) | the domain |
| query | email | [String](#string) | The email the exported CSV file should be sent to |
| query | timestamp | [Long](#long) | export user feedback create/updated after the
                  provided timestamp |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | The export process was started |

**Response Type**: [java.lang.Void](#javalangvoid)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getFeedback()
----------------

**Description**: Get one single user feedback details.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: GET     //{domain}/feedback/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | id | [Long](#long) | the feedback to get |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Feedback found |
| 404 | NOK | Feedback not found |

**Response Type**: [com.wadpam.rnr.json.JFeedback](#comwadpamrnrjsonjfeedback)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"category"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"deviceModel"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"deviceOS"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"deviceOSVersion"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"feedback"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"referenceId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"title"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"userContact"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getInapproriate()
----------------

**Description**: Get one single inappropriate report.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: GET     //{domain}/inappropriate/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | id | [Long](#long) | the report to get |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Feedback found |
| 404 | NOK | Inappropriate report not found |

**Response Type**: [com.wadpam.rnr.json.JInappropriate](#comwadpamrnrjsonjinappropriate)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

inappropriate()
----------------

**Description**: Report something as inappropriate.

 It is up to the app context to device what can be reported on. Could be content like comments
 and images or something else that is being displayed in the app.

**Implementing Class**: com.wadpam.rnr.web.FeedbackController

**REST path**: POST     //{domain}/inappropriate

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | referenceId | [String](#string) | a referenceId selected by the app. Could be reference to a
                    product, application, comment, image etc. Must be unique within the app context. |
| query | referenceDescription | [String](#string) | An optional description of the reference id. This will allow for more easy
                             reading during the moderation. |
| query | username | [String](#string) | Optional user name |
| query | latitude | [Float](#float) | optional latitude of the user when the report was done |
| query | longitude | [Float](#float) | optional longitude of the user when the report was done |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Marked as inappropriate |

**Response Type**: [java.lang.Void](#javalangvoid)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class</div>}
				
<!-- Resource: /person ---------------------------------------------------->
/{domain}
============

**Description**: The like controller implements all REST methods related to likes.

**Concrete class**: com.wadpam.rnr.web.LikeController

<!-- Method: findByName() ---------------------------------------------------->		

addLike()
----------------

**Description**: Add a like to a product.

 This method will either redirect to the created like or the product
 summary depending on the incoming uri.

**Implementing Class**: com.wadpam.rnr.web.LikeController

**REST path**: POST     /{domain}/like
,     /{domain}/product/like

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | productId | [String](#string) | domain-unique id for the product to like |
| query | username | [String](#string) | optional. A unique user name or id.
                 Needed in order to perform user related operations later on. |
| query | latitude | [Float](#float) | optional, -90..90 |
| query | longitude | [Float](#float) | optional, -180..180 |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to the newly created like or product summary |

**Response Type**: [com.wadpam.rnr.json.JLike](#comwadpamrnrjsonjlike)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteLike()
----------------

**Description**: Delete a like with a specific id.

**Implementing Class**: com.wadpam.rnr.web.LikeController

**REST path**: DELETE     /{domain}/like/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | The unique like id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Like deleted |
| 404 | NOK | Like not found and can not be deleted |

**Response Type**: [com.wadpam.rnr.json.JLike](#comwadpamrnrjsonjlike)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAllLikesForProduct()
----------------

**Description**: Returns all likes for a specific product.

**Implementing Class**: com.wadpam.rnr.web.LikeController

**REST path**: GET     /{domain}/like

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | productId | [String](#string) | the product to looks for |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Page of likes for product |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getLike()
----------------

**Description**: Get like details for a specific id.

**Implementing Class**: com.wadpam.rnr.web.LikeController

**REST path**: GET     /{domain}/like/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | The unique like id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Like found |
| 404 | NOK | Like not found |

**Response Type**: [com.wadpam.rnr.json.JLike](#comwadpamrnrjsonjlike)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMyLikes()
----------------

**Description**: Returns all likes done by a specific user.

**Implementing Class**: com.wadpam.rnr.web.LikeController

**REST path**: GET     /{domain}/like

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | username | [String](#string) | a unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | All likes for user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Resource: /person ---------------------------------------------------->
/{domain}/product
============

**Description**: The product controller implements all REST methods related to products.

**Concrete class**: com.wadpam.rnr.web.ProductController

<!-- Method: findByName() ---------------------------------------------------->		

findNearbyProducts()
----------------

**Description**: Returns a list of nearby products.

 If no latitude or longitude is provided in the request position provided by Google App Engine will be used.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/nearby

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |
| query | latitude | [Float](#float) | optional, the latitude to search around |
| query | longitude | [Float](#float) | optional, the longitude to search around |
| query | radius | [int](#int) | optional. The radius to search with in. Default value 3000m |
| query | sort | [int](#int) | optional, the sort order of the returned results
             0 - distance, default value
             1 - average rating
             2 - number of likes
             3 - number of thumbs up |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Nearby products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAllProducts()
----------------

**Description**: Get all products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMostCommentedProducts()
----------------

**Description**: Get most commented products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/commented/most

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Most liked products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMostLikedProducts()
----------------

**Description**: Get most liked products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/liked/most

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Most liked products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMostRatedProducts()
----------------

**Description**: Get most rated products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/rated/most

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Most liked products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMostThumbsDownProducts()
----------------

**Description**: Get most thumbs down products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/thumbs/down/most

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Most thumbed down products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMostThumbsUpProducts()
----------------

**Description**: Get most thumbs up products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/thumbs/up/most

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Most thumbed up products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getProductInfo()
----------------

**Description**: Get a specific product.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/{productId}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | productId | [String](#string) | domain-unique id for the product |
| query | username | [String](#string) | Optional. If a user name is provided the service will check of the user
                 has previously liked this product and will return this in the response.
                 Please not that this will make the request slower and cost more money and
                 should this only be used when absolutely necessary, |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Product found |
| 404 | Not Found | Product not found |

**Response Type**: [com.wadpam.rnr.json.JProduct](#comwadpamrnrjsonjproduct)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getProducts()
----------------

**Description**: Get a list of products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | ids | [java.lang.String[]](#javalangstring[]) | a list of productIds |
| query | username | [String](#string) | Optional. If a user name is provided the service will check of the user
                 has previously liked this product and will return this in the response.
                 Please not that this will make the request slower and cost more money and
                 should this only be used when absolutely necessary, |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Products found |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getProductsCommentedByUser()
----------------

**Description**: Get all products a user have commented.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/commented

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | username | [String](#string) | unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Products commented by user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getProductsLikedByUser()
----------------

**Description**: Get all products a user have liked.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/liked

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | username | [String](#string) | unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Products liked by user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getProductsRatedByUser()
----------------

**Description**: Get all products a user have rated.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/rated

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | username | [String](#string) | unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Products rated by user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getProductsThumbedByUser()
----------------

**Description**: Get all products a user have thumbed up and down.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/thumbs

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | username | [String](#string) | unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Products liked by user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getTopRatedProducts()
----------------

**Description**: Get top rate products.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/rated/top

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Most liked products found |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

geUserFavoriteProducts()
----------------

**Description**: Get favorite products for a user.

 This method not only returns the product ids but all product information.

**Implementing Class**: com.wadpam.rnr.web.ProductController

**REST path**: GET     /{domain}/product/favorites

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | username | [String](#string) | unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Users favorite products |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"commentCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"commentsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"distance"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"likeCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"likedByUser"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"FALSE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TRUE"</b>:Boolean,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:boolean</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likeRandomUsernames"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"likesURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingAverage"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingCount"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingSum"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"ratingsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsDown"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsUp"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"thumbsURL"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Resource: /person ---------------------------------------------------->
/{domain}
============

**Description**: The question controller implements all REST methods related to question and answers.

**Concrete class**: com.wadpam.rnr.web.QuestionController

<!-- Method: findByName() ---------------------------------------------------->		

addQuestion()
----------------

**Description**: Create a question and assign to a list of users.

**Implementing Class**: com.wadpam.rnr.web.QuestionController

**REST path**: POST     /{domain}/question

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | productId | [String](#string) | the product id related to the question |
| query | opUsername | [String](#string) | the username of the original poster |
| query | question | [String](#string) | the question |
| query | targetUsername | [java.lang.String[]](#javalangstring[]) | users that should receive the question |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to newly created question |

**Response Type**: [com.wadpam.rnr.json.JQuestion](#comwadpamrnrjsonjquestion)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"answer"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"opUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"parent"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"question"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"tagetUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

answerQuestion()
----------------

**Description**: Answer a question.

 The application need to get hold of the question id from one of the other
 methods first.

**Implementing Class**: com.wadpam.rnr.web.QuestionController

**REST path**: POST     /{domain}/question/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | id | [String](#string) | the unique id of the question |
| query | answer | [int](#int) | the users answer.
               The answer must be an int, e.g. -1 = NO, 1 = YES, 0 = DO N0T CARE
               It is up to the app to decide the logic and allowed values |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Question found |
| 404 | NOK | Question not found |

**Response Type**: [com.wadpam.rnr.json.JQuestion](#comwadpamrnrjsonjquestion)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"answer"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"opUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"parent"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"question"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"tagetUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteQuestion()
----------------

**Description**: Delete a question.

 Will delete both the original question and any answers.

**Implementing Class**: com.wadpam.rnr.web.QuestionController

**REST path**: DELETE     /{domain}/question/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | id | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Question found |
| 404 | NOK | Question not found |

**Response Type**: [com.wadpam.rnr.json.JQuestion](#comwadpamrnrjsonjquestion)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"answer"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"opUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"parent"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"question"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"tagetUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAnwsersToQuestion()
----------------

**Description**: Get all answers to a specific question.

 Will also returned unanswered questions.

 This request will also return the original question. The original question
 will have tbe opUsername parameter set, the answers will not.

 The question is must be obtains using one of the other methods.

**Implementing Class**: com.wadpam.rnr.web.QuestionController

**REST path**: GET     /{domain}/question/{id}/answers

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | id | [String](#string) | the unique id of the question. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Questions found |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"answer"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"opUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"parent"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"question"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"tagetUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAskedQuestions()
----------------

**Description**: Find all questions asked by a user.

 Filter based in product id.

**Implementing Class**: com.wadpam.rnr.web.QuestionController

**REST path**: GET     /{domain}/question

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | opUsername | [String](#string) | the person who asked the question |
| query | productId | [String](#string) | optional. only return questions matching the product id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Questions found |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"answer"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"opUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"parent"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"question"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"tagetUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAssignedQuestions()
----------------

**Description**: Get all questions assigned to user.

 Filter based on answer state and product id.

**Implementing Class**: com.wadpam.rnr.web.QuestionController

**REST path**: GET     /{domain}/question

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | username | [String](#string) | get all questions assigned to this user |
| query | answerState | [int](#int) | optional. only return question matching this answer state
                    1 - unanswered
                    2 - answered
                    3 - both
                    Both will be returned as default |
| query | productId | [String](#string) | optional. Only check for questions for this product id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Questions found |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"answer"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"opUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"parent"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"question"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"tagetUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getQuestion()
----------------

**Description**: Get question based on the unique question id.

**Implementing Class**: com.wadpam.rnr.web.QuestionController

**REST path**: GET     /{domain}/question/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| path | id | [String](#string) | the id of the question |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Question found |
| 404 | NOK | Question not found |

**Response Type**: [com.wadpam.rnr.json.JQuestion](#comwadpamrnrjsonjquestion)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"answer"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"opUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"parent"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"question"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"tagetUsername"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div>}
				
<!-- Resource: /person ---------------------------------------------------->
/{domain}
============

**Description**: The rating controller implements all REST methods related to ratings and reviews.

**Concrete class**: com.wadpam.rnr.web.RatingController

<!-- Method: findByName() ---------------------------------------------------->		

addRating()
----------------

**Description**: Add a rating to a product.

 This method will either redirect to the created rating or the product
 summary depending on the incoming uri.

**Implementing Class**: com.wadpam.rnr.web.RatingController

**REST path**: POST     /{domain}/rating
,     /{domain}/product/rating

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | productId | [String](#string) | domain-unique id for the product to rate |
| query | rating | [int](#int) | mandatory, the rating 0..100 |
| query | username | [String](#string) | optional. A unique user name or id.
                 Needed in order to perform user related operations later on. |
| query | latitude | [Float](#float) | optional, -90..90 |
| query | longitude | [Float](#float) | optional, -180..180 |
| query | comment | [String](#string) | optional. review comment |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to newly created rating or product summary |

**Response Type**: [com.wadpam.rnr.json.JRating](#comwadpamrnrjsonjrating)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"rating"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteLike()
----------------

**Description**: Delete a rating with a specific id.

**Implementing Class**: com.wadpam.rnr.web.RatingController

**REST path**: DELETE     /{domain}/rating/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | The unique rating id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Rating deleted |
| 404 | NOK | Rating not found and can not be deleted |

**Response Type**: [com.wadpam.rnr.json.JRating](#comwadpamrnrjsonjrating)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"rating"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAllRatingsForProduct()
----------------

**Description**: Returns all ratings for a specific product.

**Implementing Class**: com.wadpam.rnr.web.RatingController

**REST path**: GET     /{domain}/rating

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | productId | [String](#string) | the product to looks for |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Page of ratings for product |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"rating"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getHistogramForProduct()
----------------

**Description**: Returns a histogram over all ratings for a specific product.

**Implementing Class**: com.wadpam.rnr.web.RatingController

**REST path**: GET     /{domain}/rating/histogram

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | productId | [String](#string) | the product to looks for |
| query | interval | [int](#int) | Optional. the interval in the returned histogram. Default 10. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Histogram for product |

**Response Type**: [com.wadpam.rnr.json.JHistogram](#comwadpamrnrjsonjhistogram)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"histogram"</b>&nbsp;:&nbsp;{&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"interval"</b>&nbsp;:&nbsp;int,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getLike()
----------------

**Description**: Get rating details for a specific id.

**Implementing Class**: com.wadpam.rnr.web.RatingController

**REST path**: GET     /{domain}/rating/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | The unique like id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Rating found |
| 404 | NOK | Rating not found |

**Response Type**: [com.wadpam.rnr.json.JRating](#comwadpamrnrjsonjrating)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"rating"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMyRatings()
----------------

**Description**: Return all ratings done by a specific user.

**Implementing Class**: com.wadpam.rnr.web.RatingController

**REST path**: GET     /{domain}/rating

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | username | [String](#string) | a unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Ratings found for user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"comment"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"rating"</b>&nbsp;:&nbsp;Integer,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div>}
				
<!-- Resource: /person ---------------------------------------------------->
/{domain}
============

**Description**: A Thumbs and and down controller

**Concrete class**: com.wadpam.rnr.web.ThumbsController

<!-- Method: findByName() ---------------------------------------------------->		

addThumbsDown()
----------------

**Description**: Add a thumbs down.

 This method will either redirect to the created thumbs down or the product
 summary depending on the incoming uri.

**Implementing Class**: com.wadpam.rnr.web.ThumbsController

**REST path**: POST     /{domain}/thumbs/down
,     /{domain}/product/thumbs/down

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | productId | [String](#string) | domain-unique id for the product to rate |
| query | username | [String](#string) | optional. A unique user name or id.
                 Needed in order to perform user related operations later on. |
| query | latitude | [Float](#float) | optional, -90..90 |
| query | longitude | [Float](#float) | optional, -180..180 |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to newly created thumbs down or product summary |

**Response Type**: [com.wadpam.rnr.json.JThumbs](#comwadpamrnrjsonjthumbs)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"value"</b>&nbsp;:&nbsp;Integer,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

addThumbsUp()
----------------

**Description**: Add a thumbs up.

 This method will either redirect to the created thumbs up or the product
 summary depending on the incoming uri.

**Implementing Class**: com.wadpam.rnr.web.ThumbsController

**REST path**: POST     /{domain}/thumbs/up
,     /{domain}/product/thumbs/up

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | domain | [String](#string) |  |
| query | productId | [String](#string) | domain-unique id for the product to rate |
| query | username | [String](#string) | optional. A unique user name or id.
                 Needed in order to perform user related operations later on. |
| query | latitude | [Float](#float) | optional, -90..90 |
| query | longitude | [Float](#float) | optional, -180..180 |
| body (properties separately) | <a href="api.html#java.lang.String" class="link">java.lang.String</a> | [String](#string) |  |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 302 | OK | Redirect to newly created thumbs up or product summary |

**Response Type**: [com.wadpam.rnr.json.JThumbs](#comwadpamrnrjsonjthumbs)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"value"</b>&nbsp;:&nbsp;Integer,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

deleteThumbs()
----------------

**Description**: Delete a thumbs with a specific id.

**Implementing Class**: com.wadpam.rnr.web.ThumbsController

**REST path**: DELETE     /{domain}/thumbs/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | the unique thumbs id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Thumbs deleted |
| 404 | NOK | Thumbs not found and can not be deleted |

**Response Type**: [com.wadpam.rnr.json.JThumbs](#comwadpamrnrjsonjthumbs)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"value"</b>&nbsp;:&nbsp;Integer,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getAllThumbsForProduct()
----------------

**Description**: Returns all thumbs up and down for a specific product.

**Implementing Class**: com.wadpam.rnr.web.ThumbsController

**REST path**: GET     /{domain}/thumbs

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | productId | [String](#string) | the product to looks for |
| query | pagesize | [int](#int) | Optional. The number of products to return in this page. Default value is 10. |
| query | cursor | [String](#string) | Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided. |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | A page of thumbs for product |

**Response Type**: [com.wadpam.open.json.JCursorPage](#comwadpamopenjsonjcursorpage)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"value"</b>&nbsp;:&nbsp;Integer,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getMyThumbs()
----------------

**Description**: Returns all thumbs up and down done by a specific user.

**Implementing Class**: com.wadpam.rnr.web.ThumbsController

**REST path**: GET     /{domain}/thumbs

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| query | username | [String](#string) | a unique user name or id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | All thumbs for user |

**Response Type**: [java.util.Collection](#javautilcollection)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"value"</b>&nbsp;:&nbsp;Integer,</div>}
				
<!-- Method: findByName() ---------------------------------------------------->		

getThumbs()
----------------

**Description**: Get thumbs details for a specific id.

**Implementing Class**: com.wadpam.rnr.web.ThumbsController

**REST path**: GET     /{domain}/thumbs/{id}

**Request parameters**:

| Where | Name | Type | Description |
|-------|------|------|-------------|
| path | id | [long](#long) | the unique thumbs id |


**Response Codes**:

| HTTP Response Code | Message | Description |
|--------------------|---------|-------------|
| 200 | OK | Thumbs found |
| 404 | NOK | Thumbs not found |

**Response Type**: [com.wadpam.rnr.json.JThumbs](#comwadpamrnrjsonjthumbs)

**Response Example**:
{<div>&nbsp;&nbsp;&nbsp;<b>"createdBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"createdDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"id"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"location"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"latitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"longitude"</b>&nbsp;:&nbsp;{<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MAX_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_EXPONENT"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_NORMAL"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"MIN_VALUE"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NaN"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"NEGATIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"POSITIVE_INFINITY"</b>:float,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"serialVersionUID"</b>:long,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"SIZE"</b>:int,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"TYPE"</b>:Class,</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"value"</b>:float</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},</div>&nbsp;&nbsp;&nbsp;},</div><div>&nbsp;&nbsp;&nbsp;<b>"productId"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"state"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedBy"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"updatedDate"</b>&nbsp;:&nbsp;Long,</div><div>&nbsp;&nbsp;&nbsp;<b>"username"</b>&nbsp;:&nbsp;String,</div><div>&nbsp;&nbsp;&nbsp;<b>"value"</b>&nbsp;:&nbsp;Integer,</div>}
				

JSON Objects
============

<!-- JSON object: com.wadpam.open.json.JLocation ---------------------------------------------------->		
com.wadpam.open.json.JLocation
------------

| Name | Type | Description |
|------|------|-------------|
| latitude | [java.lang.Float](#javalangfloat) | 
 |
| longitude | [java.lang.Float](#javalangfloat) | 
 |
<!-- JSON object: com.wadpam.rnr.json.JComment ---------------------------------------------------->		
com.wadpam.rnr.json.JComment
------------

| Name | Type | Description |
|------|------|-------------|
| productId | [java.lang.String](#javalangstring) | The Many-To-One productId (unconstrained)
 |
| username | [java.lang.String](#javalangstring) | The Many-To-One username (unconstrained)
 |
| location | [com.wadpam.open.json.JLocation](#comwadpamopenjsonjlocation) | The location of the product
 |
| comment | [java.lang.String](#javalangstring) | A user-provided comment
 |
<!-- JSON object: com.wadpam.rnr.json.JFavorites ---------------------------------------------------->		
com.wadpam.rnr.json.JFavorites
------------

| Name | Type | Description |
|------|------|-------------|
| username | [java.lang.String](#javalangstring) | The user name
 |
| productIds | [java.util.Collection](#javautilcollection) | The users favorite products ids
 |
<!-- JSON object: com.wadpam.rnr.json.JFeedback ---------------------------------------------------->		
com.wadpam.rnr.json.JFeedback
------------

| Name | Type | Description |
|------|------|-------------|
| category | [java.lang.String](#javalangstring) | Feedback category
 |
| deviceModel | [java.lang.String](#javalangstring) | Device model
 |
| deviceOS | [java.lang.String](#javalangstring) | Device OS
 |
| deviceOSVersion | [java.lang.String](#javalangstring) | Device OS version
 |
| userContact | [java.lang.String](#javalangstring) | user email of the person providing the feedback
 |
| feedback | [java.lang.String](#javalangstring) | the feedback main description
 |
| location | [com.wadpam.open.json.JLocation](#comwadpamopenjsonjlocation) | The location of the user when the feedback was given
 |
| referenceId | [java.lang.String](#javalangstring) | A referenceId selected by the app
 |
| title | [java.lang.String](#javalangstring) | The feedback title
 |
| username | [java.lang.String](#javalangstring) | User name of id of the person providing the feedback
 |
<!-- JSON object: com.wadpam.rnr.json.JHistogram ---------------------------------------------------->		
com.wadpam.rnr.json.JHistogram
------------

| Name | Type | Description |
|------|------|-------------|
| interval | [int](#int) | The interval used in the returned histogram
 |
| histogram | [java.util.Map](#javautilmap) | The histogram
 |
<!-- JSON object: com.wadpam.rnr.json.JInappropriate ---------------------------------------------------->		
com.wadpam.rnr.json.JInappropriate
------------

| Name | Type | Description |
|------|------|-------------|
<!-- JSON object: com.wadpam.rnr.json.JLike ---------------------------------------------------->		
com.wadpam.rnr.json.JLike
------------

| Name | Type | Description |
|------|------|-------------|
| productId | [java.lang.String](#javalangstring) | The Many-To-One productId (unconstrained)
 |
| username | [java.lang.String](#javalangstring) | The Many-To-One username (unconstrained)
 |
| location | [com.wadpam.open.json.JLocation](#comwadpamopenjsonjlocation) | The location of the product
 |
<!-- JSON object: com.wadpam.rnr.json.JProduct ---------------------------------------------------->		
com.wadpam.rnr.json.JProduct
------------

| Name | Type | Description |
|------|------|-------------|
| location | [com.wadpam.open.json.JLocation](#comwadpamopenjsonjlocation) | The location of the product
 |
| distance | [java.lang.Float](#javalangfloat) | The distance in km between the product location and the device provided position.
 If either the device position or product location is unknown the distance will not be calculated.
 This property will be set in nearby searches.
 |
| ratingCount | [java.lang.Long](#javalanglong) | The total number of ratings
 |
| ratingSum | [java.lang.Long](#javalanglong) | The total sum or all ratings
 |
| ratingAverage | [java.lang.Integer](#javalanginteger) | The calculated average rating. Normalize to a 0-100 scale.
 |
| ratingsURL | [java.lang.String](#javalangstring) | The deep link to the individual ratings
 |
| likeCount | [java.lang.Long](#javalanglong) | The total number of Likes
 |
| likesURL | [java.lang.String](#javalangstring) | The deep link to the individual likes
 |
| commentCount | [java.lang.Long](#javalanglong) | The total number of Comments
 |
| commentsURL | [java.lang.String](#javalangstring) | The deep link to the individual likes
 |
| thumbsDown | [java.lang.Long](#javalanglong) | The number of thumbs up
 |
| thumbsUp | [java.lang.Long](#javalanglong) | The number of thumbs up
 |
| thumbsURL | [java.lang.String](#javalangstring) | The deep link to the individual thumbs
 |
| likeRandomUsernames | [java.util.Collection](#javautilcollection) | List of semi random users that liked this product
 |
| likedByUser | [java.lang.Boolean](#javalangboolean) | Set if the product has been liked by current user
 |
<!-- JSON object: com.wadpam.rnr.json.JQuestion ---------------------------------------------------->		
com.wadpam.rnr.json.JQuestion
------------

| Name | Type | Description |
|------|------|-------------|
| answer | [java.lang.Long](#javalanglong) | Targeted users answer
 |
| opUsername | [java.lang.String](#javalangstring) | The original poster username
 |
| parent | [java.lang.Object](#javalangobject) | 
 |
| productId | [java.lang.String](#javalangstring) | The product id related to the question
 |
| question | [java.lang.String](#javalangstring) | The question asked
 |
| tagetUsername | [java.lang.String](#javalangstring) | The user being asked the question
 |
<!-- JSON object: com.wadpam.rnr.json.JRating ---------------------------------------------------->		
com.wadpam.rnr.json.JRating
------------

| Name | Type | Description |
|------|------|-------------|
| productId | [java.lang.String](#javalangstring) | The Many-To-One productId (unconstrained)
 |
| username | [java.lang.String](#javalangstring) | The Many-To-One username (unconstrained)
 |
| location | [com.wadpam.open.json.JLocation](#comwadpamopenjsonjlocation) | The location of the product
 |
| rating | [java.lang.Integer](#javalanginteger) | A user-provided integer rating for a piece of content. Normalized to a 0-100 scale.
 |
| comment | [java.lang.String](#javalangstring) | A user-provided review comment
 |
<!-- JSON object: com.wadpam.rnr.json.JThumbs ---------------------------------------------------->		
com.wadpam.rnr.json.JThumbs
------------

| Name | Type | Description |
|------|------|-------------|
| location | [com.wadpam.open.json.JLocation](#comwadpamopenjsonjlocation) | The location of the product
 |
| productId | [java.lang.String](#javalangstring) | The Many-To-One productId (unconstrained)
 |
| username | [java.lang.String](#javalangstring) | The Many-To-One username (unconstrained)
 |
| value | [java.lang.Integer](#javalanginteger) | The value of the thumb.
 +1 for thumbs up
 -1 for thumbs down
 |
<!-- JSON object: java.lang.Boolean ---------------------------------------------------->		
java.lang.Boolean
------------

| Name | Type | Description |
|------|------|-------------|
| boolean | [boolean](#boolean) |  |
<!-- JSON object: java.lang.Float ---------------------------------------------------->		
java.lang.Float
------------

| Name | Type | Description |
|------|------|-------------|
| naN | [boolean](#boolean) |  |
| infinite | [boolean](#boolean) |  |
| naN | [boolean](#boolean) |  |
| infinite | [boolean](#boolean) |  |
<!-- JSON object: java.lang.Integer ---------------------------------------------------->		
java.lang.Integer
------------

| Name | Type | Description |
|------|------|-------------|
| chars | [void](#void) |  |
| andRemoveCacheProperties | [void](#void) |  |
| integer | [java.lang.Integer](#javalanginteger) |  |
| integer | [java.lang.Integer](#javalanginteger) |  |
| integer | [java.lang.Integer](#javalanginteger) |  |
<!-- JSON object: java.lang.Long ---------------------------------------------------->		
java.lang.Long
------------

| Name | Type | Description |
|------|------|-------------|
| chars | [void](#void) |  |
| long | [java.lang.Long](#javalanglong) |  |
| long | [java.lang.Long](#javalanglong) |  |
| long | [java.lang.Long](#javalanglong) |  |
<!-- JSON object: java.lang.Object ---------------------------------------------------->		
java.lang.Object
------------

| Name | Type | Description |
|------|------|-------------|
| class | [java.lang.Class](#javalangclass) |  |
<!-- JSON object: java.lang.String ---------------------------------------------------->		
java.lang.String
------------

| Name | Type | Description |
|------|------|-------------|
| empty | [boolean](#boolean) |  |
| chars | [void](#void) |  |
| chars | [void](#void) |  |
| bytes | [void](#void) |  |
| bytes | [byte](#byte) |  |
| bytes | [byte](#byte) |  |
| bytes | [byte](#byte) |  |
<!-- JSON object: java.lang.Void ---------------------------------------------------->		
java.lang.Void
------------

| Name | Type | Description |
|------|------|-------------|
<!-- JSON object: java.util.Collection ---------------------------------------------------->		
java.util.Collection
------------

| Name | Type | Description |
|------|------|-------------|
| empty | [boolean](#boolean) |  |
<!-- JSON object: java.util.Map ---------------------------------------------------->		
java.util.Map
------------

| Name | Type | Description |
|------|------|-------------|
| empty | [boolean](#boolean) |  |
|  | [java.lang.Object](#javalangobject) |  |
<!-- JSON object: org.springframework.web.servlet.view.RedirectView ---------------------------------------------------->		
org.springframework.web.servlet.view.RedirectView
------------

| Name | Type | Description |
|------|------|-------------|
| redirectView | [boolean](#boolean) |  |
| contextRequired | [boolean](#boolean) |  |
| currentRequestUriVariables | [java.util.Map](#javautilmap) |  |
| eligibleProperty | [boolean](#boolean) |  |
| eligibleValue | [boolean](#boolean) |  |
| http11StatusCode | [org.springframework.http.HttpStatus](#orgspringframeworkhttphttpstatus) |  |

