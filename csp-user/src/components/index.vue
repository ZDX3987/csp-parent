<template>
<div class="container-fluid">
	<div class="recommended">
		<div class="recommend-title">
			<p>站内推荐</p>
		</div>
		<div class="recommended-list">
			<ul>
				<li v-for="item in recommended">
					<router-link :to="{path:'/article/'+item.article.id+'.html'}">
						<img :src="item.article.imgUrl" :title="item.article.title" />
						<span>{{item.category.alias}}</span>
						<div>
							<h2>{{item.article.title}}</h2>
						</div>
					</router-link>
				</li>
			</ul>
		</div>
	</div> <!--站内推荐结束-->
	<div class="article">
		<div class="article-list">
			<div class="category-list">
				<ul class="">
					<li @click="choseCate('createTime')" class="active"><a href="#new-article" data-toggle="tab">最新文章</a></li>
					<li @click="choseCate('parise')"><a href="#parise-article" data-toggle="tab">最受欢迎</a></li>
				</ul>
			</div>
			<div class="tab-content">
				<div id="new-article" class="tab-pane fade in active article-content">
					<ul>
						<li v-for="item in newArticleList">
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
					<div @click="loadMore('createTime')" class="load_more" id="load_time">
						加载更多
					</div>
				</div>
				<div id="parise-article" class="tab-pane fade article-content">
					<ul>
						<li v-for="item in newArticleList">
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
					<div @click="loadMore('parise')" class="load_more" id="load_parise">
						加载更多
					</div>
				</div>
			</div>
		</div>
		<aside class="hot-article">
			<div class="hot-list">
				<div class="hot-title">
					热门文章
				</div>
				<div class="hot-content">
					<ul>
						<li v-for="item in recommended">
							<div class="hot-item">
								<div class="left">
									<img :src="item.article.imgUrl" :title="item.article.title" />
								</div>
								<div class="right">
									<h3><router-link :to="{path:'/article/'+item.article.id+'.html'}" :title="item.article.title">{{item.article.title}}</router-link></h3>
									<div class="hot-info">
										<span class="hot-category">{{item.category.alias}}</span>
										<span class="hot-date">
											<b class="fa fa-calendar-o"></b>
											{{item.article.createTime | dateFormat('yyyy-MM-dd')}}
										</span>
										<span class="hot-like">
											<b class="fa fa-heart-o"></b>
											{{item.article.parise}}
										</span>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</aside>
		<aside class="article-lable">
			<div class="lable-list">
				<div class="lable-title">
					标签聚合
				</div>
				<div class="lable-content">
					<router-link :to="{path:'/cate/'+item.category.id+'.html'}" v-for="(item, i) in options" :key="i">
						<font>{{item.category.alias}}</font><span>({{item.count}})</span>
					</router-link>
				</div>
			</div>
		</aside>
	</div><!--文章列表结束-->	
	
</div>
</template>

<script>
import '../../static/js/index.js'
export default {
	name: 'index',
	data() {
		return {
			//轮播图图片路径
			mycarouselImg: ["../../static/images/temp/0ef9a24605117488.jpg","../../static/images/temp/0fd310371555d0ba.jpg",
				"../../static/images/temp/24ebd574e1d55037.jpg"],
			newArticleList: null,
			options: null,
			pageNumParise: 1,
			pageNumTime: 1,
			isLast: false,
			recommended: null
		}
	},
	created() {
		this.$axios.post('/api/article/articleList', {
			order: 'createTime',
			pageNum: this.pageNumTime,
			pageCount: 10
		}).then(res=>{
			if (res.data.status == 200) {
				this.newArticleList = res.data.data;
			}
		});
		
		//推荐文章
		this.$axios.post('/api/article/articleList', {
			order: 'parise',
			pageNum: 1,
			pageCount: 4
		}).then(res=>{
			if (res.data.status == 200) {
				this.recommended = res.data.data;
			}
		});
		
		this.$axios.post('/api/article/category/listWithCount').then(res=>{
			if (res.data.status == 200) {
				this.options = res.data.data
			}
		})
	},
	methods: {
		loadMore: function(cate) {
			if (!this.isLast) {
				if (cate == 'parise') {
					this.$axios.post('/api/article/articleList', {
						order: cate,
						pageNum: ++this.pageNumParise,
						pageCount: 10
					}).then(res=>{
						if (res.data.status == 200) {
							if (res.data.data.length == 0) {
								this.isLast = true;
								$("#load_parise").text("已经到底了。。。")
							}
							for(var k in res.data.data){
								this.newArticleList.push(res.data.data[k])
							}
						}
					});
				}
				else {
					this.$axios.post('/api/article/articleList', {
						order: cate,
						pageNum: ++this.pageNumTime,
						pageCount: 10
					}).then(res=>{
						if (res.data.status == 200) {
							if (res.data.data.length == 0) {
								this.isLast = true;
								$("#load_time").text("已经到底了。。。")
							}
							for(var k in res.data.data){
								this.newArticleList.push(res.data.data[k])
							}
						}
					});
				}
			}
		},
		choseCate: function(cate) {
			this.isLast = false;
			$(".load_more").text("加载更多")
			if (cate == 'parise') {
				this.pageNumParise = 1
				this.$axios.post('/api/article/articleList', {
					order: cate,
					pageNum: this.pageNumParise,
					pageCount: 10
				}).then(res=>{
					if (res.data.status == 200) {
						this.newArticleList = res.data.data;
					}
				});
			}
			else {
				this.pageNumTime = 1
				this.$axios.post('/api/article/articleList', {
					order: cate,
					pageNum: this.pageNumTime,
					pageCount: 10
				}).then(res=>{
					if (res.data.status == 200) {
						this.newArticleList = res.data.data;
					}
				});
			}
		}
	}
}


</script>

<style>
	@import url("../../static/css/index.css");
</style>
