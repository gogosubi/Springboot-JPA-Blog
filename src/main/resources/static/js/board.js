let index = {
	init: function() {
		// function을 사용하지 않고 ()=>{}를 사용하는 이유는 this를 바인딩하기 위해서!
		$("#btn-save").on("click", () => {
			this.save();
		});
		$("#btn-delete").on("click", () => {
			this.deleteById();
		});
		$("#btn-update").on("click", () => {
			this.update();
		});
		$("#btn-reply-save").on("click", () => {
			this.replySave();
		});
	},

	save: function() {
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};

		console.log(data)
		// ajax호출시 default가 비동기 호출
		// ajax통신을 이용하여 3개의 data를 json으로 변경하여 insert요청
		$.ajax({
			type: "POST",
			url: "/api/board",
			// http Body 데이터
			data: JSON.stringify(data),
			// Body 데이터 타입
			contentType: "application/json",
			// 응답받을 데이터 형태 정의(Default:String), 수신받은 데이터 형태가 json이라면 javascript로 변경 
			dataType: "json"
		}).done(function(resp) {
			alert("글쓰기가 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
	
	, deleteById: function() {	
		let id = $("#id").text(); // .val(); // Delete시에 value값으로 하니 삭제 되지 않음.

		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id, 
			dataType: "json"
		}).done(function(resp) {
			alert("삭제가 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
	
	, update: function() {
		let id = $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};

		console.log(data)
		// ajax호출시 default가 비동기 호출
		// ajax통신을 이용하여 3개의 data를 json으로 변경하여 insert요청
		$.ajax({
			type: "PUT",
			url: "/api/board/" + id,
			// http Body 데이터
			data: JSON.stringify(data),
			// Body 데이터 타입
			contentType: "application/json",
			// 응답받을 데이터 형태 정의(Default:String), 수신받은 데이터 형태가 json이라면 javascript로 변경 
			dataType: "json"
		}).done(function(resp) {
			alert("글 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	replySave: function() {
		let data = {
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val()
		};
		
		// let boardId = $("#boardId").val(); // Dto 변경전 

		console.log(data)
		// ajax호출시 default가 비동기 호출
		// ajax통신을 이용하여 3개의 data를 json으로 변경하여 insert요청
		$.ajax({
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			// http Body 데이터
			data: JSON.stringify(data),
			// Body 데이터 타입
			contentType: "application/json",
			// 응답받을 데이터 형태 정의(Default:String), 수신받은 데이터 형태가 json이라면 javascript로 변경 
			dataType: "json"
		}).done(function(resp) {
			alert("댓글작성이 완료되었습니다.");
			location.href = `/board/${data.boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
	
	, replyDelete: function(boardId, replyId) {	
		$.ajax({
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`, 
			dataType: "json"
		}).done(function(resp) {
			alert("댓글삭제성공");
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
}

index.init();