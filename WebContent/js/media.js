$(document).ready(function () {
	setTimeout("Refresh()", 10000);
	// This is the week grid
	$(".fg-button").hover(
			function(){
				$(this).addClass("ui-state-hover");
			},
			function(){
				$(this).removeClass("ui-state-hover");
			}
		)
		.mousedown(function(){
			$(this).parents('.fg-buttonset-single:first').find(".fg-button.ui-state-active").removeClass("ui-state-active");
			if( $(this).is('.ui-state-active.fg-button-toggleable, .fg-buttonset-multi .ui-state-active') ){ $(this).removeClass("ui-state-active"); }
			else { $(this).addClass("ui-state-active"); }
			})
			.mouseup(function(){
				if(! $(this).is('.fg-button-toggleable, .fg-buttonset-single .fg-button, .fg-buttonset-multi .fg-button') ){
					$(this).removeClass("ui-state-active");
				} });
	
	var currentdata;
    var lastsel;
    jQuery("#recordings").jqGrid({ 
    	url:'mediasense1.xml',
    	 datatype: "xml", 
    	 width: 770,
    	 height: 400,
    	 colNames:['ID','Date', 'Duration','From', 'To','State','Play'], 
    	 colModel:[ {name:'number',index:'number', width:35, align:"center"}, 
    	 			{name:'start',index:'start', width:120}, 
    	 			{name:'duration',index:'duration', width:50, align:"center"}, 
    	 			{name:'from',index:'from', width:70, align: "center"}, 
    	 			{name:'to',index:'to', width:60, align:"center"}, 
    	 			{name:'state',index:'state', width:60, align:"center"}, 
    	 			{name:'name',index:'name', width:60,align:"center"}
    	 			
    	 			], 
    	 rowNum:20, 
    	 autowidth: false, 
    	 rowList:[20,30], 
    	 pager: jQuery('#pager1'), 
    	 sortname: 'id',
    	 loadonce: true, 
    	 viewrecords: true, 
    	 sortorder: "desc", 
    	 caption:"Archived Recording" 
    	 }).navGrid('#pager1',{edit:false,add:false,del:false}); 
    
  	
		function reloadGrid() {
			jQuery("#recordings").trigger("reloadGrid"); 
		   };
		   
		function intervalTrigger() {
		      setInterval( reloadGrid, 3000 );
		   };

		  intervalTrigger(); 
			   


    function imageformat(cellvalue, options, rowObject) {
        return "<img class='aa'src='jtimepicker/play.png'/>";
    }
  
    $("#startDate").datepicker({ });
    $("#endDate").datepicker({  });


    //submits the gqgrid content
  
    $('#submit').click(function () {
        var data = $('#mediaTable').jqGrid('getRowData');

       for(var i=1;i <=data.length; i++) {
        if(data[i-1].access.indexOf("select") == 1) {
        	if( $("#"+i+"_access").val() == null ||  $("#"+i+"_access").val() == "") {
        		alert("please fill all fields");
        		return;
        	}
        	else {
        		data[i-1].access = $("#"+i+"_access").val();
        	}
        }
        
       }
        
        var json = JSON.stringify(data);
        //getting tod id
        var e = document.getElementById("combobox");
        var id = e.options[e.selectedIndex].value;
        var name = $("#combobox option:selected").text();
        var isAdmin =$('input:radio[name=isAdmin]:checked').val();
      
        var queryString = {
        		name: name,
        		id: id,
        		isAdmin:isAdmin,
        		json: json
        };
     
        var okCancel=confirm('Do you want to update ?');
        if(okCancel){
        	 $s.submitAdminData(queryString, {
     			success : function(data) {
     			alert(data);
     			
     			},
     			error : function(xhr, textStatus, errorThrown) {
     				alert("Error While adding Admin data");
     			}
     		});
        }
       
       
    });




  
});

function Refresh() {
	location.reload();
}
