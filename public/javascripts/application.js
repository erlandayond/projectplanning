$(document).ready(function () {
	//JQUERY STUFF FOR EVENT HANDLERS ETC...
	$(".employee-info .project .project-name").attr('contentEditable', 'true');
	$(".employee-info .project .week").attr('contentEditable', 'true');
	var hiddenChangeStatus = $('#hidden-change-status').html();

	//Toggle function to show employees projects
	$('.toggler').live('click', function () {
		var thisRow = $(this).parent().parent();
		if (thisRow.next().hasClass('employee-info')) {
			thisRow.next('.employee-info').toggle();
			$(this).toggleClass('opened');
		}
	});
    
    $("#addEmployee").change(function(){
    	var empId=$("#addEmployee option:selected").val();
    	var empName=$('#addEmployee option:selected').text();
    	
    	if(empId=="new"){
    		var newEmpName=prompt("FirstName LastName");
    		var empUrl="http://localhost:9000/addEmployee";
    		if(newEmpName.length>0)
    		{
			 	$.post(empUrl, {ename:newEmpName, eType:"Regular"},function(data){

			 	});
			}
    	}
    });

    $("#addEmployee").change(function(){
    	var empId=$("#addEmployee option:selected").val();
    	var empName=$('#addEmployee option:selected').text();
    	
    	if(empId=="new"){
    		var newEmpName=prompt("FirstName LastName");
    		var empUrl="http://localhost:9000/addEmployee";
    		if(newEmpName.length>0)
    		{
			 	$.post(empUrl, {ename:newEmpName, eType:"Contractor"},function(data){

			 	});
			}
    	}
    });

	//add project link click function
	$("a.add-project").live('click', function () {
		var employeeId = $(this).parent().parent().parent().prev().attr('id');
		var employee = getEmployee(employeeId);
		employee.projects.push(new project('New Project', new Array("", "", "", "", "", "", "", "", "", "", "", "")));
		employee.paint(true);
		//UPDATE DATABASE HERE....
		$("#" + employeeId).next().find('.table-row .project-name').last().focus();
	});

	//add employee link click function
	$('a.add-employee').click(function () {
		if ($(this).hasClass('regular')) {
			employees.push(new employee(type = "regular"));
		} else if ($(this).hasClass('subcontractor')) {
			employees.push(new employee(type = "subcontractor"));
		}	
	});
	
	$('[contenteditable]').live('focus', function() {
	    var $this = $(this);
	    $this.data('before', $this.html());
	    if ($this.hasClass('week')) {
	    	$this.keydown(function(e) {
					var code = e.keyCode || e.which;
					if (code == '9') {
						console.log('tab pressed');
						$('.table-row .tool-tip').remove();
						$this.next().focus();
						return false;
					}
			});
	    }
	    return $this;
	}).live('blur', function(e) {
	    var $this = $(this);
	    var $e = $(e.target);
	    if ($this.hasClass('name')) {
	    	$this.validateEmployeeName();
	    }
	    if ($this.data('before') !== $this.html()) { //if information in field is changed
	        $this.data('before', $this.html());
	        if ($this.hasClass('project-name')) { //if the input is in the project name field
	        	$this.validateProjectName();			        	
	        }
	        if($this.hasClass('week')) {
	        	$this.validateWeek();
	        }
	    }
	    return $this;
	});
	//END OF EVENT HANDLERS ETC.

	//THE EMPLOYEE OBJECT....
	function employee(type, id, name ,projects, total, editable, status) {
		if (typeof(id) === "undefined") {
			var newNumber = employees.length+1;
			id = "employee"+newNumber;
			name = "New Employee";
			projects = new Array(0);
			total = new Array(12);
		}
		this.id = id;
		this.name = name;
		this.status = status;
		this.projects = projects;
		this.total = total;
		this.type = type;
		this.paint = paint;
		this.paint();
		function paint(showToggle) {
			var containerId = "#" + this.id;
			var prev = $(containerId).prev();
			var employeeContainer = '<div id="' + this.id + '" class="table-row employee"><div class="column person first-column"><div class="toggler"></div><span class="name">' + this.name + '</span></div><div class="column second-column"></div></div>';
			if(typeof prev !== 'undefined' && prev.length) {
				$(containerId).next().remove();
				$(containerId).remove();
				$(employeeContainer).insertAfter(prev);
			} else {
				if(this.type === "regular" ) {
					$('.table .employees-regular').append(employeeContainer);
				}
				else if (this.type === "subcontractor") {
					$('.table .employees-subcontractor').append(employeeContainer);
				}
			}

			function repaintTotal() {
				if (this.projects.length !== 0) {
					var newTotal = [0,0,0,0,0,0,0,0,0,0,0,0];
					for (i = 0; i < this.projects.length; i++) {
						for (j = 0; j < this.projects[i].weeks.length; j++) {
							if ((this.projects[i].weeks[j] !== "-") && (this.projects[i].weeks[j] !== undefined) && (this.projects[i].weeks[j] !== "")) {
								newTotal[j] += parseInt(this.projects[i].weeks[j]) || 0;
							}
							if (this.total[j] === "-" || this.total[j] === "") {
								newTotal[j] = this.total[j];
							}
						}
					}
					this.total = newTotal;
				} else {
					for (i = 0; i < this.total.length; i++) {
						if (this.total[i] !== "" && this.total[i] !== "-") {
							this.total[i] = "";
						} 
					}
				}
				for (i=0; i<this.total.length; i++) {
					if (typeof(this.total[i]) !== "undefined") {
						$('#' + this.id + ' .second-column').append('<div contentEditable="true" class="week">' + this.total[i] + '</div>');
					} else {
						$('#' + this.id + ' .second-column').append('<div contentEditable="true" class="week"></div>');
					}
					var $lastWeek = $('#' + this.id + ' .second-column').find('.week').last();
					if ($lastWeek.html() >= 80) {
						$lastWeek.addClass('full');
						if ($lastWeek.html() > 100) {
							$lastWeek.addClass('overload');
						}
					}
				}
			}
			this.repaintTotal = repaintTotal;
			this.repaintTotal();

			if(projects.length === 0) {
				$("#" + this.id).find('.name').attr('contentEditable', 'true');
			}
			if (showToggle) {
				$('<div style="display:block;" class="employee-info"></div>').insertAfter($('#' + this.id));
				$(containerId).find('.toggler').addClass('opened');
			} else {
				$('<div class="employee-info"></div>').insertAfter($('#' + this.id));
				$(containerId).find('.toggler').removeClass('opened');
			}
			var $thisEmployeeInfo = $("#" + this.id).next();
			for (i=0; i<this.projects.length; i++) {
				$thisEmployeeInfo.append('<div class="table-row project"><div class="column first-column"><span class="project-name" contentEditable="true">' + this.projects[i].name + '</span></div><div class="column second-column"></div></div>');
				var $thisProject = $thisEmployeeInfo.find('.project').last();
				$thisProject = $thisProject.find('.second-column');
				for (j=0; j<this.projects[i].weeks.length; j++) {
					if (this.projects[i].weeks[j] >= 80) {
	        			$thisProject.append('<div contentEditable="true" class="week full">' + this.projects[i].weeks[j]+'</div>');

	        		} else {
	        			$thisProject.append('<div contentEditable="true" class="week">' + this.projects[i].weeks[j]+'</div>');
	        		}
	        		var $lastWeek = $thisProject.find('.week').last();
					if ($lastWeek.html() >= 80) {
						$lastWeek.addClass('full');
						if ($lastWeek.html() > 100) {
							$lastWeek.addClass('overload');
						}
					}
					var newTotal = 0;
					for (k = 0; k < this.projects.length; k++) {
						if (this.projects[k].weeks[$lastWeek.index()] !=="") {
							newTotal += parseInt(this.projects[k].weeks[$lastWeek.index()]);
						}
					}
					if (this.total[$lastWeek.index()] !== "-" && this.total[$lastWeek.index()] !== "") {
						if (newTotal !== 0) {
							this.total[$lastWeek.index()] = parseInt(newTotal);
						} else {
							this.total[$lastWeek.index()] = "";
						}
					}
				}
			}
			$thisEmployeeInfo.append('<div class="table-row"><div class="column first-column"><a href="#" class="add-project">Add project</a></div><div class="column second-column"></div></div>');
			var $lastRow = $thisEmployeeInfo.find('.table-row').last();
			var $secondColumn = $lastRow.find('.second-column');
			for (i = 0; i < 12; i++) {
				$secondColumn.append('<div class="week"></div>');
			}
		}
	}
	//END OF EMPLOYEE OBJECT...

	function project(name, weeks) {
		this.name = name;
		this.weeks = weeks;
	}

	function repaintAll () {
		for (i=0; i < employees.length; i++) {
			employees[i].paint();
		}
	};

	function getEmployee(id) {
		var employee;
		for (i=0; i<employees.length; i++) {
			if(employees[i].id === id) {
				employee = employees[i];
				break;
			}
		}
		return employee;
	}

	//VALIDATE INPUT IN PROJECT NAME FIELD
	jQuery.fn.validateProjectName = function () {
		var o = $(this[0]);
		var employeeId = o.parent().parent().parent().prev().attr('id');
		var employee = getEmployee(employeeId);
		var projectIndex = o.parent().parent().index();
		if (o.html() === "<br>" || o.html() === "" ){ //if field is "empty"
	        employee.projects.splice(projectIndex,1);
	    	//UPDATE DATABASE HERE....
	    	employee.repaintTotal();
	    	employee.paint(true);
	        o.parent().parent().next().find('.week').first().focus();
	    } else {
	    	employee.projects[projectIndex].name = o.html(); //else save the new name into the object
	    	//UPDATE DATABASE HERE....
	    	employee.paint(true);
	    }
	}

	//VALIDATE INPUT IN EMPLOYEE NAME FIELD
	jQuery.fn.validateEmployeeName = function () {
		var o = $(this[0]);
		var employeeId = o.parent().parent().attr('id');
		var employee = getEmployee(employeeId);
		var employeeIndex;
		if (o.html().indexOf("<br") >= 0) { //if field is "empty"
			for (i = 0; i < employees.length; i++) { //find index of the employee in the employees array
				if (employees[i].id === employeeId) {
					employeeIndex = i;
					break;
				}
			}
			o.parent().parent().next().remove(); //remove employee info lines
			o.parent().parent().remove(); //remove line
			employees.splice(employeeIndex,1); //remove from employees array
		}
		else {
			employee.name = o.html(); //save the new name into the object
			//UPDATE DATABASE HERE....

			var empUrl="http://localhost:9000/addEmployee";
			 alert("gen url"+empUrl);
			 $.ajax({
			  	url: empUrl,
			 	data: 'ename='+employee,
			 	success: function(data){
			 		alert(data);
			 	}
			 });
			employee.paint();
		}
	}

	//VALIDATE INPUT IN WEEK FIELD

	jQuery.fn.validateWeek = function () {
		var o = $(this[0]);
		if (o.html().indexOf("<br") >= 0) {
	       	//remove the line break that will end up in empty fields for some reason...
	        o.html('');
	    }
	    if (o.html().indexOf(".") == 0) {
	        o.addClass('unsure'); //shows that the deal is not 100%;
	        o.html(o.html().substring(1));
	    } else {
	       	o.removeClass('unsure');
	    }
	    if (o.html() !== "-") { //if the information in the week field does not contain a "-"....
	    	if(o.parent().parent().hasClass('table-row employee')) { //total row
	    		employeeId = o.parent().parent().attr('id');
				employee = getEmployee(employeeId);
	    		if(o.html() === "") {
	    			var fieldClear = true;
					for (i=0; i<employee.projects.length; i++) {
						if (!(employee.projects[i].weeks[o.index()] === "-" || employee.projects[i].weeks[o.index()] === ""))  {
							fieldClear = false;
							break;
						}
					}
					if (fieldClear) {
						employee.total[o.index()] = "";
						for (i=0; i<employee.projects.length; i++) {
							employee.projects[i].weeks[o.index()] = "";
						}
					}
	    		} else if (o.html() === "-"){
	    			employee.total[o.index()] = "-";
	    			for (i=0; i<employee.projects.length; i++) {
						employee.projects[i].weeks[o.index()] = "-";
					}
	    		} 
	    		//UPDATE DATABASE HERE....
				employee.paint(true);
		    } else {
				o.html(parseInt(o.html())); //make sure that the input is integers and nothing else
		        if (o.html() === "NaN") {
		        	o.html(""); //delete information in input that is not an integer
		        }
		        var isProjectWeek = o.parent().parent().hasClass('project');
				var employeeId;
				var employee;
				if(isProjectWeek) {
					employeeId = o.parent().parent().parent().prev().attr('id');
					employee = getEmployee(employeeId);
					projectIndex = o.parent().parent().index();
					if (employee.total[o.index()] === "-") {
						for (i=0; i<employee.projects.length; i++) {
							employee.projects[i].weeks[o.index()] = "-";
						}
						employee.paint(true);
					} else {
						var weeksTotal = 0;
						for (i=0; i<employee.projects.length; i++) {
							if (i !== projectIndex && employee.projects[i].weeks[o.index()] !== "") {
								weeksTotal += parseInt(employee.projects[i].weeks[o.index()]);
							}
						}
						if(o.html() !== "") {
							weeksTotal += parseInt(o.html());
							employee.projects[projectIndex].weeks[o.index()] = parseInt(o.html());
						} else {
							employee.projects[projectIndex].weeks[o.index()] = o.html();
						}
						if (weeksTotal !== 0) {
							employee.total[o.index()] = weeksTotal;
						}
						else {
							employee.total[o.index()] = "";
						}
						//UPDATE DATABASE HERE....
						employee.paint(true);
					}
				} else { //field is on the total row for the week	
				}
			}
		} else {
			if(o.parent().parent().hasClass('table-row employee')) { //total row
				employeeId = o.parent().parent().attr('id');
				employee = getEmployee(employeeId);
				if (o.html() === "-") {
					employee.total[o.index()] = "-";
					for (i=0; i<employee.projects.length; i++) {
						employee.projects[i].weeks[o.index()] = "-";
					}
					//UPDATE DATABASE HERE....
					employee.paint(true);
				} else {
					o.html('');
				}
			}
		}
	}
    
   
   
    // var empJSONUrl="http://localhost:9000/getJSONEmployeeInfo";
			 
			 // $.ajax({
			 //  	url: empJSONUrl,
			 // 	success: function(data){
			 // 		var testData = data;
			 // 		for(var i=0;i<testData.employees.length;i++){

    //                     Array tmpProjects=new Array();
			 // 			var tmpProjectName="";
			 // 			var tmpEmpType="";
			 // 			Array tmpOccupiedArray=new Array();
			 // 			for(var j=0;j<testData.employees[i].listProjectWorking.length;j++){

			 // 				tmpOccupiedArray.push(testData.employees[i].listProjectWorking[j].nOccupied.toString());
			 // 				tmpProjectName=testData.employees[i].listProjectWorking[j].strProjectName;
			 // 				tmpEmpType="regular";
			 // 			}
			 // 			var tmpProject=new Project(tmpProjectName, tmpOccupiedArray);
			 // 			var tmpEmp=new employee(tmpEmpType,'tmpEmp', tmpProjectName, tmpProject);
			 // 			employees=new Array(tmpEmp);

			 // 		}

			 		
			 // 	}
			 // });
	//CREATE SOME OBJECTS FOR TEST PURPOSE... THIS STUFF SHOULD BE FETCHED FROM BACKEND DATABASE LATER ON...
	total1 = new Array("-", "100", "80", "10", "-", "", "20", "60", "", "-", "10", "-");
	project1 = new project('testprojekt', new Array("-", "100", "80", "10", "-", "", "20", "60", "", "-", "10", "-"));
	project2 = new project('BUS', new Array("-", "90", "80", "10", "-", "", "20", "60", "", "-", "10", "-"));
	projects1 = new Array(project1, project2);
	employee1 = new employee('regular', 'employee1', 'Pelle Persson', projects1, total1);
	employee2 = new employee('regular', 'employee2', 'Per Sunesson', projects1, total1);
	employee3 = new employee('regular', 'employee3', 'Fredrik Aldegren', projects1, total1);
	employee4 = new employee('regular', 'employee4', 'Anders Velander', projects1, total1);
	employee5 = new employee('subcontractor', 'employee5', 'Thomas Arctaedius', projects1, total1);
	employees = new Array(employee1, employee2, employee3, employee4, employee5);
});