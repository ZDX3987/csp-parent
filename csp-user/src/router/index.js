import Vue from 'vue'
import Router from 'vue-router'
import index from '@/components/index'
import UserInfo from '@/components/UserInfo'
import UserContent from '@/components/UserContent'
import Write from '@/components/Write'
import ArticleContent from '@/components/ArticleContent'
import collection from '@/components/collection'
import cateArticle from '@/components/cateArticle'
import modifyPass from '@/components/modifyPass'
import friends from '@/components/friends'

/* import Login from '@/components/Login'
import Regist from '@/components/Regist' */


Vue.use(Router)

export default new Router({
  routes: [
		{
		  path: '/',
		  name: 'index',
		  component: index
		},
		{
			path: '/cate/:id',
			name: 'cateArticle',
			component: cateArticle
		},
		{
			path: '/pass',
			name: 'modifyPass',
			component: modifyPass
		},
		{
			path: '/article/:id',
			name: 'ArticleContent',
			component: ArticleContent
		},
		{
			path: '/userInfo',
			name: 'UserInfo',
			component: UserInfo,
			children: [
				{
					path: '/userContent',
					name: 'UserContent',
					component: UserContent
				},
				{
					path: '/write',
					name: 'Write',
					component: Write
				},
				{
					path: '/collection',
					name: 'collection',
					component: collection
				},
				{
					path: '/friends',
					name: 'friends',
					component: friends
				}
			]
		}
		/* ,
	{
	  path: '/login',
	  name: 'Login',
	  component: Login
	},
	{
	  path: '/regist',
	  name: 'Regist',
	  component: Regist
	} */
  ]
})
