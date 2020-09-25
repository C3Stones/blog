/**
 * 入口文件索引 使用说明：将此文件引入到页面中，可在script标签上定义一个data-main=""属性， 此属性指定页面入口文件。
 * 
 */
(function() {

	var entry, app = {
		home : '{/}layui/js/home'
	};

	(function() {

		var dataMain, scripts = document.getElementsByTagName('script'), eachScripts = function(
				el) {
			dataMain = el.getAttribute('data-main');
			if (dataMain) {
				entry = dataMain;
			}
		};

		[].slice.call(scripts).forEach(eachScripts);

	})();

	layui.config({
		base : 'layui/lay/modules/'
	}).extend(app).use(entry || 'home');

})();

if(window.parent != this.window){
	top.location.reload();
}