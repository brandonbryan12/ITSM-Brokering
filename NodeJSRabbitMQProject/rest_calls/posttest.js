var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var requestBody = "{'short_description':'Unable to connect to office wifi','urgency':'2','impact':'2'}"; 
var btoa = require('btoa');
var client = new XMLHttpRequest();

client.open("post","https://dev33012.service-now.com/api/now/table/incident?sysparm_limit=1");
client.setRequestHeader('Accept','application/json');
client.setRequestHeader('Content-Type','application/json');

//Eg. UserName="admin", Password="admin" for this code sample.
client.setRequestHeader('Authorization', 'Basic '+btoa('admin'+':'+'SEseniorproject1!'));

client.onreadystatechange = function() 
{ 
	if(this.readyState == this.DONE) 
	{
		console.log(this.status);
		console.log(this.responseText); 
	}
}; 
client.send(requestBody);