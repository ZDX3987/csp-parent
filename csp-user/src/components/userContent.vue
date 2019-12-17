<template>
<div>
	<ul id="myTab" class="nav nav-tabs">
		<li class="active">
			<a href="#main_info" data-toggle="tab">
				 基本信息
			</a>
		</li>
		<li><a href="#pass_info" data-toggle="tab">更改密码</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="main_info">
			<div v-if="!isModify">
				<h3 class="title">基本信息</h3>
				<button class="btn_modify" @click="modifyInfo">修改资料</button>
				<div class="content">
					<ul>
						<li><span>用户名/昵称：</span>{{userData.username}}</li>
						<li><span>注册时间：</span>{{userData.createTime | dateFormat("yyyy-MM-dd")}}</li>
					</ul>
					<ul>
						<li><span>注册邮箱：</span>{{userData.email}}</li>
						<li><span>个性签名：</span>{{userData.introduce}}</li>
					</ul>
				</div>
			</div>
			<div v-if="isModify">
				<h3 class="title">修改资料</h3>
				<button class="btn_modify" @click="submit">完成</button>
				<div class="content-md">
					<form id="user-form" role="form" enctype="multipart/form-data">
						<div class="form-group">
							<label for="" class="col-sm-3 control-label">用户名&nbsp;<b>*</b></label>
							<div class="col-sm-9 form-item">
								<input type="text" name="username" class="form-control" v-model="userData.username">
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-3 control-label">个人简介&nbsp;<b>*</b></label>
							<div class="col-sm-9 form-item">
								<input type="text" name="introduce" class="form-control" v-model="userData.introduce">
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-3 control-label">邮箱&nbsp;<b>*</b></label>
							<div class="col-sm-9 form-item">
								<input type="text" id="email_input" name="email" class="form-control" v-model="userData.email" @change="emailChange">
							</div>
						</div>
						<div class="form-group" v-if="code_flag">
							<label for="" class="col-sm-3 control-label">验证码&nbsp;<b>*</b></label>
							<div class="col-sm-9 form-item">
								<input type="text" class="form-control check_code" v-model="code" placeholder="请输入验证码">
								<button @click="getCheckCode" class="form-control get_code" :disabled="codeClicked">{{codeMsg}}</button>
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-3 control-label">头像&nbsp;<b>*</b></label>
							<div class="col-sm-9 form-item upload-header">
								<img id="show_img" src="../../static/images/add_header.jpg"/>
								<input type="file" name="file" accept="image/*" id="upload_img" @change="uploadImg">
								点击上传或者修改头像
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="tab-pane fade" id="pass_info">
			<div>
				<h3 class="title">修改密码</h3>
				<button class="btn_modify" @click="changePassword()">确认修改</button>
				<div class="content-pass">
					<form role="form">
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
				</div>
			</div>
		</div>
	</div>
</div>
</template>

<script>
export default {
	name: 'UserContent',
	data () {
		return {
			isModify: false,
			userData: JSON.parse(localStorage.getItem("userData")),
			codeMsg: '获取验证码',
			code_flag: false,
			oldEmail: null,
			codeClicked: false,
			file: null,
			password: null,
			repassword: null,
			hasError: false,
			errorMsg: '这是错误信息',
			code: null
		}
	},
	created() {
		this.oldEmail = this.userData.email;
	},
	methods: {
		//判断是否为修改资料
		modifyInfo: function() {
			this.isModify = !this.isModify
		},
		//上传头像预览
		uploadImg: function() {
			var reader = new FileReader();
			reader.readAsDataURL($("#upload_img")[0].files[0]);
			var _this = this
			reader.onload = function (evt) {
                $("#show_img")[0].src = evt.target.result;
				_this.file = $("#upload_img")[0].files[0]
            }
		},
		//判断是否需要更改邮箱
		emailChange: function() {	
			if (this.userData.email!=this.oldEmail) {
				this.code_flag = true;
				return;
			}
			this.code_flag = false;
			return;
		},
		//获取验证码
		getCheckCode: function() {
			this.codeClicked = true;
			//请求后台发送邮箱验证码
			this.$axios.post('/api/user/sendCode', {
				email: this.userData.email                                                                                 
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
			var form = new FormData();
			form.append('id', this.userData.id);
			form.append('file', this.file);
			var config = {
				headers: {
					 'Content-Type': 'multipart/form-data'
				}
			}
			this.$axios.post('/api/user/modify', this.userData).then(res=>{
				if (res.data.status == 200) {
					if (this.file != null) {
						this.$axios.post('api/user/uploadImg', form, config).then(data=>{
							
						})
					}
					
					this.$axios.post('api/user/getUserInfo', {
						id: this.userData.id,
					}).then(info=> {
						var userData = info.data.data;
						localStorage.setItem("userData", JSON.stringify(userData));
						this.$parent.userData = userData;
						this.$parent.$parent.userData = userData;
					})
					this.isModify = !this.isModify	
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
					email: this.userData.email,
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
.title {
	color: #55bd66;
	margin-left: 20px;
	margin-bottom: 40px;
}
.content {
	margin-left: 20px;
	width: 800px; height: 150px;
	background: rgb(239,239,239);
}
.content ul {
	list-style: none;
	float: left;
	width: 400px; height: 120px;
}
.content ul li {
	color: #666;
	height: 60px;
	line-height: 60px;
	margin-left: 30px;
}
.content ul li span {
	color: rgb(165,153,153);
	display: inline-block;
	width: 120px;
}
.btn_modify {
	background: #55bd66;
	color: #FFF;
	width: 100px; height: 40px;
	border: none;
	right: 102px; margin-top: -70px;
	position: absolute;
	transition: all 0.8s;
}
.btn_modify:hover {
	background: #5bae69;
}

.content-md {
	margin-left: 20px;
	width: 800px; height: 400px;
	background: rgb(239,239,239);
	padding-top: 30px;
}
.control-label {
	margin-left: 20px;
	margin-right: -50px;
}
#myTab {
	height: 60px;
	background: rgb(250,250,250);
	border: none;
}
#myTab li {
	height: 60px;
	border: none;
	background: rgb(250,250,250);
}
#myTab .active{
	border-top: 2px solid #55bd66;
}
#myTab a{
	border: none;
	height: 60px;
	line-height: 40px;
}
.upload-header {
	height: 100px;
}
.upload-header input {
	display: block;
	width: 80px; height: 80px;
	position: absolute;
	left: 16px; top: 0;
	opacity: 0;
}
.upload-header img {
	width: 80px; height: 80px;
}
.content-pass {
	margin-left: 20px;
	width: 800px; height: 350px;
	background: rgb(239,239,239);
	padding-top: 30px;
}
.wain-info {
	margin-top: -20px;
	width: 380px;
	height: 40px;
	line-height: 10px;
}
</style>
