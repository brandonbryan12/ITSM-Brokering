var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var requestBody = "{'state':'2','short_description':'PUT TESTING IS WORKING AND HAS BEEN MODIFIED','urgency':'2','impact':'2'}"; 
var btoa = require('btoa');
var client = new XMLHttpRequest();

client.open("put","https://dev33012.service-now.com/api/now/table/incident/a448ef71db011300f84df00fbf961929?state=2");
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
