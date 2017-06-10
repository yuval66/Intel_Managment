<?php
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");
 
    $response["missions"] = array();

   $query1 = "SELECT COUNT(*) AS `count1` FROM `MISSIONS` ";
   $result1 = mysql_query($query1);
   
   $query2 = "SELECT * FROM `MISSIONS` WHERE `STATUS` = 'In Work'";
   $result2 = mysql_query($query2);
   $count2= mysql_num_rows($result2);
   
   $query3 = "SELECT * FROM `MISSIONS` WHERE `STATUS` = 'Late'";
   $result3 = mysql_query($query3);
   $count3= mysql_num_rows($result3);
   
   $query4 = "SELECT * FROM `MISSIONS` WHERE `STATUS` = 'Done'";
   $result4 = mysql_query($query4);
   $count4= mysql_num_rows($result4);
 
 
 
 
if ($result1) {
    // looping through all results
    // products node
 
        $mission= array();
        $num1 = mysql_fetch_assoc($result1);
	$count1 = $num1['count1'];
	
        
        $mission["MISSIONS_NUM"] = $count1;
        
        if($result2)
        {
          $mission["MISSIONS_INWORK"] = $count2;
        }
        else
           $mission["MISSIONS_INWORK"] = 0;	
           
        if($result3)
        {
          $mission["MISSIONS_LATE"] = $count3;
        }
        else
           $mission["MISSIONS_LATE"] = 0;
           
        if($result4)
        {
          $mission["MISSIONS_DONE"] = $count4;
        }
        else
           $mission["MISSIONS_DONE"] = 0;	
        
     
    	// success
    	$response["success"] = 1;
        array_push($response["missions"], $mission);
    	echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No Missions found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>