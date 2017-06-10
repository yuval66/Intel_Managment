<?php

$user="noya3105";
$password="yuval66z";
$database="intelManage";


define('HOST','localhost');
	define('USER','noya3105');
	define('PASS','yuval66z');
	define('DB','intelManage');
	
	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
 
// array for JSON response
$response = array();
$wID= 0;



 
// check for required fields
if (isset($_POST['Board']) && isset($_POST['Name']) && isset($_POST['Wid'])) {
 
    $board= $_POST['Board'];
    $name= $_POST['Name'];
    $wID= $_POST['Wid'];
    $sdate= $_POST['Sdate'];
    $edate= $_POST['Edate'];
    $status= $_POST['Status'];
    $notes= $_POST['Notes'];
    $photo= $_POST['Photo'];
    $hasphoto= $_POST['HasPhoto'];
    $title = $_POST['Title'];



   
     $query="INSERT INTO  `MISSIONS` (  `BOARD` ,  `NAME` ,  `WORKERID` ,  `STARTDATE` ,  `ENDDATE` ,  `STATUS` ,  `NOTES`, `PHOTO`, `HASPHOTO`, `TITLE` ) 
	VALUES (
	'$board',  '$name',  '$wID',  '$sdate',  '$edate',  '$status',  '$notes', ?, '$hasphoto', '$title '
	)";
    	
    	

	$stmt = mysqli_prepare($con,$query);
		
		mysqli_stmt_bind_param($stmt,"s",$photo);
		mysqli_stmt_execute($stmt);
		
		$check = mysqli_stmt_affected_rows($stmt);
		
	if($check == 1)
	{

        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Mission successfully added.";
        
         $query2 = "UPDATE  `LOGIN` SET  `TASKS` = `TASKS` + 1 WHERE  `WORKERID` =  '".$wID."'  ";
         mysqli_query($con,$query2);
 
 
        // echoing JSON response
        echo json_encode($response);
    } 
    else 
    {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred";
 
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