jQuery.ajaxSetup( {
	cache: false,
	data: null
});

function backendAPI() {
	this.backendUrl = "${baseUrl}";

	this.no_more_requests = false;

	this.makeRequest = function(type,url,dataType,data,errMsg) {
		if(!this.no_more_requests){
			var response = null;
			var request = jQuery.ajax({
				async: false,
				type: type,
				url: this.backendUrl + url,
				crossDomain: true,
				accept: "*", 
				data: data,
				dataType: dataType,
				success: function(data, textStatus, jqXHR) {
					response = data;
				},
				error: function(jqXHR, textStatus, errorThrown) {
				},
				complete: function(){
					return this;
				}
			});
			if(request.status != 200){
				return this.handleError(request,errMsg);
			}else{
				return response;
			}

		}
	};
	this.handleError = function(request,errMsg){
		switch(request.status){
			case 401:
				alert('Your current session has expired please log back in.');
				this.no_more_requests = true;
				location.replace('/');
				break;
			default:
				if(typeof(errMsg) != 'undefined'){
					if(typeof(errMsg) == "object"){
						if(typeof(errMsg[request.status]) != 'undefined'){
							alert(errMsg[request.status]);
						}else{
							alert('An error occured, please try again.');
						}
					}else{
						alert(errMsg);
					}
				}else{
					alert('An error occured, please try again.');
				}
		}
		return 'error';
	}
}

    

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to comment"
			"dataType":"String"
			"paramType":"query"
		"name":"comment"
			"description":"the comment"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional. -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 6
*/
backendAPI.prototype.addComment = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/comment";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to comment"
			"dataType":"String"
			"paramType":"query"
		"name":"comment"
			"description":"the comment"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional. -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 6
*/
backendAPI.prototype.addComment = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/product/comment";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"The unique comment id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.deleteComment = function(id) {
	var type = "DELETE";
	var data = {};



	var url = "{domain}/comment/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"productId"
			"description":"the product to look for"
			"dataType":"String"
			"paramType":"query"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getAllCommentsForProduct = function(data) {
	var type = "GET";
	var url = "{domain}/comment";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"The unique comment id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.getComment = function(id) {
	var type = "GET";
	var data = {};



	var url = "{domain}/comment/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"username"
			"description":"a unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 1
*/
backendAPI.prototype.getMyComments = function(data) {
	var type = "GET";
	var url = "{domain}/comment";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to add as favorites"
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.addFavorite = function(domain,username,data) {
	var type = "POST";






	var url = ""+domain+"/favorites/"+username;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to add as favorites"
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.deleteFavorite = function(domain,username,data) {
	var type = "DELETE";






	var url = ""+domain+"/favorites/"+username;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.getFavorites = function(username) {
	var type = "GET";
	var data = {};



	var url = "{domain}/favorites/"+username;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"title"
			"description":"the feedback title"
			"dataType":"String"
			"paramType":"query"
		"name":"feedback"
			"description":"the feedback main description"
			"dataType":"String"
			"paramType":"query"
		"name":"referenceId"
			"description":"a referenceId selected by the app. Could be reference to a
                    product id, application id or other content.
                    Must be unique within the app context."
			"dataType":"String"
			"paramType":"query"
		"name":"category"
			"description":"an optional category to allow the user the categorize the feedback e.g.
                 "quality", "service", price. Can also be used to allow better post processing
                 of the feedback."
			"dataType":"String"
			"paramType":"query"
		"name":"deviceModel"
			"description":"optional device model"
			"dataType":"String"
			"paramType":"query"
		"name":"deviceOS"
			"description":"optional device OS"
			"dataType":"String"
			"paramType":"query"
		"name":"deviceOSVersion"
			"description":"optional device OS version"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional user name of id of the person providing the feedback"
			"dataType":"String"
			"paramType":"query"
		"name":"userContact"
			"description":"optional user contact data of the person providing the feedback.
                    This can be used
              to get back to the user."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional latitude of the user when the feedback was given"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional longitude of the user when the feedback was given"
			"dataType":"Float"
			"paramType":"query"
count = 12
*/
backendAPI.prototype.addFeedback = function(domain,data) {
	var type = "POST";



	var url = "/"+domain+"/feedback";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"id"
			"description":"the feedback to delete"
			"dataType":"Long"
			"paramType":"path"
count = 2
*/
backendAPI.prototype.deleteFeedback = function(domain,id) {
	var type = "*";
	var data = {};






	var url = "/"+domain+"/feedback/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"timestamp"
			"description":"Optional. Delete user feedback created/updated before
                  this timestamp"
			"dataType":"Long"
			"paramType":"query"
count = 2
*/
backendAPI.prototype.deleteListOfFeedback = function(domain,data) {
	var type = "*";



	var url = "/"+domain+"/feedback";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"email"
			"description":"the email address to send the exported feedback to"
			"dataType":"String"
			"paramType":"query"
		"name":"timestamp"
			"description":"Optional. Export user feedback created/updated after
                  this timestamp."
			"dataType":"Long"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.exportFeedback = function(domain,data) {
	var type = "GET";



	var url = "/"+domain+"/feedback/export";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":"the domain"
			"dataType":"String"
			"paramType":"path"
		"name":"email"
			"description":"The email the exported CSV file should be sent to"
			"dataType":"String"
			"paramType":"query"
		"name":"timestamp"
			"description":"export user feedback create/updated after the
                  provided timestamp"
			"dataType":"Long"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.exportFeedbackWorker = function(domain,data) {
	var type = "POST";



	var url = "/_worker/"+domain+"/feedback/export";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"id"
			"description":"the feedback to get"
			"dataType":"Long"
			"paramType":"path"
count = 2
*/
backendAPI.prototype.getFeedback = function(domain,id) {
	var type = "GET";
	var data = {};






	var url = "/"+domain+"/feedback/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"id"
			"description":"the report to get"
			"dataType":"Long"
			"paramType":"path"
count = 2
*/
backendAPI.prototype.getInapproriate = function(domain,id) {
	var type = "GET";
	var data = {};






	var url = "/"+domain+"/inappropriate/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"referenceId"
			"description":"a referenceId selected by the app. Could be reference to a
                    product, application, comment, image etc. Must be unique within the app context."
			"dataType":"String"
			"paramType":"query"
		"name":"referenceDescription"
			"description":"An optional description of the reference id. This will allow for more easy
                             reading during the moderation."
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"Optional user name"
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional latitude of the user when the report was done"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional longitude of the user when the report was done"
			"dataType":"Float"
			"paramType":"query"
count = 6
*/
backendAPI.prototype.inappropriate = function(domain,data) {
	var type = "POST";



	var url = "/"+domain+"/inappropriate";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to like"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 5
*/
backendAPI.prototype.addLike = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/like";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to like"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 5
*/
backendAPI.prototype.addLike = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/product/like";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"The unique like id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.deleteLike = function(id) {
	var type = "DELETE";
	var data = {};



	var url = "{domain}/like/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"productId"
			"description":"the product to looks for"
			"dataType":"String"
			"paramType":"query"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getAllLikesForProduct = function(data) {
	var type = "GET";
	var url = "{domain}/like";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"The unique like id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.getLike = function(id) {
	var type = "GET";
	var data = {};



	var url = "{domain}/like/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"username"
			"description":"a unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 1
*/
backendAPI.prototype.getMyLikes = function(data) {
	var type = "GET";
	var url = "{domain}/like";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, the latitude to search around"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, the longitude to search around"
			"dataType":"Float"
			"paramType":"query"
		"name":"radius"
			"description":"optional. The radius to search with in. Default value 3000m"
			"dataType":"int"
			"paramType":"query"
		"name":"sort"
			"description":"optional, the sort order of the returned results
             0 - distance, default value
             1 - average rating
             2 - number of likes
             3 - number of thumbs up"
			"dataType":"int"
			"paramType":"query"
count = 7
*/
backendAPI.prototype.findNearbyProducts = function(domain,data) {
	var type = "GET";



	var url = "product/nearby";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getAllProducts = function(domain,data) {
	var type = "GET";



	var url = "product/";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getMostCommentedProducts = function(domain,data) {
	var type = "GET";



	var url = "product/commented/most";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getMostLikedProducts = function(domain,data) {
	var type = "GET";



	var url = "product/liked/most";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getMostRatedProducts = function(domain,data) {
	var type = "GET";



	var url = "product/rated/most";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getMostThumbsDownProducts = function(domain,data) {
	var type = "GET";



	var url = "product/thumbs/down/most";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getMostThumbsUpProducts = function(domain,data) {
	var type = "GET";



	var url = "product/thumbs/up/most";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product"
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"Optional. If a user name is provided the service will check of the user
                 has previously liked this product and will return this in the response.
                 Please not that this will make the request slower and cost more money and
                 should this only be used when absolutely necessary,"
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getProductInfo = function(domain,productId,data) {
	var type = "GET";






	var url = "product/"+productId;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"ids"
			"description":"a list of productIds"
			"dataType":"java.lang.String[]"
			"paramType":"query"
		"name":"username"
			"description":"Optional. If a user name is provided the service will check of the user
                 has previously liked this product and will return this in the response.
                 Please not that this will make the request slower and cost more money and
                 should this only be used when absolutely necessary,"
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getProducts = function(domain,data) {
	var type = "GET";



	var url = "product/";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 2
*/
backendAPI.prototype.getProductsCommentedByUser = function(domain,data) {
	var type = "GET";



	var url = "product/commented";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 2
*/
backendAPI.prototype.getProductsLikedByUser = function(domain,data) {
	var type = "GET";



	var url = "product/liked";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 2
*/
backendAPI.prototype.getProductsRatedByUser = function(domain,data) {
	var type = "GET";



	var url = "product/rated";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 2
*/
backendAPI.prototype.getProductsThumbedByUser = function(domain,data) {
	var type = "GET";



	var url = "product/thumbs";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getTopRatedProducts = function(domain,data) {
	var type = "GET";



	var url = "product/rated/top";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 2
*/
backendAPI.prototype.geUserFavoriteProducts = function(domain,data) {
	var type = "GET";



	var url = "product/favorites";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"the product id related to the question"
			"dataType":"String"
			"paramType":"query"
		"name":"opUsername"
			"description":"the username of the original poster"
			"dataType":"String"
			"paramType":"query"
		"name":"question"
			"description":"the question"
			"dataType":"String"
			"paramType":"query"
		"name":"targetUsername"
			"description":"users that should receive the question"
			"dataType":"java.lang.String[]"
			"paramType":"query"
count = 5
*/
backendAPI.prototype.addQuestion = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/question";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"id"
			"description":"the unique id of the question"
			"dataType":"String"
			"paramType":"path"
		"name":"answer"
			"description":"the users answer.
               The answer must be an int, e.g. -1 = NO, 1 = YES, 0 = DO N0T CARE
               It is up to the app to decide the logic and allowed values"
			"dataType":"int"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.answerQuestion = function(domain,id,data) {
	var type = "POST";






	var url = ""+domain+"/question/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"id"
			"description":""
			"dataType":"String"
			"paramType":"path"
count = 2
*/
backendAPI.prototype.deleteQuestion = function(domain,id) {
	var type = "DELETE";
	var data = {};






	var url = ""+domain+"/question/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"id"
			"description":"the unique id of the question."
			"dataType":"String"
			"paramType":"path"
count = 2
*/
backendAPI.prototype.getAnwsersToQuestion = function(domain,id) {
	var type = "GET";
	var data = {};






	var url = ""+domain+"/question/"+id+"/answers";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"opUsername"
			"description":"the person who asked the question"
			"dataType":"String"
			"paramType":"query"
		"name":"productId"
			"description":"optional. only return questions matching the product id"
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getAskedQuestions = function(domain,data) {
	var type = "GET";



	var url = ""+domain+"/question";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"username"
			"description":"get all questions assigned to this user"
			"dataType":"String"
			"paramType":"query"
		"name":"answerState"
			"description":"optional. only return question matching this answer state
                    1 - unanswered
                    2 - answered
                    3 - both
                    Both will be returned as default"
			"dataType":"int"
			"paramType":"query"
		"name":"productId"
			"description":"optional. Only check for questions for this product id"
			"dataType":"String"
			"paramType":"query"
count = 4
*/
backendAPI.prototype.getAssignedQuestions = function(domain,data) {
	var type = "GET";



	var url = ""+domain+"/question";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"id"
			"description":"the id of the question"
			"dataType":"String"
			"paramType":"path"
count = 2
*/
backendAPI.prototype.getQuestion = function(domain,id) {
	var type = "GET";
	var data = {};






	var url = ""+domain+"/question/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to rate"
			"dataType":"String"
			"paramType":"query"
		"name":"rating"
			"description":"mandatory, the rating 0..100"
			"dataType":"int"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
		"name":"comment"
			"description":"optional. review comment"
			"dataType":"String"
			"paramType":"query"
count = 7
*/
backendAPI.prototype.addRating = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/rating";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to rate"
			"dataType":"String"
			"paramType":"query"
		"name":"rating"
			"description":"mandatory, the rating 0..100"
			"dataType":"int"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
		"name":"comment"
			"description":"optional. review comment"
			"dataType":"String"
			"paramType":"query"
count = 7
*/
backendAPI.prototype.addRating = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/product/rating";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"The unique rating id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.deleteLike = function(id) {
	var type = "DELETE";
	var data = {};



	var url = "{domain}/rating/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"productId"
			"description":"the product to looks for"
			"dataType":"String"
			"paramType":"query"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getAllRatingsForProduct = function(data) {
	var type = "GET";
	var url = "{domain}/rating";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"productId"
			"description":"the product to looks for"
			"dataType":"String"
			"paramType":"query"
		"name":"interval"
			"description":"Optional. the interval in the returned histogram. Default 10."
			"dataType":"int"
			"paramType":"query"
count = 2
*/
backendAPI.prototype.getHistogramForProduct = function(data) {
	var type = "GET";
	var url = "{domain}/rating/histogram";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"The unique like id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.getLike = function(id) {
	var type = "GET";
	var data = {};



	var url = "{domain}/rating/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"username"
			"description":"a unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 1
*/
backendAPI.prototype.getMyRatings = function(data) {
	var type = "GET";
	var url = "{domain}/rating";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to rate"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 5
*/
backendAPI.prototype.addThumbsDown = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/thumbs/down";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to rate"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 5
*/
backendAPI.prototype.addThumbsDown = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/product/thumbs/down";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to rate"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 5
*/
backendAPI.prototype.addThumbsUp = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/thumbs/up";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"domain"
			"description":""
			"dataType":"String"
			"paramType":"path"
		"name":"productId"
			"description":"domain-unique id for the product to rate"
			"dataType":"String"
			"paramType":"query"
		"name":"username"
			"description":"optional. A unique user name or id.
                 Needed in order to perform user related operations later on."
			"dataType":"String"
			"paramType":"query"
		"name":"latitude"
			"description":"optional, -90..90"
			"dataType":"Float"
			"paramType":"query"
		"name":"longitude"
			"description":"optional, -180..180"
			"dataType":"Float"
			"paramType":"query"
count = 5
*/
backendAPI.prototype.addThumbsUp = function(domain,data) {
	var type = "POST";



	var url = ""+domain+"/product/thumbs/up";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"the unique thumbs id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.deleteThumbs = function(id) {
	var type = "DELETE";
	var data = {};



	var url = "{domain}/thumbs/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"productId"
			"description":"the product to looks for"
			"dataType":"String"
			"paramType":"query"
		"name":"pagesize"
			"description":"Optional. The number of products to return in this page. Default value is 10."
			"dataType":"int"
			"paramType":"query"
		"name":"cursor"
			"description":"Optional. The current cursor position during pagination.
               The next page will be return from this position.
               If asking for the first page, not cursor should be provided."
			"dataType":"String"
			"paramType":"query"
count = 3
*/
backendAPI.prototype.getAllThumbsForProduct = function(data) {
	var type = "GET";
	var url = "{domain}/thumbs";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"username"
			"description":"a unique user name or id"
			"dataType":"String"
			"paramType":"query"
count = 1
*/
backendAPI.prototype.getMyThumbs = function(data) {
	var type = "GET";
	var url = "{domain}/thumbs";
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};

/*
variables
		"name":"id"
			"description":"the unique thumbs id"
			"dataType":"long"
			"paramType":"path"
count = 1
*/
backendAPI.prototype.getThumbs = function(id) {
	var type = "GET";
	var data = {};



	var url = "{domain}/thumbs/"+id;
	var dataType = "json";
	//var errMsg = [];
	//var response = this.makeRequest(type,url,dataType,data,errMsg);
	var response = this.makeRequest(type,url,dataType,data);
	return response;
};


var projectAPI = new backendAPI();
