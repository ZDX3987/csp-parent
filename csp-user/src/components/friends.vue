<template>
<div>
	<ul id="myTab" class="nav nav-tabs">
		<li class="active">
			<a href="#attention" data-toggle="tab">
				 关注
			</a>
		</li>
		<li><a href="#fans" data-toggle="tab">粉丝</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active user-info" id="attention">
			<h2 v-if="attentionNull">您还没有关注作者</h2>
			<ul v-if="!attentionNull">
				<li v-for="(item, i) in attentions">
					<div class="user-item" :title="item.username">
						<img :src="item.imgUrl"/>
						<h4>{{item.username}}</h4>
						<p>{{item.introduce}}</p>
					</div>
				</li>
			</ul>
		</div>
		<div class="tab-pane fade user-info" id="fans">
			<h2 v-if="fansNull">您还没有任何粉丝</h2>
			<ul v-if="!fansNull">
				<li v-for="(item, i) in fans">
					<div class="user-item" :title="item.username">
						<img :src="item.imgUrl"/>
						<h4>{{item.username}}</h4>
						<p>{{item.introduce}}</p>
					</div>
				</li>
			</ul>
		</div>
	</div>
</div>
</template>

<script>
export default {
	name: 'friends',
	data() {
		return {
			userData: JSON.parse(localStorage.getItem("userData")),
			attentions: null,
			fans: null,
			attentionNull: false,
			fansNull: false
		}
	},
	created() {
		this.$axios.post('api/user/friends', {
			flag: 0,
			userId: this.userData.id
		}).then(res=>{
			if (res.data.status == 200) {
				if (res.data.data.length == 0) {
					this.attentionNull = true;
					return;
				}
				this.attentions = res.data.data
			}
		})
		
		this.$axios.post('api/user/friends', {
			flag: 1,
			userId: this.userData.id
		}).then(res=>{
			if (res.data.status == 200) {
				if (res.data.data.length == 0) {
					this.fansNull = true;
					return;
				}
				this.fans = res.data.data
			}
		})
	}
}
</script>

<style scoped="scoped">
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
.user-info {
	height: 300px;
}
.user-info ul{
	list-style: none;
	margin: 0 20px;
	margin-top: 20px;
}
.user-info ul li {
	width: 47%;
	margin: 0 20px 20px 0;
	float: left;
	height: 120px;
}
.user-item {
	width: 100%;
	height: 120px;
	padding: 10px;
	border: 1px solid #eee;
	transition: all 0.5s;
	transform: translate(0,0);
}
.user-info ul li:hover .user-item {
	border: 1px solid #55bd66;
	box-shadow: 2px 2px 12px rgba(0,0,0,0.3);
	transform: translate(0,-10px);
}
.user-info h2 {
	margin: 0 auto;
	color: #999;
	width: 30%;
	margin-top: 20px;
}
.user-item img {
	width: 100px; height: 100px;
	float: left;
	margin-right: 20px;
}
.user-item p {
	margin-top: 20px;
	color: #999;
}
</style>
