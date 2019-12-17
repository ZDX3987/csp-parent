<template>
	<div id="app">
		<div class="bg-body"></div>
		<div id="nav-container" class="nav-container">
			<nav class="navbar navbar-default main-nav" role="navigation">
				<div class="container-fluid">
					<div class="navbar-header main-icon">
						<a class="navbar-brand" href="#">
							<img :src="icon_school" />
						</a>
					</div>
					<div>
						<ul class="nav navbar-nav nav-content">
							<li><router-link :to="{name:'index'}">首页</router-link></li>
							<li><router-link :to="{path:'/cate/2.html'}">社区动态</router-link></li>
							<li><router-link :to="{path:'/cate/1.html'}">学习教程</router-link></li>
							<li><router-link :to="{path:'/cate/3.html'}">视频</router-link></li>
						</ul>
					</div>
					<ul v-if="!login_flag" class="nav navbar-nav navbar-right">
						<li><a href="#" data-toggle="modal" data-target="#myModal" @click="isLogin=false"><span class="glyphicon glyphicon-user"></span> 注册</a></li>
						<li><a href="#" data-toggle="modal" data-target="#myModal" @click="isLogin=true"><span class="glyphicon glyphicon-log-in"></span> 登录</a></li>
					</ul>
					<ul id="user-info" v-if="login_flag" @mouseenter="showUserList" @mouseleave="hideUserList" @click="toUserInfo" class="nav navbar-nav navbar-right">
						<li class="user-head">
							<img :src="userData.imgUrl" />
						</li>
						<li class="user-name">
							<h5>{{userData.username}}</h5>&nbsp;
							<i v-if="!userHover" class="fa fa-chevron-down"></i>
							<i v-if="userHover" class="fa fa-chevron-up"></i>
						</li>
						<li>
							<ul class="user-list">
								<li><a @click="logout"><span class="fa fa-close"></span>退出</a></li>
							</ul>
						</li>
					</ul>
				</div>
			</nav>
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog" v-if="isLogin==false">
					<Regist/>
				</div><!-- /.modal -->
				<div class="modal-dialog" v-else>
					<Login @changeLogin='change'/>
				</div><!-- /.modal -->
			</div>
		</div>
		<!--路由组件-->
		<router-view class="router" :key="key" />
		
		<div class="footer">
			<div class="left col-sm-5 col-sm-offset-1">
				<ul>
					<li><a href="https://spring.io/projects/spring-boot/">SpringBoot</a></li>
					<li><a href="https://cn.vuejs.org/">Vue官网</a></li>
					<li><a href="https://spring.io/projects/spring-cloud">SpringCloud</a></li>
					<li><a href="https://www.javazhiyin.com/">Java知音</a></li>
				</ul>
				<p>网站维护微信：ZDX_3987  反馈建议：zdx3987@163.com</p>
				<p>Copyright © 2019 CSP.保留所有权利. </p>
			</div>
			<div class="right col-sm-4 col-sm-offset-2">
				<ul>
					<li><a href="tencent://message/?uin=153226256&Site=在线咨询&Menu=yes"><span class="fa fa-qq"></span></a></li>
					<li class="wechat-li">
						<span class="fa fa-wechat"></span>
						<div class="wechat">
							<img src="../static/images/wechat_qr.jpg" title="欢迎关注微信公众号" />
						</div>
					</li>
				</ul>
			</div>
		</div>
		
		<div class="back-top">
			<a title="返回顶部">
				<span class="top-icon fa fa-angle-up"></span>
				<span class="top-text">返回顶部</span>
			</a>
		</div>
	</div>
</template>

<script>
	import icon_school from "../static/images/icon_title.jpg"
	import Login from "@/components/Login"
	import Regist from "@/components/Regist"
	import '../static/js/app.js'
	
	export default {
		name: 'App',
		components: {
			Login,
			Regist
		},
		data() {
			return {
				icon_school,
				//标识登录或者注册窗口
				isLogin: false,
				//标识是否登录
				login_flag: false,
				//标识鼠标是否移入显示信息
				userHover: false,
				userData: JSON.parse(localStorage.getItem("userData"))
			}
		},
		computed: {
			key(){
				return this.$route.path + Math.random();
			}
		},
		created() {
			if (this.userData.imgUrl == null || this.userData.imgUrl == '') {
				this.userData.imgUrl = './static/images/user_default.jpg'
			}
			
			if (this.getCookie("userToken")!="") {
				this.$axios.post('api/user/getUser', {
					token: this.getCookie("userToken")
				}).then(data=>{
					this.userData = data.data.data;
					localStorage.setItem('login_flag', JSON.stringify(true));
					$("#myModal").modal('hide');
					localStorage.setItem("userData", JSON.stringify(this.userData));
				
				})
			}
			else {
				localStorage.setItem('login_flag', JSON.stringify(false));
			}

			//获取session中的登录状态
			this.login_flag = JSON.parse(localStorage.getItem("login_flag"));
			if (this.userData!=null) {
				if (this.userData.imgUrl == '' || this.userData.imgUrl == null) {
					this.userData.imgUrl = '../static/images/user_default.jpg'
				}
			}
			
		},
		methods: {
			change: function() {
				localStorage.setItem('login_flag', JSON.stringify(true));
				this.login_flag = true;
				$("#myModal").modal('hide');
				this.userData = JSON.parse(localStorage.getItem("userData"))
				window.location.reload()
			},
			getCookie: function(cookieName) {
				var theCookie = "" + document.cookie;
				var ind = theCookie.indexOf(cookieName);
				if(ind==-1 || cookieName=="") return "";
				var ind1 = theCookie.indexOf(';',ind);
				if(ind1==-1) ind1 = theCookie.length;
				return theCookie.substring(ind+cookieName.length+1,ind1);
			},
			showUserList: function() {
				this.userHover = !this.userHover
			},
			hideUserList: function() {
				this.userHover = !this.userHover
			},
			toUserInfo: function() {
				//跳转路由
				this.$router.push({
					name: 'UserContent'
				})
			},
			//退出账号
			logout: function() {
				localStorage.setItem('login_flag', JSON.stringify(false))
				localStorage.removeItem('userData');
				var exp = new Date();
				exp.setTime(exp.getTime() - 1);
				var cval=this.getCookie("userToken");
				if(cval!=null) {
					document.cookie= "userToken="+cval+";expires="+exp.toGMTString();
				}
				this.$axios.post('/api/user/logout', {
					token: cval
				}).then(res=>{
					if (res.data.status == 200) {
						window.location.href = '/'
					}
				})
				
			}
		}
	}
	
</script>

<style>
	@import url("../static/css/app.css");
</style>
