<?php
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");
 
  
   	$sort= $_GET['SORT'];
   	$missions= $_GET['MISSIONS'];

  
  	 if($sort === "Asset")
   	{
   		if($missions === "open")
         	 	$query = "SELECT * FROM `MISSIONS` WHERE `STATUS` !=  'Done' ORDER BY `BOARD` ";
         	else
         	        $query = "SELECT * FROM `MISSIONS` WHERE `STATUS` =  'Done' ORDER BY `BOARD` ";

   	}
   
   	else if($sort === "Name")
   	{
   		if($missions === "open")
         	 	$query = "SELECT * FROM `MISSIONS` WHERE `STATUS` !=  'Done' ORDER BY `NAME` ";
         	else
         	        $query = "SELECT * FROM `MISSIONS`  WHERE `STATUS` =  'Done' ORDER BY `NAME`";
   	}
   
   	else
   	{
   		if($missions === "open")
         	 	$query = "SELECT * FROM `MISSIONS`  WHERE `STATUS` != 'Done' ORDER BY `STATUS` DESC";
         	else
         	        $query = "SELECT * FROM `MISSIONS` WHERE `STATUS` =  'Done' ORDER BY `STATUS` DESC";
   	}
   
   
   
  
   $result = mysql_query($query);
 
 
    if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["missions"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $mission= array();
        $mission["board"] = $row["BOARD"];
        $mission["name"] = $row["NAME"];
        $mission["workerID"] = $row["WORKERID"];
        $wid =  $row["WORKERID"];
        $mission["sdate"] = $row["STARTDATE"];
        $mission["edate"] = $row["ENDDATE"];
        $mission["status"] = $row["STATUS"];
        $mission["notes"] = $row["NOTES"];
        $mission["hasphoto"] = $row["HASPHOTO"];
        $mission["id"] = $row["ID"];
        $mission["title"] = $row["TITLE"];
        
        $query2 = "SELECT `PHONE` FROM `LOGIN` WHERE `WORKERID` = '".$wid ."'";
   	$result2 = mysql_query($query2);
   	$row2 = mysql_fetch_row($result2);
   	
   	$phone = $row2[0];

	$mission["phone"] = $phone;
              
 
        // push single product into final response array
        array_push($response["missions"], $mission);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No missions found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>