<template>
<div class="container-fluid">
	<div class="articleInfo">
		<div class="articleTitle">
			<h1>{{info.article.title}}</h1>
			<div class="postInfo">
				<div class="left">
					<ul>
						<li>
							<img :src="info.user.imgUrl" />
							{{info.user.username}}
						</li>
						<li>
							<span class="fa fa-bookmark-o"></span>
							{{info.category.alias}}
						</li>
						<li>
							<span class="fa fa-calendar-o"></span>
							{{info.article.createTime | dateFormat('yyyy-MM-dd')}}
						</li>
					</ul>
				</div>
				<div class="right">
					<ul>
						<li>
							<span class="fa fa-comment-o"></span>
							{{commentsNUll?0:comments.length}}
						</li>
						<li>
							<span class="fa fa-heart-o"></span>
							{{info.article.parise}}
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="articleContent" v-html="info.article.content"></div>
		<div class="parise">
			<div class="parise_btn" @click="pariseToggle">
				<span class="fa fa-heart-o">&nbsp;赞&nbsp;&nbsp;|&nbsp;&nbsp;{{info.article.parise}}</span>
			</div>
		</div>
		<div class="article-nav">
			<a href="#" class="prev">上一篇：Java 性能优化：教你提高代码运行的效率</a>
			<a href="#" class="next">下一篇：负载均衡基础知识</a>
		</div>
	</div>
	
	
	<div class="authorShow">
		<div class="authorInfo">
			<img :src="info.user.imgUrl" />
			<h3>{{info.user.username}}</h3>
			<p>{{info.user.introduce}}</p>
		</div>
		<div class="author_btn">
			<button class="collect" @click="collectArticle">
				<font v-if="!hasCollected">收藏</font>
				<font v-if="hasCollected">已收藏</font>
			</button>
			<button @click="attentionUser">
				<font v-if="!hasAttentioned">关注</font>
				<font v-if="hasAttentioned">已关注</font>
			</button>
		</div>
	</div>
	<div class="comments">
		<div class="write-comments">
			<input type="text" v-model="comment_text" class="input" id="saytext" name="saytext" placeholder="留下你的评论..."/>
			<span class="emotion fa fa-smile-o" title="插入表情"></span>
			<input type="submit" @click="commit" class="sub_btn" value="发布评论" />
			<div id="show"></div>
		</div>
		<div class="list-comments">
			<h4>全部评论（{{commentsNUll?0:comments.length}}）</h4>
			<div class="content-com">
				<h2 class="commentsNUll" v-if="commentsNUll">当前没有任何评论...</h2>
				<ul v-if="!commentsNUll">
					<li v-for="(item, i) in comments">
						<div class="item-com">
							<div class="item-com-1">
								<img :src="item.user.imgUrl" />
								<h5>{{item.user.username}}</h5>
								<font>{{item.comment.createTime | dateFormat('yyyy-MM-dd')}}</font>
							</div>
							<div class="item-com-2">
								<p>{{item.comment.text}}</p>
							</div>
							<div class="item-com-3">
								<span v-if="pariseIndex[i]==1" @click="cPariseToggle(i)" class="parised fa fa-thumbs-up">&nbsp;{{item.comment.parise}}</span>
								<span v-else @click="cPariseToggle(i)" class="fa fa-thumbs-up">&nbsp;{{item.comment.parise}}</span>
								
								<span @click="replyMethod(0, item.comment)" class="reply fa fa-reply" data-toggle="modal" data-target="#replyModal">&nbsp;回复</span>
								
							</div>
							<ul>
								<li v-if="i<replyLength" v-for="(e, i) in item.replys"  class="reply-item">
									<div class="item-com">
										<div class="item-com-1">
											<img :src="e.from.imgUrl" />
											<h5>{{e.from.username}} <span>回复</span> {{e.to.username}}</h5>
											<font>{{e.reply.createTime | dateFormat('yyyy-MM-dd')}}</font>
										</div>
										<div class="item-com-2">
											<p>{{e.reply.text}}</p>
										</div>
										<div class="item-com-3">
											<span  class="fa fa-thumbs-up">&nbsp;{{e.reply.parise}}</span>
											<span @click="replyMethod(1, e.reply)" class="fa fa-reply" data-toggle="modal" data-target="#replyModal">&nbsp;回复</span>
											
										</div>
									</div>
								</li>
								<li v-if="item.replys.length>2" class="reply-toggle">
									<h4 @click="replyToggle(item.replys.length)">展开剩余{{item.replys.length-replyLength}}条回复</h4>
								</li>	
							</ul>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div id="replyModal" class="reply-modal modal fade">
		<div class="reply-win">
			<div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">回复</h4>
            </div>
            <div class="modal-body write-reply">
				<input type="text" v-model="replyText" class="input" id="saytext" name="saytext" placeholder="回复..."/>
			</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button @click="replyCommit" type="button" class="btn btn-success">回复</button>
            </div>
        </div><!-- /.modal-content -->
		</div>
	</div>
	<warnMsg ref="warnMsg"/>
</div>
</template>

<script>
import Editor from 'wangeditor'
import '../../release/wangEditor.min.css'
import '../../static/js/jquery.qqFace.js'
import '../../static/js/jquery-browser.js'
import warnMsg from "@/components/warnMsg"

export default {
	name: 'articleContent',
	data() {
		return {
			id:null,
			info: null,
			isParised: false,
			hasCollected:false,
			hasAttentioned: false,
			userData: JSON.parse(localStorage.getItem("userData")),
			editor: null,
			isInput: true,
			//一级回复
			toReply: -1,
			replyText: null,
			replyLength: 2,
			//二级回复
			sToReply: -1,
			sReplyText: null,
			//评论数据
			comment_text: null,
			comments: null,
			commentsNUll: false,
			cParised: false,
			pariseIndex: [],
			reIndex:[],
			
			comment:null, 
			reply:null, 
			to: null,
			replyType:null
	
		}
		
	},
	components: {
		warnMsg: warnMsg
	},
	mounted() {
		
	},
	created(){
		this.id = this.$route.path.substring(this.$route.path.lastIndexOf('/')+1, 
		         this.$route.path.indexOf('.html'))
		this.$axios.post('/api/article/'+this.id).then(res=>{
			if (res.data.status == 200) {
				this.info = res.data.data
				if (this.userData != null) {
					//监测是否关注
					this.$axios.post('api/user/attentioned', {
						to: this.info.user.id,
						from: this.userData.id
					}).then(res=>{
						if (res.data.status == 200) {
							if (res.data.data == 1) {
								this.hasAttentioned = true
							}
							else {
								this.hasAttentioned = false
							}
						}
					})
				}
				//获取评论数据
				this.$axios.post('api/article/comment/show', {
					articleId: parseInt(this.id)
				}).then(cData=>{
					if (cData.data.status == 200) {
						if (cData.data.data) {
							this.comments = cData.data.data
						}
						else {
							this.commentsNUll = true
						}
					}
				})
			}
		});
		
		if (this.userData != null) {
			//检查是否为收藏文章
			this.$axios.post('api/article/collected', {
				articleId: parseInt(this.id),
				userId: this.userData.id
			}).then(res=>{
				if (res.data.status == 200) {
					if (res.data.data == 1) {
						this.hasCollected = true
					}
					else {
						this.hasCollected = false
					}
				}
			})
		}

	},
	methods: {
		pariseToggle: function() {
			if (!this.isParised) {
				this.$axios.post('/api/article/parise', {
					id: this.info.article.id,
					isParised: this.isParised
				}).then(res=>{
					if (res.data.status == 200) {
						$(".parise_btn").css({
							background: '#55bd66',
							color: '#FFF'
						})
						this.info.article.parise += 1
						this.isParised = !this.isParised;
					}
				})
			}
			else {
				this.$axios.post('/api/article/parise', {
					id: this.info.article.id,
					isParised: this.isParised
				}).then(res=>{
					if (res.data.status == 200) {
						$(".parise_btn").css({
							background: '#FFF',
							color: '#55bd66'
						})
						this.info.article.parise -= 1
						this.isParised = !this.isParised;
					}
				})				
			}
		},
		collectArticle: function() {
			if (this.userData == null) {
				this.$refs.warnMsg.showBg = true
				this.$refs.warnMsg.msg = '您还没有登录，不能操作'
				return;
			}
			
			if (!this.hasCollected) {
				this.$axios.post('/api/article/collect', {
					aId: this.info.article.id,
					uId: this.userData.id,
					hasCollected: this.hasCollected
				}).then(res=>{
					if (res.data.status == 200) {
						this.hasCollected = !this.hasCollected
					}
				})
			}
			else {
				this.$axios.post('/api/article/collect', {
					aId: this.info.article.id,
					uId: this.userData.id,
					hasCollected: this.hasCollected
				}).then(res=>{
					if (res.data.status == 200) {
						this.hasCollected = !this.hasCollected
					}
				})
			}
		},
		cPariseToggle: function(i) {
			let key = i.toString()+"a";
			if(this.reIndex[key]){
				this.reIndex[key].zj = !this.reIndex[key].zj
			}else{
					this.reIndex[key] = {
					zj:true
				}
			}
			this.cParised = this.reIndex[key].zj
			if (this.cParised) {
				this.$axios.post('/api/article/comment/parise', {
					id: this.comments[i].comment.id,
					isParised: this.cParised
				}).then(res=>{
					if (res.data.status == 200) {
						this.comments[i].comment.parise += 1
						this.pariseIndex[i] = 1
					}
				})
			}
			else {
				this.$axios.post('/api/article/comment/parise', {
					id: this.comments[i].comment.id,
					isParised: this.cParised
				}).then(res=>{
					if (res.data.status == 200) {
						this.comments[i].comment.parise -= 1
							this.pariseIndex[i] = 0
					}
				})				
			}
		},
		rPariseToggle: function() {
			
		},
		attentionUser: function() {
			if (this.userData == null) {
				this.$refs.warnMsg.showBg = true
				this.$refs.warnMsg.msg = '您还没有登录，不能操作'
				return;
			}
			
			if (!this.hasCollected) {
				this.$axios.post('/api/user/attention', {
					to: this.info.user.id,
					from: this.userData.id,
					hasAttention: this.hasAttentioned
				}).then(res=>{
					if (res.data.status == 200) {
						this.hasAttentioned = !this.hasAttentioned
					}
				})
			}
			else {
				this.$axios.post('/api/user/attention', {
					to: this.info.user.id,
					from: this.userData.id,
					hasAttention: this.hasAttentioned
				}).then(res=>{
					if (res.data.status == 200) {
						this.hasAttentioned = !this.hasAttentioned
					}
				})
			}
		},
		replyToggle: function(length) {
			this.replyLength = length
		},
		commit: function() {
			if (this.userData == null) {
				this.$refs.warnMsg.showBg = true
				this.$refs.warnMsg.msg = '您还没有登录，不能评论'
				return;
			}
			if (this.comment_text == null) {
				this.$refs.warnMsg.showBg = true
				this.$refs.warnMsg.msg = '请输入内容！'
				return;
			}
			this.$axios.post('api/article/comment/add', {
				text: this.comment_text,
				articleId: this.info.article.id,
				userId: this.userData.id
			}).then(res=>{
				if (res.data.status == 200) {
					this.$refs.warnMsg.showBg = true
					this.$refs.warnMsg.msg = '评论成功！'
					//获取评论数据
					this.$axios.post('api/article/comment/show', {
						articleId: parseInt(this.id)
					}).then(cData=>{
						if (cData.data.status == 200) {
							if (cData.data.data) {
								this.commentsNUll = false
								this.comments = cData.data.data
							}
							else {
								this.commentsNUll = true
							}
						}
					})
				}
			})
		},
		replyCommit: function(replyType, e) {
			if (this.userData == null) {
				this.$refs.warnMsg.showBg = true
				this.$refs.warnMsg.msg = '您还没有登录，不能回复'
				return;
			}
			if (this.replyText == null) {
				this.$refs.warnMsg.showBg = true
				this.$refs.warnMsg.msg = '请输入内容！'
				return;
			}

			this.$axios.post('api/article/reply/add', {
				replyType: this.replyType,
				text: this.replyText,
				comment: this.comment,
				reply: this.reply,
				to: this.to,
				from: this.userData.id
			}).then(res=>{
				if (res.data.status==200) {
					this.$refs.warnMsg.showBg = true
					this.$refs.warnMsg.msg = '回复成功！'
					this.toReply = -1
					//获取评论数据
					this.$axios.post('api/article/comment/show', {
						articleId: parseInt(this.id)
					}).then(cData=>{
						if (cData.data.status == 200) {
							if (cData.data.data) {
								this.commentsNUll = false
								this.comments = cData.data.data
							}
							else {
								this.commentsNUll = true
							}
						}
					})
					return;
				}
			})
		},
		replyMethod: function(replyType, e) {
			this.replyType = replyType
			if (replyType == 0) {
				this.comment = e.id;
				this.reply = this.comment;
				this.to = e.user;
			}
			else if (replyType == 1) {
				this.comment = e.comment;
				this.reply = e.id;
				this.to = e.from;
			}
		},
		editorConfig: function() {
			this.editor = new Editor("#commentsEditor")
			this.editor.customConfig.menus = ['emoticon']
			this.editor.create()
		}
	}
}

$(function(){

// 	$('.emotion').qqFace({
// 		id : 'facebox', 
// 		assign:'saytext', 
// 		path:'../../static/images/arclist/'	//表情存放的路径
// 	});
// 
// 	$(".sub_btn").click(function(){
// 		var str = $("#saytext").val();
// 		$("#show").html(replace_em(str));
// 	});
	

});

//查看结果

function replace_em(str){
	str = str.replace(/\</g,'&lt;');
	str = str.replace(/\>/g,'&gt;');
	str = str.replace(/\n/g,'<br/>');
	str = str.replace(/\[em_([0-9]*)\]/g,'<img src="../../static/images/arclist/$1.gif" border="0" />');
	return str;

}
</script>

<style>

.container-fluid {
	padding: 0 70px;
}
.articleInfo {
	background: #FFF;
	width: 68%;
	margin-top: 20px;
	padding: 40px;
	float: left;
}
.articleTitle {
	height: 150px;
	margin-bottom: 40px;
}
.articleTitle h1 {
	margin: 0;
}
.postInfo {
	margin-top: 40px;
	height: 50px;
	border-bottom: 1px solid #eee;
}
.postInfo ul {
	list-style: none;
	height: 40px;
	color: #999;
}
.postInfo .left {
	float: left;
	width: 80%;
}
.postInfo .left li {
	float: left;
	height: 40px;
	margin-right: 20px;
	line-height: 40px;
}
.postInfo .left li img {
	width: 25px; height: 25px;
	border-radius: 50%;
}
.postInfo .right {
	float: left;
	width: 20%;
}
.postInfo .right ul {
	float: right;
}
.postInfo .right li {
	float: left;
	height: 40px;
	margin-right: 20px;
	line-height: 40px;
}
.articleContent {
	width: 100%;
}
.parise {
	height: 80px;
	border-bottom: 1px solid #eee;
}
.parise_btn {
	height: 50px; width: 150px;
	border: 1px solid #55bd66;
	margin: 0 auto; color: #55bd66;
	line-height: 50px; text-align: center;
	border-radius: 50px;
	transition: all 0.5s;
	cursor: pointer;
}
.parise_btn span {
	font-size: 16px;
}
.parise_btn:hover {
	background: #55bd66;
	color: #FFF;
}
.article-nav {
	margin-top: 10px;
	height: 50px; line-height: 50px;
}
.article-nav a{
	display: inline-block;
	width: 49%; color: #999;
	font-size: 14px;
	text-decoration: none;
	transition: all 0.5s;
}
.article-nav a:hover {
	color: #55bd66;
}
.article-nav .prev {
	text-align: left;
}
.article-nav .next {
	text-align: right;
}
.authorShow {
	width: 30%;
	background: #FFF;
	height: 300px;
	float: left;
	margin-top: 20px;
	margin-left: 20px;
}
.authorInfo {
	margin-top: 50px;
	text-align: center;
}
.authorInfo img {
	width: 70px; height: 70px;
	border-radius: 50%;
}
.authorInfo h3 {
	margin: 10px auto;
}
.author_btn {
	text-align: center;
	margin-top: 20px;
}
.author_btn button {
	width: 50px; height: 30px;
	margin: 0 5px;
	border: none;
	color: #FFF;
	background: #55bd66;
}
.author_btn button:nth-of-type(2) {
	background: #5890FF;
}

.comments {
	width: 68%;
	background: #FFF;
	float: left; margin-top: 20px;
}
.write-comments {
	padding: 30px 20px 0 20px;
}
.write-comments .input {
	width: 100%; height: 40px;
	font-size: 14px; padding-left: 5px;
}
.emotion {
	font-size: 30px;
	color: #999; 
	margin-left: 10px;
	margin-top: 10px;
}
.emotion:hover {
	color: #55bd66;
}
.write-comments .sub_btn {
	width: 80px; height: 40px;
	border: none; background: #55bd66;
	color: #FFF; float: right;
	margin-top: 10px;
}
.list-comments {
	padding: 30px 20px 0 20px;
}
.list-comments h4 {
	background: rgb(250,250,250);
	height: 30px; line-height: 30px;
	font-size: 14px; padding-left: 10px;
}
.content-com ul {
	list-style: none;
}
.content-com ul li{
	margin-bottom: 10px;
}
.commentsNUll {
	width: 35%;
	margin: 20px auto;
	color: #8590a6;
	font-weight: 300;
}
.item-com {
	padding:0 10px;
}
.item-com-1 {
	height: 30px;
}
.item-com-1 img {
	width: 24px; height: 24px;
	border-radius: 50%;
}
.item-com-1 h5 {
	margin-top: -18px;
	margin-left: 30px;
}
.item-com-1 font {
	float: right;
	margin-top: -28px;
	color: #8590a6;
	font-size: 14px;
}
.item-com-1 span {
	color: #8590a6;
}
.item-com-2 p {
	margin-left: 30px;
}
.item-com-3 {
	margin-left: 30px;
	padding-bottom: 10px;
	border-bottom: 1px solid #ebebeb;
}
.item-com-3 span {
	margin:0 5px;
	color: #8590a6;
	cursor: pointer;
}
.item-com-3 .parised {
	color: #55bd66;
}
.reply-item {
	margin-top: 10px;
	margin-left: 20px;
}
.reply-item .item-com{
	padding-right: 0;
}
.write-reply {
}
.write-reply .input {
	width: 85%; height: 30px;
	font-size: 14px; padding-left: 5px;
}
.write-reply .sub_btn {
	width: 60px; height: 30px;
	border: none; background: #55bd66;
	color: #FFF; float: right;
	margin-top: 8px;
}
.reply-toggle h4{
	margin-left: 50px;
	color: #8590a6;
	cursor: pointer;
	background: none;
	width: 120px;
}
.reply-win {
	width: 20%; height: 100px;
	position: fixed;
	left: 50%; top: 50%; 
	transform: translate(-50%, -50%);
	z-index: 101;
}
.reply-bg {
	width: 100%; height: 800px;
	background: #000;
	position: fixed;
	left: 0; top: 0;
	opacity: 0.5;
	z-index: 100;
}
</style>
