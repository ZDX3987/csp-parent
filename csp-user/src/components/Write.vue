<template>
<div class="pub-content">
	<ul id="myTab" class="nav nav-tabs">
		<li class="active">
			<a href="#article-list" data-toggle="tab">
				 文章列表
			</a>
		</li>
		<li><a href="#article-pub" data-toggle="tab">发布文章</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active article-panel" id="article-list">
			<h2 v-if="articleNull">您还没有发布任何动态或者文章</h2>
			<div class="collect-list" v-if="!articleNull">
				<ul>
					<li v-for="(item, i) in articleList">
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
							<div class="delete">
								<span class="fa fa-chevron-circle-left "></span>
								<div @click="deleteArticle(item.id, i)" class="fa fa-trash-o"></div>
							</div>
						</article>
					</li>
				</ul>
			</div>
		</div>
		<div class="tab-pane fade article-panel" id="article-pub">
			<div class="pub-info">
				<form role="form" onsubmit="return false;">
					<div class="form-group">
						<label for="" class="col-sm-3 control-label">文章标题&nbsp;<b>*</b></label>
						<div class="col-sm-9 form-item">
							<input type="text" class="form-control" v-model="title" placeholder="请输入标题">
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-3 control-label">文章分类&nbsp;<b>*</b></label>
						<div class="col-sm-9 form-item">
							<select class="form-control" v-model="category">
								<option v-for="item in options" :value="item.id">{{item.alias}}</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-3 control-label">缩略图&nbsp;<b>*</b></label>
						<div class="col-sm-9 form-item pub-thumbnail">
							<img src="../../static/images/add_header.jpg" />
							建议图片尺寸2:1
							<input id="pub-upload" class="pub-upload" type="file" accept="image/*" @change="uploadImg" />
						</div>
					</div>
					<div id="editor"></div>
					<div class="form-group">
						<div class="col-sm-9 form-item pub-btn">
							<input id="pub-reset" type="reset" value="重填" class="form-control" />
							<input id="pub-submit" type="submit" value="提交" class="form-control" @click="submit" />
						</div>
					</div>
					<p v-if="hasError" class="pub-wain col-sm-offset-1 alert alert-danger">{{msg}}</p>
				</form>
			</div>
		</div>
	</div>
	
	
</div>
</template>

<script>
import Editor from 'wangeditor'
import '../../release/wangEditor.min.css';


export default {
	name: 'Write',
	data() {
		return {
			articleList: null,
			articleNull: false,
			editor: null,
			userData: JSON.parse(localStorage.getItem("userData")),
			options: [{'id':1, 'alias':'项1'},{'id':2, 'alias':'项2'},{'id':3, 'alias':'项3'}],
			//校验数据
			hasError: false,
			msg: '错误信息',
			
			//表单数据
			title: '',
			category: null,
			thumbnail: null
		}
	},
	mounted() {
		this.editorConfig();
	},
	created() {
		this.$axios.post('api/article/ownArticle', {
			userId: this.userData.id
		}).then(res=>{
			if (res.data.status == 200) {
				if (res.data.data.length == 0) {
					this.articleNull = true
					return;
				}
				this.articleList = res.data.data
			}
		})
		
		this.$axios.post('/api/article/category/list').then(res=>{
			if (res.data.status == 200) {
				this.options = res.data.data
			}
		})
	},
	methods: {
		//上传头像预览
		uploadImg: function() {
			var reader = new FileReader();
			reader.readAsDataURL($("#pub-upload")[0].files[0]);
			var _this = this
			reader.onload = function (evt) {
		        $(".pub-thumbnail img")[0].src = evt.target.result;
				_this.file = $("#pub-upload")[0].files[0]
		    }
		},
		submit: function() {
			if (this.title == '') {
				this.hasError = true;
				this.msg = '请将信息填写完整后发布！'
				return;
			}
			if (this.category == null) {
				this.hasError = true;
				this.msg = '请将信息填写完整后发布！'
				return;
			}
			if (this.editor.txt.text() == '') {
				this.hasError = true;
				this.msg = '请将信息填写完整后发布！'
				return;
			}
			this.hasError = false;
			
			var config = {
				headers: {
					 'Content-Type': 'multipart/form-data'
				}
			}
			var form = new FormData();
			form.append('file', this.file);
			this.$axios.post('/api/article/thumbnail', form, config).then(res=> {
				if (res.data.status == 200) {
					this.$axios.post('/api/article/create', {
						article: {
							title: this.title,
							category: this.category,
							content: this.editor.txt.html(),
							imgUrl: res.data.data,
							digest: this.editor.txt.text().substr(0,100)
						},
						token: this.getCookie('userToken')
					}).then(data=>{
						window.location.href = '/'
					})
				}
			})
		},
		deleteArticle: function(id, i) {
			this.$axios.post('api/article/delete', {
				id: id
			}).then(res=>{
				if (res.data.status == 200) {
					this.articleList.splice(i, 1)
				}
			})
		},
		getCookie: function(cookieName) {
			var theCookie = "" + document.cookie;
			var ind = theCookie.indexOf(cookieName);
			if(ind==-1 || cookieName=="") return "";
			var ind1 = theCookie.indexOf(';',ind);
			if(ind1==-1) ind1 = theCookie.length;
			return theCookie.substring(ind+cookieName.length+1,ind1);
		},
		editorConfig: function() {
			this.editor = new Editor("#editor")
			//允许本地上传图片
			//this.editor.customConfig.uploadImgShowBase64 = true
			this.editor.customConfig.uploadImgServer = '/api/article/upload'
			this.editor.customConfig.uploadFileName = 'file'
			this.editor.customConfig.zIndex = 2
			//允许外部粘贴带格式文本
			this.editor.customConfig.pasteFilterStyle = false
			//允许粘贴外部图片
			this.editor.customConfig.pasteIgnoreImg = false
			//插入网络图片回调URL
			this.editor.customConfig.linkImgCallback = function (url) {
				console.log(url) // url 即插入图片的地址
			}
			this.editor.customConfig.fontNames = ['宋体', '微软雅黑', 'Arial', 'Tahoma', 'Verdana', '楷体', '黑体', '仿宋']
			this.editor.create()
		}
	}
}
</script>

<style>
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
.article-panel {
	padding: 0 20px;
	padding-top: 20px;
}
.article-panel h2 {
	margin: 0 auto;
	color: #999;
	width: 60%;
	margin-top: 20px;
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
	width: 100%;
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

.delete {
	float: left; transition: all 0.5s;
	width: 30px; height: 30px;
	transform: translateX(30px);
	
}
.delete span {
	display: block; position: absolute;
	float: left;
	width: 30px; height: 30px;
	font-size: 30px;
}
.delete div {
	width: 60px; height: 100px;
	position: absolute; line-height: 100px;
	color: #FFF; font-size: 40px;
	padding-left: 15px; float: left;
	background: rgb(241,64,60);
	transform: translateX(30px);
	visibility: hidden; opacity: 0;
	transition: all 0.5s;
}
.delete:hover{
	transform: translateX(-30px);
}
.delete:hover div {
	visibility: visible; opacity: 1;
}

.pub-info label{
	width: 150px;
}
.pub-thumbnail {
	width: 400px; height: 100px;
}
.pub-thumbnail img {
	width: 200px; height: 100px;
}
.pub-upload {
	width: 200px; height: 100px;
	margin-top: -100px; opacity: 0;
}
#editor {
	margin-bottom: 30px;
}
.pub-btn {
	float: right;
}
.pub-btn input {
	width: 100px;
	float: right;
	margin-left: 20px;
	color: #FFF;
	border: none;
}
#pub-submit {
	background: #55bd66;
}
#pub-reset {
	background: rgb(51,51,51);
}
.pub-wain {
	position: relative;
	width: 50%;
	top: -70px;
}
</style>
