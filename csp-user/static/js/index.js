/**JQ代码部分**/

//最后一张图片的下标，三张图片，下标2
var currentNum = 2;
var interval;

$(function() {
	var length = $(".item").length-1;
	autoPlay(length);
	initEvent(length);
	
})

/**轮播图**/
function initEvent(length) {
	$(".mycarousel").mouseenter(function () {
        stopPlay();
    });
 
    $(".mycarousel").mouseleave(function () {
        autoPlay();
    });
	
	 $(".carousel-dot li").click(function () {
        goImg(length-$(".carousel-dot li").index(this), length);
    });
	
	$(".left").click(function () {
        let transord =currentNum+1;
        transord = transord>length?0:transord;
        goImg(transord, length);
    });
 
    $(".right").click(function () {
        let transord =currentNum-1;
        transord = transord<0?length:transord;
        goImg(transord, length);
    });	
}

function autoPlay(length) {
	interval = setInterval(function() {
		let num = currentNum;
		currentNum--;
		if (currentNum < 0) {
			currentNum = length;
		}
		$(".item").eq(num).fadeOut('slow');
		$(".item").eq(currentNum).fadeIn('slow');
		$(".carousel-dot li").eq(length-num).removeClass('active');
		$(".carousel-dot li").eq(length-currentNum).addClass('active');
	}, 3000)
}

function stopPlay() {
	window.clearInterval(interval);
}

function goImg(transNum, length) {
	let num = currentNum;
	currentNum = transNum;
	if (currentNum < 0) {
		currentNum = length;
	}
	$(".item").eq(num).fadeOut('slow');
	$(".item").eq(currentNum).fadeIn('slow');
	$(".carousel-dot li").eq(length-num).removeClass('active');
	$(".carousel-dot li").eq(length-currentNum).addClass('active');
}