<?php
 
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

// check for post data
if (isset($_POST["workerID"])) 
{
    $workerid= $_POST["workerID"];
    $newPassword= $_POST["newPass"];
    
 
   $query = "UPDATE `LOGIN` SET `PASSWORD` = '".$newPassword."' WHERE `WORKERID` = '".$workerid."' ";
   
   $result = mysql_query($query);
 
    if ($result) 
    {
		  $response["success"] = 1;
                  $response["message"] = "success";
                  echo json_encode($response);
    } 
    else 
    {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Failed change password";
 
    // echoing JSON response
    echo json_encode($response);
   }
	  
	
                   
 } 
     
 
else 
{
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required fields is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>