$(document).ready(function () {
	//JQUERY STUFF FOR EVENT HANDLERS ETC...
	
	var hiddenChangeStatus = $('#hidden-change-status').html();

    $('#staffPopUp').hide();
    $('#projectPopUp').hide();
    $('.employee-info').hide();
    $('#txtProjectNameRequired').hide();
    $('#txtStaffNameRequired').hide();
    
    $(".ui-dialog").css('z-index', 99999);
    
     $('#loginButton').submit(function(){
        
        var password=$('#pass').val();
        var username=$('#user').val();
    
     });

	
    
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
    		var empUrl="/addEmployee";
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
        var newProjUrl="/addProject";
         
       
        $.post(newProjUrl,{projectId:projId, employeeId:empId},function(data){
           //alert('Project added ! Please Refresh');
           //$.cookie('projAddedToEmpId',empId);
           window.location.reload(true);

           //var empIdSelector1="div.employee-info[employeeid="+$.cookie('projAddedToEmpId')+"]";
           //var empIdSelector2=".table-row.employee#employee-"+$.cookie('projAddedToEmpId');
           //$(empIdSelector1).show();$(empIdSelector2).find('.toggler').each(function(){$(this).removeClass('toggler').addClass('toggler opened');});
        }) ;

    });
    $('.del-project').change(function(){
        var projId=$(this).find('option:selected').val();
        var projName=$(this).find('option:selected').text();

        if(parseInt(projId)>0){

         if(confirm("Are you sure you want to delete project: "+ projName+" ?")){
            $.ajax({
            url:'/deleteProject',
            data:{nProjId:projId},
            success:function(){
                window.location.reload(true);
             },
            error:function(){}

                });
             }
        }

    });
    // $("#btnNewProject").click(function(){
    // 	var newProjectName=prompt("Project Name");
    // 	var newProjUrl="/addNewProject";
    // 	if(newProjectName.length>0){
    // 		$.post(newProjUrl, {projectName:newProjectName}, function(data){
    //             window.location.reload(true);
    //         });
    // 	}
    // });

    $('#btnNewProject').click(function(){
        $('#projectPopUp').dialog({ resizable: false, width: 400, height: 210, title: 'Add project to Planning tool', position:{my: "center", at: "top center", of: window}  });
    });

     $('#btnNewStaff').click(function(){
         $('#staffPopUp').dialog({ resizable: false, width: 400, height:210, title: 'Add staff to Planning tool',position:{my: "center", at: "center", of: window} });
    });

    $('#btnAddProject').click(function(){

        var newProjectName=$('#txtProjectName').val();
        var newProjectType=$('select#ddProjectType').find('option:selected').val();
        var newProjUrl="/addNewProject";
       

        if(newProjectName.length>0 && newProjectType.length>0){

            $.post(newProjUrl, {projectName:newProjectName, projectType:newProjectType}, function(data){
                 window.location.reload(true);
             });  

        }else{
             $('#txtProjectNameRequired').show();
        }
    });


    // $('#btnNewEmployee').click(function(){

    // 	var newEmpName=prompt("Employee Name");
    // 	var newEmpUrl="/addNewEmployee";

    // 	if(newEmpName.length>0){
    // 		$.post(newEmpUrl, {employeeName:newEmpName}, function(data){

    //             window.location.reload(true);

    // 		});
    // 	}
    // });
    // $('#btnNewContractor').click(function(){

    // 	var newContractorName=prompt("Contractor Name");
    // 	var newContractUrl="/addNewContractor";

    // 	if(newContractorName.length>0){
    // 		$.post(newContractUrl, {contractorName:newContractorName}, function(data){
    //             window.location.reload(true);
    //         })
    // 	}
    // });

    $('#btnAddStaff').click(function(){

        var newStaffName=$('#txtStaffName').val();
        var newStaffType=$('select#ddStaffType').find('option:selected').val();
        var newStaffUrl="/addNewStaff";
       

        if(newStaffName.length>0 && newStaffType.length>0){
            $.post(newStaffUrl, {strStaffName:newStaffName, strStaffType:newStaffType}, function(data){

                window.location.reload(true);

            });
        }else{
            $('#txtStaffNameRequired').show();
        }
    });
    
    $('.week').blur(function(){
        
         quarter=1;
    	var weekNumber=this.id; // get week number
        
        var occupied=$(this).text(); // occupied percentage

    	var tempFind='.column.second-column #'+weekNumber;

    	var projectId=$(this).parent().parent().attr('projectid');

        
    	
        var employeeid=$(this).parent().parent().parent().attr('employeeid');

    	var total=0;
        var nInnovation=0;
        var nNonworking=0;
        var nExternal=0;
        var nInternal=0;

    	var empInfoRow=$(this).parent().parent().parent().find(tempFind).each(function(){

            var projectType=$(this).parent().parent().attr('projecttype');
            if($(this).text().length>0){

                if(projectType=="EXTERNAL"){
                     nExternal+=parseInt($(this).text());
                }else if(projectType=="INTERNAL"){
                    nInternal+=parseInt($(this).text());
                }else if(projectType=="NONWORKING"){
                    nNonworking+=parseInt($(this).text());
                }else if(projectType=="INNOVATION"){
                    nInnovation+=parseInt($(this).text());
                }

                total+=parseInt($(this).text());
            }else{
                $(this).text(0);
            }
    		
    	});
        
    	var prevRowWeek=$(this).parent().parent().parent().prev();
    	

        var prevRowWeekNum=$(this).parent().parent().parent().prev().find(tempFind);
        $(prevRowWeekNum).first().text(total);

        if(parseInt(total)>100){

            if((parseInt(total)+1)%10==0){
            
                $(prevRowWeekNum).first().removeClass('overload full less-external').addClass('prospect');
            }else{

                $(prevRowWeekNum).first().removeClass('full less-external').addClass('overload');
            }

            
         }
         if(parseInt(nExternal)>=80 && parseInt(total)<=100){

            if((parseInt(total)+1)%10==0){
            
                $(prevRowWeekNum).first().removeClass('overload full less-external').addClass('prospect');
            }else{

                $(prevRowWeekNum).first().removeClass('overload prospect less-external').addClass('full');   
            }
            
         }

         if(parseInt(nExternal)<80 && parseInt(total)<=100){

            if((parseInt(total)+1)%10==0){
            
                $(prevRowWeekNum).first().removeClass('overload full less-external').addClass('prospect');
            }else{

                $(prevRowWeekNum).first().removeClass('overload prospect full').addClass('less-external');   
            }
            
         }


         if((parseInt(total)+1)%10==0){
            
                $(prevRowWeekNum).first().removeClass('overload full less-external').addClass('prospect');
            }


        var updateProjUrl="/updateEmpProjOccupied";

        $.post(updateProjUrl, {sEmpId:employeeid, sProjId:projectId, sQuarter:quarter, sWeekNumber:weekNumber, sOccupied:occupied},function(data){

        });

    });

  

            //nWeekNumber is a hidden field
      var tempWeekNumber=1;
     

      $('.table-row.employee').each(function(index){

            var employee=$(this);
            var employeeInfo=$(this).next();

            for (var i = tempWeekNumber; i <=52; i++) {
              
                var selector=".column.second-column #"+i;

                var nWeekTotal=0; 
                var nWeekInternal=0;
                var nWeekExternal=0;
                var nWeekInnovation=0;
                var nWeekNonWorking=0;

                // Sum of external projects
                $(employeeInfo).find('.table-row[projecttype="EXTERNAL"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekExternal+=parseInt($(this).text());
                    }
                    
                    });
                });

                // Sum of internal projects
                $(employeeInfo).find('.table-row[projecttype="INTERNAL"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekInternal+=parseInt($(this).text());
                    }
                    
                    });
                });

                // Sum of innovation projects
                $(employeeInfo).find('.table-row[projecttype="INNOVATION"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekInnovation+=parseInt($(this).text());
                    }
                    
                    });
                });

                // Sum of Non-working projects
                $(employeeInfo).find('.table-row[projecttype="NONWORKING"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekNonWorking+=parseInt($(this).text());
                    }
                    
                    });
                });

                // SUm of total working includes External, Intennal, Nonworking, Innnovation
                $(employeeInfo).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekTotal+=parseInt($(this).text());
                    }
                    
                });

                

                $(employee).find(selector).first().text(nWeekTotal);
                
                // Validation rules 
                if(parseInt(nWeekExternal)>80 && parseInt(nWeekTotal)<=100){
                    if((parseInt(nWeekTotal)+1)%10==0){
            
                           $(employee).find(selector).first().removeClass('overload full less-external').addClass('prospect');
                        }else{

                            $(employee).find(selector).first().addClass('full');   
                        }
                   
                }

                if(parseInt(nWeekExternal)<80 && parseInt(nWeekTotal)<=100){
                    if((parseInt(nWeekTotal)+1)%10==0){
            
                           $(employee).find(selector).first().removeClass('overload full less-external').addClass('prospect');
                        }else{

                            $(employee).find(selector).first().addClass('less-external');   
                        }
                   
                }
                if(parseInt(nWeekTotal)>100){

                        if((parseInt(nWeekTotal)+1)%10==0){
            
                           $(employee).find(selector).first().removeClass('overload full').addClass('prospect');
                        }else{

                            $(employee).find(selector).first().addClass('overload');
                        }

                    
                 }

               if((parseInt(nWeekTotal)+1)%10==0){
            
                     $(employee).find(selector).first().removeClass('overload full').addClass('prospect');
                }
            };
      });
      
       $('.table-row.contractor').each(function(index){

            var employee=$(this);
            var employeeInfo=$(this).next();

            for (var i = tempWeekNumber; i <=52; i++) {
              
                var selector=".column.second-column #"+i;

                var nWeekTotal=0;
                var nWeekInternal=0;
                var nWeekExternal=0;
                var nWeekInnovation=0;
                var nWeekNonWorking=0;

                // Sum of external projects
                $(employeeInfo).find('.table-row[projecttype="EXTERNAL"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekExternal+=parseInt($(this).text());
                    }
                    
                    });
                });

                // Sum of internal projects
                $(employeeInfo).find('.table-row[projecttype="INTERNAL"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekInternal+=parseInt($(this).text());
                    }
                    
                    });
                });

                // Sum of innovation projects
                $(employeeInfo).find('.table-row[projecttype="INNOVATION"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekInnovation+=parseInt($(this).text());
                    }
                    
                    });
                });

                // Sum of Non-working projects
                $(employeeInfo).find('.table-row[projecttype="NONWORKING"]').each(function(){
                    $(this).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekNonWorking+=parseInt($(this).text());
                    }
                    
                    });
                });

                // Sum of total working projects include External, internal, innovation and Non-working

                $(employeeInfo).find(selector).each(function(){

                    if($(this).text().length>0){
                        nWeekTotal+=parseInt($(this).text());
                    }
                    
                });

                $(employee).find(selector).first().text(nWeekTotal);
                
                // Validation rules 
                if(parseInt(nWeekExternal)>80 && parseInt(nWeekTotal)<=100){
                    if((parseInt(nWeekTotal)+1)%10==0){
            
                           $(employee).find(selector).first().removeClass('overload full less-external').addClass('prospect');
                        }else{

                            $(employee).find(selector).first().addClass('full');   
                        }
                   
                }

                if(parseInt(nWeekExternal)<80 && parseInt(nWeekTotal)<=100){
                    if((parseInt(nWeekTotal)+1)%10==0){
            
                           $(employee).find(selector).first().removeClass('overload full less-external').addClass('prospect');
                        }else{

                            $(employee).find(selector).first().addClass('less-external');   
                        }
                   
                }
                if(parseInt(nWeekTotal)>100){

                        if((parseInt(nWeekTotal)+1)%10==0){
            
                           $(employee).find(selector).first().removeClass('overload full').addClass('prospect');
                        }else{

                            $(employee).find(selector).first().addClass('overload');
                        }

                    
                 }

               if((parseInt(nWeekTotal)+1)%10==0){
            
                     $(employee).find(selector).first().removeClass('overload full').addClass('prospect');
                }
               
            };
      });
     $('li.month-button').click(function(){

           $('li.month-button').removeClass('active');
           $(this).addClass('active');

           // change the quarter hidden field value
           var quarterNum=this.id;
           $("#nQuarterNumber").val(quarterNum);

         

      });

     var activeQuarter= $('li.month-button').hasClass('active');

     if(activeQuarter){
        var ele=$('li.month-button.active').get();

       
     }

    $('.remove').click(function(){
        var empId=this.id;

        if(confirm("Are you sure you want to delete employee ?")){
            $.ajax({
            url:'/deleteEmployee',
            data:{nEmpId:empId},
            success:function(){
            window.location.reload(true); },
            error:function(){
                
            }

        });

        }else{

        }
        
    });

    $('.remove-project').click(function(){
        var empId=$(this).parent().parent().parent().attr('employeeid');
        var projId=$(this).attr('projid');

        if(confirm("Are you sure you want to delete project this employee ?")){
            $.ajax({
            url:'/deleteProjectForEmployee',
            data:{nEmpId:empId, nProjId:projId},
            success:function(){
             window.location.reload(true);
            $(this).parent().parent().parent().parent().show() },
            error:function(){
                
            }

        });
        }
        
    })

    $('input.autocomplete').each(function(){
        var $input=$(this);
        var serverUrl=$input.data('url');
        $input.autocomplete({source:'http://localhost:9000/autocompleteLabel'});
    });
});




