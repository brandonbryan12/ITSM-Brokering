var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var requestBody = ""; 
var btoa = require('btoa');
var client = new XMLHttpRequest();

client.open("get","https://dev33012.service-now.com/api/now/table/incident?sysparm_limit=10000");
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