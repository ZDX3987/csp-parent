<template>
<div class="collect-content">
	<h3>收藏</h3>
	<div class="collect-list">
		<ul>
			<li v-for="item in articleList">
				<article class="article-item">
					<router-link :to="{path:'/article/'+item.id+'.html'}" class="article-img">
						<img :src="item.imgUrl" :title="item.title" />
					</router-link>
					<div class="artile-info">
						<h2><router-link :to="{path:'/article/'+item.id+'.html'}" :title="item.title">{{item.title}}</router-link></h2>
						<div class="excerpt">{{item.digest}}</div>
						<div class="post-info">
							<div class="left">
								
								<span class="article-date">
									<b class="fa fa-calendar-o"></b>
									{{item.createTime | dateFormat('yyyy-MM-dd')}}
								</span>
							</div>
							<div class="right">
								<span class="article-comment">
									<b class="fa fa-comments-o"></b>
									20
								</span>
								<span class="article-like">
									<b class="fa fa-heart-o"></b>
									{{item.parise}}
								</span>
							</div>
						</div>
					</div>
				</article>
			</li>
		</ul>
	</div>
</div>
</template>

<script>
export default {
	name: 'collection',
	data() {
		return {
			articleList: null,
			userData: JSON.parse(localStorage.getItem("userData"))
		}
	},
	created() {
		this.$axios.post('/api/article/getCollect', {
			uId: this.userData.id
		}).then(res=>{
			if (res.data.status == 200) {
				this.articleList = res.data.data
			}
		})
	}
}
</script>

<style>
.collect-content {
	padding: 20px;
}
.collect-content h3 {
	margin: 0;
	color: #55bd66;
	margin-bottom: 20px;
}
.collect-list {
	
}
.collect-list li {
	list-style: none;
	margin-bottom: 20px;
}
.article-item {
	height: 160px; padding: 20px auto;
}
.article-img {
	display: inline-block;
	width: 30%; height: 160px;
	margin-left: 20px; float: left;
	position: relative;
	background: #000;
}
.article-img img {
	width: 100%; height: 160px;
	position: absolute;
	left: 0; top: 0;
	transition: all 0.5s; 
}
.article-img:hover img {
	opacity: 0.5;
}
.article-img span {
	display: inline-block;
	width: 50px; height: 20px;
	color: #FFF; font-size: 12px;
	background: rgba(76,76,76,0.8);
	position: absolute;
	left: 10px; top: 10px;
	text-align: center; line-height: 20px;
}
.artile-info {
	width: 60%; height: 160px;
	position: relative; float: left;
	margin-left: 20px;
	padding: 10px 0;
}
.artile-info h2 {
	margin: 0;
	font-size: 18px;
	font-weight: normal;
	height: 30px;
	line-height: 30px;
	margin-bottom: 10px;
}
.artile-info h2 a {
	text-decoration: none;
	color: #333;	
}
.excerpt {
	display: block;
	font-size: 13px;
	color: #666;
	font-weight: 300;
	letter-spacing: 3px;
	margin-bottom: 30px;
}
.post-info {
	height: 30px;
	color: #999;
	line-height: 30px;
}
.post-info .left {
	width: 80%; height: 30px;
	float: left;
}
.post-info .article-author {
	width: 20%; 
	margin-right: 10px;
}
.article-author img {
	width: 25px; height: 25px;
	border-radius: 50%;
}
.post-info b {
	color: #999;
}
.post-info .right {
	float: left;
	width: 20%;
	font-size: 14px;
}
.post-info .right .article-comment {
	margin: 0 10px;
}
</style>
