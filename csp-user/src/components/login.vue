<template>
	<div class="modal-content login-win">
		<div class="modal-header login-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h2 class="modal-title" id="myModalLabel">登录</h2>
		</div>
		<div class="modal-body login-body">
			<form class="" role="form" onsubmit="return false;">
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">用户名/邮箱</label>
					<div class="col-sm-9 form-item">
						<input type="text" class="form-control" v-model="username" placeholder="请输入用户名或邮箱">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-3 control-label">密码</label>
					<div class="col-sm-9 form-item">
						<input type="password" class="form-control" placeholder="请输入密码" v-model="password" @blur="blur()">
					</div>
				</div>
				<p v-if="hasError" class="wain-info col-sm-offset-1 alert alert-danger">{{message}}</p>
				<div class="form-group">
					<div class="col-sm-12 form-item submit">
						<button type="submit" class="btn btn-default col-sm-12" @click="login" :disabled="hasError" 
						:data-dismiss="modal">登录</button>
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer login-footer">
			<router-link :to="{name:'modifyPass'}">忘记密码</router-link>
		</div>
	</div><!-- /.modal-content -->
</template>

<script>
export default {
	name: 'Login',
	data() {
		return {
			message: '这是错误信息！',
			password: null,
			username: null,
			hasError: false,
			userToken: null,
			modal: null
		}
	},
	methods: {
		login:function() {
			this.$axios.post('/api/user/login', {
				username: this.username,
				password: this.password																					
			}).then(res=>{
				if (res.data.status == 200) {
					this.$axios.post('api/user/getUser', {
						token: res.data.data.userToken
					}).then(data=>{
						if(!this.hasError) {
							var userData = data.data.data;
							this.$emit('changeLogin');
							localStorage.setItem("userData", JSON.stringify(userData));
							//存cookie
							this.addCookie(res.data.data.userToken)
						}
					})
				}
				else {
					this.message = res.data.msg;
					this.hasError = true;
				}
			})
		},
		blur() {
			if(!this.password) {
				this.message = '密码不能为空';
				this.hasError = true;
			}
			else {
				this.hasError = false;
			}
		},
		addCookie: function(value) {
			var days = 90;
			var exp = new Date();
			exp.setDate(exp.getTime()+days*24*60*60*1000);
			document.cookie = "userToken=" + value + ";expires=" +exp.toGMTString();
		}
	}
};
</script>

<style>
.login-win {
	border: none;
	border-radius: 0;
	height: 450px;
	margin-top: 80px;
}
.login-header {
	border: none;
}
.login-header button {
	font-size: 32px;
}
.login-header button:hover {
	color: rgb(255,0,0);
}
.login-header h2 {
	text-align: center;
}
.login-body {
	padding: 0 auto;
	width: 80%;
	margin: 0 auto;
	overflow: hidden;
}
.login-footer {
	border: none;
	text-align: center;
}
.login-footer a {	
	text-decoration: none;
	color: rgb(153,153,153);
}
.login-footer a:hover {	
	color: #55bd66;
}

.form-item {
	height: 60px;
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
.submit {
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
