<template>
	<div class="modal-content regist-win">
		<div class="modal-header regist-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h2 class="modal-title" id="myModalLabel">注册</h2>
		</div>
		<div class="modal-body regist-body">
			<form class="" role="form">
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">用户名&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="text" class="form-control" v-model="username" @blur="checkUserName" placeholder="请输入用户名">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">密码&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="password" class="form-control" v-model="password" @blur="checkPassword" placeholder="请输入密码">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">验证密码&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="password" class="form-control" v-model="repassword" @blur="checkPassword" placeholder="请输入密码">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">邮箱&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="text" class="form-control" v-model="email" @focus="checkEmail" placeholder="请输入邮箱">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">验证码&nbsp;<b>*</b></label>
					<div class="col-sm-9 form-item">
						<input type="text" class="form-control check_code" v-model="code" placeholder="请输入验证码">
						<button @click="getCheckCode" class="form-control get_code" :disabled="codeClicked">{{codeMsg}}</button>
					</div>
				</div>
				<p v-if="hasError" class="wain-info col-sm-offset-1 alert alert-danger">{{message}}</p>
				<div class="form-group">
					<div class="col-sm-12 form-item submit">
						<button type="submit" class="btn btn-default col-sm-12" :disabled="hasError" @click="submit">注册</button>
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer regist-footer">
			<a href="#">注册</a>&nbsp;|&nbsp;<a href="#">忘记密码</a>
		</div>
	</div><!-- /.modal-content -->
</template>

<script>
export default {
	name: 'Regist',
	data: function() {
		return {
			message: '这是错误信息',
			hasError: false,
			username: null,
			password: null,
			repassword: null,
			email: null,
			code: null,
			codeMsg: '获取验证码',
			codeClicked: false
		}
	},
	methods: {
		checkUserName: function() {
			if(!this.username) {
				this.message = '用户名不能为空！';
				this.hasError = true;
				return;
			}
			this.hasError = false;
			
		},
		checkPassword: function() {
			if(!this.password) {
				this.message = '密码不能为空！';
				this.hasError = true;
				return;
			}
			else if(this.repassword != this.password) {
				this.message = '两次输入密码不一致！';
				this.hasError = true;
				return;
			}
			this.hasError = false;
			
		},
		checkEmail: function() {
			if (!this.email) {
				this.message =  '邮箱不能为空！';
				this.hasError = true;
				return;
			}
			this.hasError = false;
		},
		getCheckCode: function() {
			if (!this.email) {
				this.message = '邮箱不能为空';
				this.hasError = true;
				return;
			}
			this.hasError = false;
			this.codeClicked = true;
			/* var second = 5;
			var _this = this
			var interval = setInterval(function() {
				if (second == 0) {
					_this.codeMsg = '获取验证码';
					clearInterval(interval);
					_this.codeClicked = false;
				}else{
				   _this.codeMsg = (second--) + '秒有效';
				}
			}, 1000); */
			
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
		submit: function() {
			
			if (!this.username || !this.password || !this.email) {
				console.info(666)
				this.hasError = true;
				this.message = '请完整填写信息！';
				return;
			}
			
			this.$axios.post('/api/user/register', {
				user: {
					username: this.username,
					password: this.password,
					email: this.email
				},
				code: this.code																							
			}).then(res=>{
				console.info(res.data);
			})
		}
	}
}
</script>

<style>
.regist-win {
	border: none;
	border-radius: 0;
	height: 550px;
	margin-top:40px;
}
.regist-header {
	border: none;
}
.regist-header button {
	font-size: 32px;
}
.regist-header button:hover {
	color: rgb(255,0,0);
}
.regist-header h2 {
	text-align: center;
}
.regist-body {
	padding: 0 auto;
	width: 80%;
	margin: 0 auto;
	overflow: hidden;
}
b {
	color: #f00;
	font-size: 16px;
}
.regist-footer {
	border: none;
	text-align: center;
	padding-top: 0;
}
.regist-footer a {	
	text-decoration: none;
	color: rgb(153,153,153);
}
.regist-footer a:hover {	
	color: #55bd66;
}
.form-group {
	overflow: hidden;
}
.wain-info {
	margin-top: -20px;
	width: 380px;
	height: 40px;
	line-height: 10px;
}
.form-item {
	height: 45px;
}
.check_code {
	width: 150px;
}
.get_code {
	width: 130px;
	position: relative;
	top: -34px; left: 175px;
	background: rgb(239,239,239);
	transition: all 0.5s;
}
.get_code:hover {
	background: #fff;
}

.submit button {
	height: 40px;
	background: #55bd66;
	font-size: 18px;
	color: #fff;
	transition: all 0.5s;
}
.submit button:hover {
	background: #5bae69;
	color: #fff;
}
.submit button:focus {
	background: #55bd66;
	color: #fff;
}
</style>
