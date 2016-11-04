/**
 * 
 */
var userInfo = getParams();
var userNick = userInfo.nick
$("#userNick").text(userNick);

function getParams() {
	var param = new Array();
	var url = decodeURIComponent(location.href);
	url = decodeURIComponent(url);
	var params = url.substring( url.indexOf('?')+1, url.length );
	params = params.split("#");
	var size = params.length;
	var key, value;
	for(var i=0 ; i < size ; i++) {
		key = params[i].split("=")[0];
		value = params[i].split("=")[1];

		param[key] = value;
	}
	return param;
}
aJaxOtherUserProfileLoder(userInfo);

function aJaxOtherUserProfileLoder(userInfo) {

	$.ajax({
		url:serverAddr+"/membership/otherUserInfoDetail.json",
		type: "GET",
		dataType: "json",
		data: {nickname:userInfo.nick},
		//	  {vo객체 프라퍼티명 : 데이터}
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success: function(obj) {
			var result = obj.jsonResult
			if (result.state != "success"){
				alert("조회 실패!")
				return
			}
			$("#userProfilePhotos").attr('src',"/TeamProject/upload/"+result.data.profilePhoto)
			$("#viewCount").text(result.data.totalViewCount)
			$("#followers").text(result.data.totalFollowers)
			$("#user-introduce").text(result.data.userInfo)
			
			var source = $('#liTemplateText').html();
			var template = Handlebars.compile(source);
			var data = result.data.boardInfo
			data.stringify = JSON.stringify(data);
			var boards = template(data);
			$("#post_wrapper").append(boards);
			
			
			$(".titleLink").click(function(event){
				$("#yourModal").modal();
				$("html").css({"overflow":"hidden"});
				 var no = $(this).attr("data-no")
				ajaxLoadBoard(no);
				ajaxPostComentsList(no)
			})
			
			
			
		},
		error: function(result) {
			console.log(result.state)
		}
	})

}





