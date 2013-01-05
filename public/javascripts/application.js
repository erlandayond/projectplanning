$(document).ready(function () {
	//JQUERY STUFF FOR EVENT HANDLERS ETC...
	$(".employee-info .project .project-name").attr('contentEditable', 'true');
	$(".employee-info .project .week").attr('contentEditable', 'true');
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

    //nWeekNumber is a hidden field
      var tempWeekNumber=$('#nWeekNumber').val();
     

      $('.table-row.employee').each(function(index){

            var employee=$(this);
            var employeeInfo=$(this).next();

            for (var i = tempWeekNumber; i <52; i++) {
              
                var selector=".column.second-column #"+i;

                var nWeekTotal=0;

                $(employeeInfo).find(selector).each(function(){
                    nWeekTotal+=parseInt($(this).text());
                });

                $(employee).find(selector).text(nWeekTotal);
               
            };
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
        displayMonthWeekNumbers();
     }


});

// return no of "mondays" in a year, month
function getNumOfWeeks(year,month){
    
    var firstDayOfMonth= new Date(year,month-1,1);
    var lastDayOfMonth=new Date(year, month,0);

    var day=firstDayOfMonth.getDay();
    var date=lastDayOfMonth.getDate();

    var firstMondayDate=1;

    if(day!=1){
        var firstMondayDate=1+(6-day)+2;
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
            var endWeek=(nQuarter-1)*13+13;


    for (var month = startMonth; month <=endMonth; month++) {
            
            var monthName=getMonthName(month);
            var numberOfWeeks=getNumOfWeeks(year,month);
            var nWeekCounter=numberOfWeeks;
           
            if(parseInt(month)%3==1){

                var monthParent= $('.column.second-column.month');
                var monthChild='<span class="title">'+monthName+'</span>';

                  for (var week = startWeek;nWeekCounter>0; week++, nWeekCounter--) {
                      monthChild+='<span class="week">v'+week+'</span>';
                };

                $(monthParent).html(monthChild);
                startWeek=week;

            }

            if(parseInt(month)%3==2){
                var monthParent='.column.third-column.month';
                var monthChild='<span class="title">'+monthName+'</span>';

                 for (var week = startWeek; nWeekCounter>0; week++, nWeekCounter--) {
                      monthChild+='<span class="week">v'+week+'</span>';
                };

                $(monthParent).html(monthChild);
                startWeek=week;
            }

            if(parseInt(month)%3==0){
                 var monthParent='.column.fourth-column.month';
                var monthChild='<span class="title">'+monthName+'</span>';

                for (var week = startWeek; nWeekCounter>0; week++, nWeekCounter--) {
                      monthChild+='<span class="week">v'+week+'</span>';
                };

                $(monthParent).html(monthChild);
                
            }
        };
}