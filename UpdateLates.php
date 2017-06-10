<?php
 
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

// check for post data
if (isset($_GET["missionID"])) 
{
    $missionid= $_GET["missionID"];

 
   $query = "UPDATE `MISSIONS` SET `STATUS` = 'Late' WHERE `ID` = '".$missionid."' ";
   $result = mysql_query($query);
   
  // $query2 = "SELECT `WORKERID` FROM  `MISSIONS` WHERE `ID` = '".$missionid."' ";
  // $result2 = mysql_query($query2);
  // $wID = $result2[0];
 
    if ($result) 
    {
		  $response["success"] = 1;
                  $response["message"] = "success";
                  
                  
                 // $query3 = "UPDATE  `LOGIN` SET  `LATES` = `LATES` + 1 WHERE  `WORKERID` =  '".$wID ."'  ";
                //  $result3 = mysql_query($query3);
                    
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