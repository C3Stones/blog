// 是否为空
function isEmpty(n) {
	if (n == null || n == '' || typeof(n) == 'undefined') {
		return true;
	}
	return false;
}

//错误处理
function errorHandle(data) {
	if (data.status == 403) {
		layer.msg("权限不足", {icon: 2});
	} else if (data.status == 404) {
		layer.msg("请求资源不存在", {icon: 2});
	} else {
		layer.msg("服务端异常", {icon: 2});
	}
	return;
}

//提示消息
function msg(data) {
	if(!isEmpty(data.msg)) {
		if (data.data) {
			layer.msg(data.msg, {icon: 1});
		} else {
			layer.msg(data.msg, {icon: 2});
		}
	}
}

//刷新表格
function refresh() {
	$("button[data-type='reload']").click();
}