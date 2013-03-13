




//Get data
function backendAPI_admin_getAllCommentsForProduct(){
	//getData
	var data = {};
	data.productId = $("#productId").val();
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getAllCommentsForProduct(data);
	//add data to table
	$("#JComment").tmpl(data).appendTo("#getAllCommentsForProduct-JComment-output");
}

//Get data
function backendAPI_admin_getComment(id){
	//getData
	var data = projectAPI.getComment(id);
	//add data to table
	$("#JComment").tmpl(data).appendTo("#getComment-JComment-output");
}

//Get data
function backendAPI_admin_getMyComments(){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getMyComments(data);
	//add data to table
	$("#JComment").tmpl(data).appendTo("#getMyComments-JComment-output");
}

//Get data
function backendAPI_admin_getFavorites(username){
	//getData
	var data = projectAPI.getFavorites(username);
	//add data to table
	$("#JFavorites").tmpl(data).appendTo("#getFavorites-JFavorites-output");
}

//Get data
function backendAPI_admin_exportFeedback(domain){
	//getData
	var data = {};
	data.email = $("#email").val();
	data.timestamp = $("#timestamp").val();
	data = projectAPI.exportFeedback(data,domain);
	//add data to table
	$("#Void").tmpl(data).appendTo("#exportFeedback-Void-output");
}

//Get data
function backendAPI_admin_getFeedback(domain,id){
	//getData
	var data = projectAPI.getFeedback(domain,id);
	//add data to table
	$("#JFeedback").tmpl(data).appendTo("#getFeedback-JFeedback-output");
}

//Get data
function backendAPI_admin_getInapproriate(domain,id){
	//getData
	var data = projectAPI.getInapproriate(domain,id);
	//add data to table
	$("#JInappropriate").tmpl(data).appendTo("#getInapproriate-JInappropriate-output");
}

//Get data
function backendAPI_admin_getAllLikesForProduct(){
	//getData
	var data = {};
	data.productId = $("#productId").val();
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getAllLikesForProduct(data);
	//add data to table
	$("#JLike").tmpl(data).appendTo("#getAllLikesForProduct-JLike-output");
}

//Get data
function backendAPI_admin_getLike(id){
	//getData
	var data = projectAPI.getLike(id);
	//add data to table
	$("#JLike").tmpl(data).appendTo("#getLike-JLike-output");
}

//Get data
function backendAPI_admin_getMyLikes(){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getMyLikes(data);
	//add data to table
	$("#JLike").tmpl(data).appendTo("#getMyLikes-JLike-output");
}

//Get data
function backendAPI_admin_findNearbyProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data.latitude = $("#latitude").val();
	data.longitude = $("#longitude").val();
	data.radius = $("#radius").val();
	data.sort = $("#sort").val();
	data = projectAPI.findNearbyProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#findNearbyProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getAllProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getAllProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getAllProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getMostCommentedProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getMostCommentedProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getMostCommentedProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getMostLikedProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getMostLikedProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getMostLikedProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getMostRatedProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getMostRatedProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getMostRatedProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getMostThumbsDownProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getMostThumbsDownProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getMostThumbsDownProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getMostThumbsUpProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getMostThumbsUpProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getMostThumbsUpProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getProductInfo(domain,productId){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getProductInfo(data,domain,productId);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getProductInfo-JProduct-output");
}

//Get data
function backendAPI_admin_getProducts(domain){
	//getData
	var data = {};
	data.ids = $("#ids").val();
	data.username = $("#username").val();
	data = projectAPI.getProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getProductsCommentedByUser(domain){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getProductsCommentedByUser(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getProductsCommentedByUser-JProduct-output");
}

//Get data
function backendAPI_admin_getProductsLikedByUser(domain){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getProductsLikedByUser(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getProductsLikedByUser-JProduct-output");
}

//Get data
function backendAPI_admin_getProductsRatedByUser(domain){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getProductsRatedByUser(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getProductsRatedByUser-JProduct-output");
}

//Get data
function backendAPI_admin_getProductsThumbedByUser(domain){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getProductsThumbedByUser(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getProductsThumbedByUser-JProduct-output");
}

//Get data
function backendAPI_admin_getTopRatedProducts(domain){
	//getData
	var data = {};
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getTopRatedProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#getTopRatedProducts-JProduct-output");
}

//Get data
function backendAPI_admin_geUserFavoriteProducts(domain){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.geUserFavoriteProducts(data,domain);
	//add data to table
	$("#JProduct").tmpl(data).appendTo("#geUserFavoriteProducts-JProduct-output");
}

//Get data
function backendAPI_admin_getAnwsersToQuestion(domain,id){
	//getData
	var data = projectAPI.getAnwsersToQuestion(domain,id);
	//add data to table
	$("#JQuestion").tmpl(data).appendTo("#getAnwsersToQuestion-JQuestion-output");
}

//Get data
function backendAPI_admin_getAskedQuestions(domain){
	//getData
	var data = {};
	data.opUsername = $("#opUsername").val();
	data.productId = $("#productId").val();
	data = projectAPI.getAskedQuestions(data,domain);
	//add data to table
	$("#JQuestion").tmpl(data).appendTo("#getAskedQuestions-JQuestion-output");
}

//Get data
function backendAPI_admin_getAssignedQuestions(domain){
	//getData
	var data = {};
	data.username = $("#username").val();
	data.answerState = $("#answerState").val();
	data.productId = $("#productId").val();
	data = projectAPI.getAssignedQuestions(data,domain);
	//add data to table
	$("#JQuestion").tmpl(data).appendTo("#getAssignedQuestions-JQuestion-output");
}

//Get data
function backendAPI_admin_getQuestion(domain,id){
	//getData
	var data = projectAPI.getQuestion(domain,id);
	//add data to table
	$("#JQuestion").tmpl(data).appendTo("#getQuestion-JQuestion-output");
}

//Get data
function backendAPI_admin_getAllRatingsForProduct(){
	//getData
	var data = {};
	data.productId = $("#productId").val();
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getAllRatingsForProduct(data);
	//add data to table
	$("#JRating").tmpl(data).appendTo("#getAllRatingsForProduct-JRating-output");
}

//Get data
function backendAPI_admin_getHistogramForProduct(){
	//getData
	var data = {};
	data.productId = $("#productId").val();
	data.interval = $("#interval").val();
	data = projectAPI.getHistogramForProduct(data);
	//add data to table
	$("#JHistogram").tmpl(data).appendTo("#getHistogramForProduct-JHistogram-output");
}

//Get data
function backendAPI_admin_getLike(id){
	//getData
	var data = projectAPI.getLike(id);
	//add data to table
	$("#JRating").tmpl(data).appendTo("#getLike-JRating-output");
}

//Get data
function backendAPI_admin_getMyRatings(){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getMyRatings(data);
	//add data to table
	$("#JRating").tmpl(data).appendTo("#getMyRatings-JRating-output");
}

//Get data
function backendAPI_admin_getAllThumbsForProduct(){
	//getData
	var data = {};
	data.productId = $("#productId").val();
	data.pagesize = $("#pagesize").val();
	data.cursor = $("#cursor").val();
	data = projectAPI.getAllThumbsForProduct(data);
	//add data to table
	$("#JThumbs").tmpl(data).appendTo("#getAllThumbsForProduct-JThumbs-output");
}

//Get data
function backendAPI_admin_getMyThumbs(){
	//getData
	var data = {};
	data.username = $("#username").val();
	data = projectAPI.getMyThumbs(data);
	//add data to table
	$("#JThumbs").tmpl(data).appendTo("#getMyThumbs-JThumbs-output");
}

//Get data
function backendAPI_admin_getThumbs(id){
	//getData
	var data = projectAPI.getThumbs(id);
	//add data to table
	$("#JThumbs").tmpl(data).appendTo("#getThumbs-JThumbs-output");
}


//TODO: generate sortable tables for get functions
//TODO: generate css file
//TODO: generate forms for post and put
//TODO: link all sections to menu system




