/**
* controller.js 
*/
(function($j) {
	
	// MOH
	$j.extend(spinsci, {

	
	getAllUsers: function(criteria, callback) {
		var params = $j.extend({
			type: "GET",
			data: criteria,
			url: "rs/getAllUsers?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
	},
	getUserAppAccess: function(criteria, callback) {
		var params = $j.extend({
			type: "GET",
			data: criteria,
			url: "rs/getUserAppAccess?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
		
	},
	
	submitAdminData: function(queryString, callback){
		var params = $j.extend({
			type: "GET",
			data: queryString,
			url: "rs/submitAdminData?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
	},
	submitHolidayData: function(queryString, callback){
		var params = $j.extend({
			type: "GET",
			data: queryString,
			url: "rs/updateHoliday?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
	},
	accessAuthDAOById:function(queryString, callback){
		var params = $j.extend({
			type: "GET",
			data: queryString,
			async:false,
			url: "rs/accessAuthDAOById?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
	},
	
	getIsCCOpen:function(queryString, callback){
		var params = $j.extend({
			type: "GET",
			data: queryString,
			async:false,
			url: "rs/isccopen?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
	}
	
	});
	
})(jQuery);


