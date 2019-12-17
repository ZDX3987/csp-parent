<template>
<div v-if="showBg">
	<div @click="hideBg" class="msg-bg"></div>
	<div class="warn-msg">
		<div class="alert alert-danger">{{msg}}</div>
	</div>
</div>
</template>

<script>
export default {
	name: 'warnMsg',
	data() {
		return {
			msg: null,
			showBg:false,
		}
	},
	methods: {
		hideBg: function() {
			this.showBg = false
		}
	}
}
</script>

<style>
.warn-msg {
	width: 20%; height: 100px;
	position: fixed;
	left: 50%; top: 50%; 
	transform: translate(-50%, -50%);
	z-index: 101;
}
.msg-bg {
	width: 100%; height: 800px;
	background: #000;
	position: fixed;
	left: 0; top: 0;
	opacity: 0.5;
	z-index: 100;
}
</style>
