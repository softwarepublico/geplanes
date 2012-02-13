/**
*	akModal-  simplest alternative to thickbox
*	author: Amit Kumar Singh 
* 	project url : http://amiworks.co.in/talk/akmodal-simplest-alternative-to-thickbox/
 * 	inspired from early versions of thickbox
 *	
**/
/**
  * Version 2.0.0
  *  @param String  navurl             url to dispaly in the ifame
  *  @param String  title      title of the pop up box
  *  @param  Numeric  box_width	width of the box in pixels
  *  @param  Numeric  box_height	height of the box in pixels
  *   
 **/

jQuery.extend({
	
	showAkModal:function(navurl,title,box_width,box_height,scroll)
	{
	    var offset={};
	    var options ={
	    margin:1,
	    border:1,
	    padding:1,
	    scroll:0
	    };
		
		var scrollStr = "YES";
		if(!scroll){
			scrollStr = "NO";
		}
		
	    var win_width =$(window).width();
	    var scrollToLeft=$(window).scrollLeft();
	    var win_height =$(window).height();
	    var scrollToBottom=$(window).scrollTop();
	   

	    $('body').append("<div id='ak_modal_div' style='padding-top:0px;border:1px solid black;background-color:#FFF;position: absolute;z-index:1000;display:none;' ><iframe width='"+box_width+"' height='"+box_height+"'  frameborder=0 marginwidth='0' marginheight='0' scrolling='"+scrollStr+"'  name='frmTest' src='"+navurl+"'></iframe></div>");

	    $('#ak_modal_div').css({left:(((win_width/2-box_width/2))+scrollToLeft)+'px',top:(((win_height/2-box_height/2))+scrollToBottom)+'px'});
	  
	    $('#close').click( function() {
	       // 
	       $('#ak_modal_div').fadeOut(500);
	       $('#ak_modal_div').remove();
	        $.dimScreenStop();
	    });
	    $.dimScreen(500, 0.7, function() {
			$('#ak_modal_div').fadeIn(500);
	    });
	    
		var offset = {}
	    offset=$("#ak_modal_div").offset({ scroll: false })
	
  	    X_left=offset.left+box_width-16;
	    X_top=offset.top;

	    $('#close').css({left:X_left,top:X_top});

	},
	
	akModalRemove:function()
	{
	   $('#ak_modal_div').fadeOut(500);
	   $('#ak_modal_div').remove();
	   $.dimScreenStop();
	},
		
	akModalHideAndRedirect:function(redirect_url)
	{
	    $('#ak_modal_div').fadeOut(500);
	    $.dimScreenStop();
	    window.location=redirect_url;
	}
});	