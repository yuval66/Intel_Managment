<?php

$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

 
// array for JSON response
$response = array();

 
// check for required fields
if (isset($_POST['ID'])) {
 
    $id= $_POST['ID'];

    	$query="SELECT * FROM `MISSIONS`  WHERE `ID` = '".$id."' ";
   
    	

    // mysql inserting a new row
$result = mysql_query($query);
    // check if row inserted or not
    if ($result) 
    {
    	 $row = mysql_fetch_array($result);
    	 
         $response["photo"] = $row["PHOTO"];
         $response["success"] = 1;
 
    	// echoing JSON response
   	 echo json_encode($response);
    } 
    else 
    {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} 
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required fields is missing";
 
    // echoing JSON response
    echo json_encode($response);
}

mysql_close();

?>