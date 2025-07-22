$(document).ready(function () {
	const urlParams = new URLSearchParams(window.location.search);
	const postId = urlParams.get("postId");
	
	$.ajax({
		url: "controller",
		method: "GET",
	    data: {
	      cmd: "commentCount",
	      postId: postId
	    },
	    success: function (responseText) {
	    	$(".comment-count").text(responseText.commentCount);
	    }
	})
})