<template>
<div class="container-fluid">
	<div class="modify-content">
		<h3 class="title">修改密码</h3>
		<div class="content-pass">
			<form role="form">
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">邮箱&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="text" class="form-control" v-model="email" placeholder="请输入密码">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">密码&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="password" class="form-control" v-model="password" placeholder="请输入密码">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">验证密码&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="password" class="form-control" v-model="repassword" placeholder="请输入密码">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">验证码&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="text" class="form-control check_code" v-model="code" placeholder="请输入验证码">
						<button @click="getCheckCode" class="form-control get_code" :disabled="codeClicked">{{codeMsg}}</button>
					</div>
				</div>
				<p v-if="hasError" class="wain-info col-sm-offset-1 alert alert-danger">{{errorMsg}}</p>
			</form>
			<button class="btn_modify" @click="changePassword()">确认修改</button>
		</div>
	</div>
</div>
</template>

<script>
export default {
	name: 'modifyPass',
	data() {
		return {
			email: null,
			codeMsg: '获取验证码',
			codeClicked: false,
			password: null,
			repassword: null,
			hasError: false,
			errorMsg: '这是错误信息',
			code: null
		}
	},
	methods: {
		//获取验证码
		getCheckCode: function() {
			this.codeClicked = true;
			//请求后台发送邮箱验证码
			this.$axios.post('/api/user/sendCode', {
				email: this.email                                                                                 
			}).then(res=>{
				//验证码请求成功
				if (res.data.status == 200) {
					//开始倒计时数秒
					var second = 89;
					var _this = this
					var interval = setInterval(function() {
						if (second == 0) {
							_this.codeMsg = '获取验证码';
							clearInterval(interval);
							_this.codeClicked = false;
						} else {
							_this.codeMsg = (second--) + '秒有效';
						}
						
					}, 1000);
				}
			});
			
		},
		changePassword: function() {
			if (this.password == null || this.repassword == null) {
				this.hasError = true;
				this.errorMsg = '密码不能为空！'
				return;
			}
			else if (this.password != this.repassword) {
				this.hasError = true;
				this.errorMsg = '两次输入的密码不一样！'
				return;
			}
			else if (this.code == null) {
				this.hasError = true;
				this.errorMsg = '请获取验证码！'
				return;
			}
			else {
				this.hasError = false;
				this.$axios.post('/api/user/password', {
					email: this.email,
					password: this.password,
					code: this.code
				}).then(res=>{
					if (res.data.status != 200) {
						this.hasError = true;
						this.errorMsg = res.data.msg
						return;
					}
					window.location.reload();
				})
			}
			
		}
	}
}
</script>

<style>
.container-fluid {
	padding: 0 70px;
}
.modify-content {
	background: #FFF;
	width: 80%; height: 500px;
	margin-top: 20px;
	padding: 10px 0;
}
.title {
	color: #55bd66;
	margin-left: 40px;
	margin-bottom: 20px;
}

.btn_modify {
	background: #55bd66;
	color: #FFF;
	width: 100px; height: 40px;
	border: none;
	transition: all 0.8s;
	margin-left: 230px;
}
.btn_modify:hover {
	background: #5bae69;
}
.content-pass {
	margin-left: 20px;
	width: 800px; height: 350px;
	padding-top: 30px;
}
.wain-info {
	margin-top: -20px;
	width: 380px;
	height: 40px;
	line-height: 10px;
}
</style>
