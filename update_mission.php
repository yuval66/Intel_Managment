<?php

$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

 
// array for JSON response
$response = array();

 
// check for required fields
if (isset($_POST['Sdate']) && isset($_POST['Edate']) && isset($_POST['ID'])) {
 
    $sdate= $_POST['Sdate'];
    $edate= $_POST['Edate'];
    $notes= $_POST['Notes'];
    $change = $_POST['change'];
    
    if (isset($_POST['Photo']))
    {
    	$photo= $_POST['Photo'];
    }
    $hasphoto= $_POST['HasPhoto'];
    $id= $_POST['ID'];
    
    if (isset($_POST['Photo']))
    {
    	if($change === "yes")
    	{

	    	$query="UPDATE `MISSIONS` SET `STARTDATE` = '".$sdate."' ,  `ENDDATE` =  '".$edate."', 					
	        `NOTES` = '".$notes."', `PHOTO` =  '".$photo."', `HASPHOTO` =  '".$hasphoto."',`STATUS` =  'In Work'
	
	   	   WHERE `ID` = '".$id."' ";
	}
	
	else
	{
		$query="UPDATE `MISSIONS` SET `STARTDATE` = '".$sdate."' ,  `ENDDATE` =  '".$edate."', 					
	        `NOTES` = '".$notes."', `PHOTO` =  '".$photo."', `HASPHOTO` =  '".$hasphoto."'
	
	   	   WHERE `ID` = '".$id."' ";
	}
    }
    
    else
    {
    
    	if($change === "yes")
    	{
	      $query="UPDATE `MISSIONS` SET `STARTDATE` = '".$sdate."' ,  `ENDDATE` =  '".$edate."', 					
	        `NOTES` = '".$notes."', `HASPHOTO` =  '".$hasphoto."', `STATUS` =  'In Work'
	
	   	   WHERE `ID` = '".$id."' ";
	}
	
	else
	{
	 	$query="UPDATE `MISSIONS` SET `STARTDATE` = '".$sdate."' ,  `ENDDATE` =  '".$edate."', 					
	        `NOTES` = '".$notes."', `HASPHOTO` =  '".$hasphoto."'
	
	   	   WHERE `ID` = '".$id."' ";
	
	}
    }
   
    	

    // mysql inserting a new row
$result = mysql_query($query);
    // check if row inserted or not
    if ($result) 
    {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Mission successfully updated.";
 
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