<?php
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");
 
   $query = "SELECT * FROM `LOGIN` ORDER BY `FNAME`";
   $result = mysql_query($query);
 
 
    if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["elecs"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $elec= array();
        $elec["workerID"] = $row["WORKERID"];
        $elec["Fname"] = $row["FNAME"];
        $elec["Lname"] = $row["LNANE"];
        $elec["mail"] = $row["MAIL"];
        $elec["phone"] = $row["PHONE"];
        
        
        $workerid = $row["WORKERID"];
        $query = "SELECT COUNT(*) AS `count` FROM `MISSIONS` WHERE `WORKERID` = '".$workerid."'";
   	$res= mysql_query($query);
	
	
	if($res)
	{
		$num= mysql_fetch_assoc($res);
		$count = $num['count'];
	}
		
	else
	{
		$count = 0;
	}
 
 	$elec["numMissions"] = $count;
 	
        // push single product into final response array
        array_push($response["elecs"], $elec);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No elecs found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>