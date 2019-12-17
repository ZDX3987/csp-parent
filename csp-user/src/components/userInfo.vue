<template>
<div class="container-fluid">
	<div class="auth-header">
		<div class="user-content">
			<img :src="userData.imgUrl" />
			<h1>{{userData.username}}</h1>
			<p>{{userData.introduce}}</p>
		</div>
	</div>
	<div class="auth-list">
		<ul>
			<li v-for="l in listArr"><router-link :to="{name:l.router}"><span :class="l.icon"></span>&nbsp;{{l.text}}</router-link></li>
		</ul>
	</div>
	<div class="auth-content">
		<router-view/>
	</div>
</div>
</template>

<script>
export default {
	name: 'UserInfo',
	data () {
		return {
			//列表信息
			listArr: [
				{router:'UserContent', icon:'fa fa-user-o', text:'资料'},
				{router:'Write', icon:'fa fa-list', text:'发文'},
				{router:'collection', icon:'fa fa-heart-o', text:'收藏'},
				{router:'friends', icon:'fa fa-users', text:'好友'}
			],
			userData: JSON.parse(localStorage.getItem("userData"))
		}
	},
	created() {
		if (this.userData.imgUrl == null || this.userData.imgUrl == '') {
			this.userData.imgUrl = '../static/images/user_default.jpg'
		}
	},
	methods: {
		
	}
}

</script>

<style>
.container-fluid {
	padding: 0 70px;
}
.auth-header {
	background-image: url(../../static/images/img-author.jpg);
	background-position: center;
	width: 100%; height: 300px;
	color: rgb(255,255,255);
}
.user-content {
	width: 300px; height: 300px;
	text-align: center;
	margin: 0 auto;
	padding-top: 20px;
}
.user-content img{
	width: 100px; height: 100px;
	border-radius: 50%;
	border: 6px solid rgba(239,239,239,0.3);
}
.user-content h1 {
	font-weight: 300;
	font-size: 32px;
	margin-bottom: 10px;
}
.auth-list {
	width: 300px; height: 240px;
	background: #FFF;
	position: absolute;
	left: 70px; top: 420px;
}
.auth-list ul {
	width: 100%; height: 600px;
	list-style: none;
	
}
.auth-list li {
	height: 60px; width: 100%;
	line-height: 60px;
	padding-left: 40px;
	border-top: 1px solid #eee;
	border-left: 3px solid #FFF;
	transition: all 0.8s; 
}
.auth-list li a {
	font-size: 16px;
	color: #333;
	text-decoration: none;
}
.auth-list li:hover {
	border-left: 3px solid #55bd66;
}
.auth-list li:hover a {
	color: #55bd66;
}
.auth-list li span {
	margin-right: 20px;
}
.auth-content {
	width: 850px;
	padding-bottom: 20px;
	background: #FFF;
	margin-left: 350px;
	margin-top: 40px;
	
}
</style>
