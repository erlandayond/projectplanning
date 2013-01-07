$(document).ready(function () {
	//JQUERY STUFF FOR EVENT HANDLERS ETC...
	
	var hiddenChangeStatus = $('#hidden-change-status').html();


	$('.employee-info').hide();
    
    $('.toggler').live('click',function(){

    	var thisRow=$(this).parent().parent();

    	if(thisRow.next().hasClass('employee-info')){

    		thisRow.next('.employee-info').toggle();
    		$(this).toggleClass('opened');

    		//project info
    		var thisEmployeeInfo=$(thisRow.next());
    		$(thisEmployeeInfo).find('.week').attr('contenteditable','true');
    }

    });

    $("#addEmployee").change(function(){
    	var empId=$("#addEmployee option:selected").val();
    	var empName=$('#addEmployee option:selected').text();
    	
    	if(empId==="new"){
    		var newEmpName=prompt("FirstName LastName");
    		var empUrl="http://localhost:9000/addEmployee";
    		if(newEmpName.length>0)
    		{
			 	$.post(empUrl, {ename:newEmpName, eType:"regular"},function(data){

			 	});
			}
    	}
    });

    $(".add-project").change(function(){
    	var projId=$(this).find('option:selected').val();
    	var projName=$(this).find('option:selected').text();
        var empId=$(this).parent().parent().parent().attr('employeeid');
        var newProjUrl="http://localhost:9000/addProject";
         
        alert("projId:"+projId+"empId:"+empId);
        $.post(newProjUrl,{projectId:projId, employeeId:empId},function(data){

        }) ;

    });
    $("#btnNewProject").click(function(){
    	var newProjectName=prompt("Project Name");
    	var newProjUrl="http://localhost:9000/addNewProject";
    	if(newProjectName.length>0){
    		$.post(newProjUrl, {projectName:newProjectName}, function(data){});
    	}
    });
    $('#btnNewEmployee').click(function(){

    	var newEmpName=prompt("Employee Name");
    	var newEmpUrl="http://localhost:9000/addNewEmployee";

    	if(newEmpName.length>0){
    		$.post(newEmpUrl, {employeeName:newEmpName}, function(data){

                window.location.reload(true);

    		});
    	}
    });
    $('#btnNewContractor').click(function(){

    	var newContractorName=prompt("Contractor Name");
    	var newContractUrl="http://localhost:9000/addNewContractor";

    	if(newContractorName.length>0){
    		$.post(newContractUrl, {contractorName:newContractorName}, function(data){})
    	}
    });
    $("#addContractor").change(function(){
    	var empId=$("#addContractor option:selected").val();
    	var empName=$('#addContractor option:selected').text();
    	
    	if(empId==="new"){
    		var newEmpName=prompt("FirstName LastName");
    		var empUrl="http://localhost:9000/addEmployee";
    		if(newEmpName.length>0)
    		{
			 	$.post(empUrl, {ename:newEmpName, eType:"contractor"},function(data){

			 	});
			}
    	}
    });
    $('.week').blur(function(){
        

        var quarter=$('#nQuarterNumber').val();

    	var weekNumber=this.id; // get week number
        
        var occupied=$(this).text(); // occupied percentage

    	var tempFind='.column.second-column #'+weekNumber;

    	var projectId=$(this).parent().parent().attr('projectid');
    	
        var employeeid=$(this).parent().parent().parent().attr('employeeid');

    	var total=0;
    	var empInfoRow=$(this).parent().parent().parent().find(tempFind).each(function(){
    		total+=parseInt($(this).text());
    	});
    	var prevRowWeek=$(this).parent().parent().parent().prev();
    	

        var prevRowWeekNum=$(this).parent().parent().parent().prev().find(tempFind);
        $(prevRowWeekNum).text(total);

        var updateProjUrl="http://localhost:9000/updateEmpProjOccupied";

        $.post(updateProjUrl, {sEmpId:employeeid, sProjId:projectId, sQuarter:quarter, sWeekNumber:weekNumber, sOccupied:occupied},function(data){

        });

    });

  
      
     $('li.month-button').click(function(){

           $('li.month-button').removeClass('active');
           $(this).addClass('active');

           // change the quarter hidden field value
           var quarterNum=this.id;
           $("#nQuarterNumber").val(quarterNum);

           displayMonthWeekNumbers(this); // display week numbers and months
      });

     var activeQuarter= $('li.month-button').hasClass('active');

     if(activeQuarter){
        var ele=$('li.month-button.active').get();

        var nQuarter=$('#nQuarterNumber').val();
        displayMonthWeekNumbers(ele);
     }

    $('.remove').click(function(){
        var empId=this.id;

        $.ajax({
            url:'/deleteEmployee',
            data:{nEmpId:empId},
            success:function(){
            window.location.reload(true); },
            error:function(){}

        });
    });
});

// return no of "mondays" in a year, month
function getNumOfWeeks(year,month){
    
    var firstDayOfMonth= new Date(year,month-1,1);
    var lastDayOfMonth=new Date(year, month,0);

    var day=firstDayOfMonth.getDay();
    var date=lastDayOfMonth.getDate();

    var firstMondayDate=1;
    
       if(day!=1){
        firstMondayDate=1+(6-day)+2;
    }
    
    var nMondayCount=0;

        for (var tempDate = firstMondayDate; tempDate <= lastDayOfMonth.getDate();  ) {
                nMondayCount+=1;
                tempDate+=7;
        };
    //alert("number of mondays:"+nMondayCount);

    return nMondayCount;

}

// return month name
function getMonthName(month){
    // can be any year
    var tempMonth=new Date(2013,month-1);

    var monthNames = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];
    var monthNum=tempMonth.getMonth();

   return monthNames[monthNum];
}

function displayMonthWeekNumbers(element){

            var year=2013;
            var startMonth=$(element).attr('startmon');
            var endMonth=parseInt(startMonth)+2;

            var nQuarter=$('#nQuarterNumber').val();
            var startWeek=(nQuarter-1)*13+1;
            //Remove hardcoded
            if(nQuarter==4){
                startWeek=startWeek+1;
            }
            var endWeek=(nQuarter-1)*13+13;

            var countWeekInQuarter=1;
            var tempStartWeek=startWeek; // just saving startweek before changing
       
            // Get first day of the year
            var firstDayOfYear=new Date(2013,0,1);
            
            var week=1;  //initialisation

            var monthChild="";

    for (var month = startMonth; month <=endMonth; month++) {
            
            var monthName=getMonthName(month);
            var numberOfWeeks=getNumOfWeeks(year,month);
            var nWeekCounter=numberOfWeeks;
            
            var secondcol='.table-header div.column.second-column';

            if(parseInt(month)%3==1){

                if(parseInt(firstDayOfYear)!=0 && parseInt(month)==1){
                    nWeekCounter=nWeekCounter+1;
                }
                
                monthChild+='<span class="title"></span>';

                  for (week = startWeek;nWeekCounter>0; week++, nWeekCounter--) {
                      monthChild+='<span id="'+week+'" class="week">v'+week+'</span>';
                      countWeekInQuarter=countWeekInQuarter+1;
                };

                
                startWeek=week;

            }

            if(parseInt(month)%3==2){
                
                monthChild+='<span class="title"></span>';

                 for (week = startWeek; nWeekCounter>0; week++, nWeekCounter--) {
                      monthChild+='<span id="'+week+'" class="week">v'+week+'</span>';
                      countWeekInQuarter=countWeekInQuarter+1;
                };

                
                startWeek=week;
            }

            if(parseInt(month)%3==0){

                //Remove hard-carded
                 if(parseInt(month)==9){
                    nWeekCounter=nWeekCounter+1;

                 }
                
                monthChild+='<span class="title"></span>';

                for (week = startWeek; nWeekCounter>0; week++, nWeekCounter--) {
                      monthChild+='<span id="'+week+'" class="week">v'+week+'</span>';
                      countWeekInQuarter=countWeekInQuarter+1;
                };

               
                
            }

        };

         $(secondcol).html(monthChild);

     $('.table-row.employee').each(function(index){

        var tempCounter1=tempStartWeek;
        var tempCounter2=tempStartWeek;

        var employee=$(this);
        var employeeInfo=$(this).next();

       $(employee).find('.column.second-column div.week').each(function(){
            
            this.id=tempCounter1;
            tempCounter1=tempCounter1+1;

        });
       
       $(employeeInfo).find('.column.second-column div.week').each(function(){

            this.id=tempCounter2;
            tempCounter2=tempCounter2+1;
       });

     });


     if(startMonth>3){
         var getEmployeesUrl="/getEmployees";
        $.ajax({
            url: getEmployeesUrl,
            data: { strStartWeek:tempStartWeek, strEndWeek:week-1},
            success:function(data){
                
            }
        });
     }


       //nWeekNumber is a hidden field
      var tempWeekNumber=1;
     

      $('.table-row.employee').each(function(index){

            var employee=$(this);
            var employeeInfo=$(this).next();

            for (var i = tempWeekNumber; i <52; i++) {
              
                var selector=".column.second-column #"+i;

                var nWeekTotal=0;

                $(employeeInfo).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekTotal+=parseInt($(this).text());
                    }
                    
                });

                $(employee).find(selector).text(nWeekTotal);
               
            };
      });
    
}