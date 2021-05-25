let index = {
	init:function()
	{
		// function을 사용하지 않고 ()=>{}를 사용하는 이유는 this를 바인딩하기 위해서!
		$("#btn-save").on("click",()=>{
			this.save();	
		});
	},
	
	save:function()
	{
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			eamil: $("#email").val()
		};
		
		//console.log(data)
		// ajax호출시 default가 비동기 호출
		// ajax통신을 이용하여 3개의 data를 json으로 변경하여 insert요청
		$.ajax({
			type: "POST",
			url: "/blog/api/user",
			// http Body 데이터
			data: JSON.stringify(data), 
			// Body 데이터 타입
			contentType: "application/json", 
			// 응답받을 데이터 형태 정의(Default:String), 수신받은 데이터 형태가 json이라면 javascript로 변경 
			dataType: "json" 
		}).done(function(resp){
			alert("회원가입이 완료되었습니다.");
			location.href = "/blog";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	}
}

index.init();