let index = {
	init: function() {
		// function을 사용하지 않고 ()=>{}를 사용하는 이유는 this를 바인딩하기 위해서!
		$("#btn-save").on("click", () => {
			this.save();
		});	
		// function을 사용하지 않고 ()=>{}를 사용하는 이유는 this를 바인딩하기 위해서!
		$("#btn-update").on("click", () => {
			this.update();
		});	
		/*
		 * 전통적인 방식의 로그인 방법(springboot security를 이용하면서 삭제)	
		$("#btn-login").on("click", () => {
			this.login();
		});
		*/
	},

	save: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		console.log(data)
		// ajax호출시 default가 비동기 호출
		// ajax통신을 이용하여 3개의 data를 json으로 변경하여 insert요청
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			// http Body 데이터
			data: JSON.stringify(data),
			// Body 데이터 타입
			contentType: "application/json",
			// 응답받을 데이터 형태 정의(Default:String), 수신받은 데이터 형태가 json이라면 javascript로 변경 
			dataType: "json"
		}).done(function(resp) {
			alert("회원가입이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	update: function() {
		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		console.log(data)
		// ajax호출시 default가 비동기 호출
		// ajax통신을 이용하여 3개의 data를 json으로 변경하여 insert요청
		$.ajax({
			type: "PUT",
			url: "/user",
			// http Body 데이터
			data: JSON.stringify(data),
			// Body 데이터 타입
			contentType: "application/json",
			// 응답받을 데이터 형태 정의(Default:String), 수신받은 데이터 형태가 json이라면 javascript로 변경 
			dataType: "json"
		}).done(function(resp) {
			alert("회원 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
	
	/*
	 * 전통적인 방식의 로그인 방법(springboot security를 이용하면서 삭제)
	, login: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val()
		};

		console.log(data)
		// ajax호출시 default가 비동기 호출
		// ajax통신을 이용하여 3개의 data를 json으로 변경하여 insert요청
		$.ajax({
			type: "POST",
			url: "/api/user/login",
			// http Body 데이터
			data: JSON.stringify(data),
			// Body 데이터 타입
			contentType: "application/json; charset=utf-8",
			// 응답받을 데이터 형태 정의(Default:String), 수신받은 데이터 형태가 json이라면 javascript로 변경 
			dataType: "json"
		}).done(function(resp) {
			alert("로그인이 완료되었습니다.");
			console.log(data);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
	*/
}

index.init();