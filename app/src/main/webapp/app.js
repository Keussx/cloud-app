/**
 * After the client library has loaded, this init() function is called. The
 */

var ingredientsSelection = [];
var recipesSelection = [];

function init() {

	// rootpath will evaulate to either of these, depending on where the app is
	// running:
	// //localhost:8080/_ah/api
	// //your-app-id/_ah/api

	var rootpath = "http://localhost:8080/_ah/api";

	// Load the bookrecommenderApi API
	gapi.client.load('ohmyrecipesAPI', 'v1', loadCallback, rootpath);
}

/*
 * When API has loaded, this callback is called.
 */
function loadCallback() {

	// Enable the button actions
	enableButtons();
}

function enableButtons() {
	// Set the onclick action for the first button
	var btn = document.getElementById("getRecipes");
	btn.onclick = function() {
		sendGetRecipesRequest();
	};

	// Update the button label now that the button is active
	btn.value = "Click to Find Recipes";
}

function sendGetRecipesRequest() {

	//Create json string for ingredients
	var jsonIngredients = '{"meat", "apples", "lemon"}';
    var jsonIngredients = '{"meat", "apples", "lemon"}';
    var jsonIngredients = '{"meat", "apples", "lemon"}';

	// create request after conversion to base64 string
	var request = gapi.client.ohmyrecipesAPI.getRecipes({
		'ingredients' : json
	});

	request.execute(function(resp) {
		handleResultsCallback(resp);
	});
}

// Process the JSON response
function handleResultsCallback(response) {
	console.log(response.message);
}

function addIngredientToSelection(ingredient) {
	var index = array.indexOf(ingredient);

	//add item only if not exist
	if (index < 0) {
		ingredientsSelection.push(ingredient);
	}

}

function removeIngredientFromSelection(ingredient) {
	var index = array.indexOf(ingredient);

	//remove item at index only if exist
	if (index > -1) {
		ingredientsSelection.splice(index, 1);
	}
}

//polyfill for indexOf
Array.prototype.indexOf || (Array.prototype.indexOf = function(d, e) {
	var a;
	if (null == this)
		throw new TypeError('"this" is null or not defined');
	var c = Object(this), b = c.length >>> 0;
	if (0 === b)
		return -1;
	a = +e || 0;
	Infinity === Math.abs(a) && (a = 0);
	if (a >= b)
		return -1;
	for (a = Math.max(0 <= a ? a : b - Math.abs(a), 0); a < b;) {
		if (a in c && c[a] === d)
			return a;
		a++
	}
	return -1
});
