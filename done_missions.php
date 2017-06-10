<?php
 
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

// check for post data
if (isset($_POST["ID"])) 
{
    $id= $_POST["ID"];    

 
   $query ="UPDATE `MISSIONS` SET `STATUS` = 'Done' WHERE `ID`= '".$id."' ";
   $result = mysql_query($query);
 
    if ($result) 
    {
	$response["success"] = 1;
        $response["message"] = "success";
        
      // $query2 ="SELECT * FROM `WEEKS` ORDER BY `ID` DESC LIMIT 1 ";
      // $result2 = mysql_query($query2);

      //   while ($row = mysql_fetch_array($result2)) {
       //    $id2 = $row["ID"];
       //  }
             
        // $query3 ="UPDATE `WEEKS` SET `NUM` = `NUM` + 1 WHERE `ID`= '".$id2 ."' ";
   	 //$result3 = mysql_query($query3);
    
     	$query4 ="SELECT CURDATE()";
  	$result4 = mysql_query($query4);
  	$time =  mysql_fetch_array($result4);
  
   
   	 $query6 ="SELECT WEEK('".$time[0]."')";
  	$result6 = mysql_query($query6);
  	 $time =  mysql_fetch_array($result6);
    	$week = (int)$time[0];

 	$query3 ="UPDATE `WEEKS` SET `NUM` = `NUM` + 1 WHERE `ID`= '".$week ."' ";
  	$result3 = mysql_query($query3);

        
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