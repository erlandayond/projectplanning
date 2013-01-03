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
        

        var quarter=4;

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

      });

});