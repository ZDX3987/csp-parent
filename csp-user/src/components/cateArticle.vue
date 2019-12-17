<template>
<div class="container-fluid">
	<div class="cate-content">
		<ul>
			<li v-for="item in info">
				<article class="article-item">
					<router-link :to="{path:'/article/'+item.article.id+'.html'}" class="article-img">
						<img :src="item.article.imgUrl" :title="item.article.title" />
						<span>{{item.category.alias}}</span>
					</router-link>
					<div class="artile-info">
						<h2><router-link :to="{path:'/article/'+item.article.id+'.html'}" :title="item.article.title">{{item.article.title}}</router-link></h2>
						<div class="excerpt">{{item.article.digest}}</div>
						<div class="post-info">
							<div class="left">
								<span class="article-author">
									<img :src="item.user.imgUrl" />
									{{item.user.username}}
								</span>
								<span class="article-date">
									<b class="fa fa-calendar-o"></b>
									{{item.article.createTime | dateFormat('yyyy-MM-dd')}}
								</span>
							</div>
							<div class="right">
								<span class="article-comment">
									<b class="fa fa-comments-o"></b>
									20
								</span>
								<span class="article-like">
									<b class="fa fa-heart-o"></b>
									{{item.article.parise}}
								</span>
							</div>
						</div>
					</div>
				</article>
			</li>
		</ul>
		<div @click="loadMore" class="load_more" id="load_more">
			加载更多
		</div>
	</div>
</div>
</template>

<script>
export default {
	name: 'cateArticle',
	data() {
		return {
			id: null,
			info: null,
			pageNum: 1,
			isLast: false
		}
	},
	created() {
		this.id = this.$route.path.substring(this.$route.path.lastIndexOf('/')+1, 
		         this.$route.path.indexOf('.html'))
		this.$axios.post('/api/article/getByCate', {
			cateId: this.id,
			pageNum: this.pageNum,
			pageCount: 10
		}).then(res=>{
			if (res.data.status == 200) {
				this.info = res.data.data
			}
		})
		
	},
	methods: {
		loadMore: function() {
			if (!this.isLast) {
				this.$axios.post('/api/article/getByCate', {
					cateId: this.id,
					pageNum: ++this.pageNum,
					pageCount: 10
				}).then(res=>{
					if (res.data.status == 200) {
						if (res.data.data.length == 0) {
							this.isLast = true;
							$("#load_more").text("已经到底了。。。")
						}
						for(var k in res.data.data){
							this.info.push(res.data.data[k])
						}
					}
				});
			}
		}
	}
}
</script>

<style>
.container-fluid {
	padding: 0 70px;
}
.cate-content {
	background: #FFF;
	margin-top: 20px;
	padding-top: 20px;
	width: 68%;
}

.cate-content li {
	height: 180px;
	list-style: none;
	border-bottom: 1px solid #efefef;
}
.article-item {
	height: 160px; margin: 20px auto;
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
	height: 20px; padding: 0 5px;
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
.load_more {
	height: 80px; line-height: 70px;
	text-align: center; transition: all 0.5s;
	font-size: 16px; cursor: pointer;
}
.load_more:hover {
	color: #999;
}
</style>
