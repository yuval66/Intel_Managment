<?php

$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

 
// array for JSON response
$response = array();

 
// check for required fields
if (isset($_POST['WORKERID']) && isset($_POST['LNAME']) && isset($_POST['FNAME'])) {
 
    $workerID = $_POST['WORKERID'];
    $first_name = $_POST['FNAME'];
    $last_name = $_POST['LNAME'];
    $password = $_POST['PASSWORD'];
    $phone = $_POST['PHONE'];
    $mail = $_POST['MAIL'];
    $role = $_POST['ROLE'];
 
 
 
$query="INSERT INTO  `LOGIN` (  `WORKERID` ,  `FNAME` ,  `LNANE` ,  `PASSWORD` ,  `PHONE` ,  `MAIL` ,  `ROLE` ) 
VALUES (
'$workerID',  '$first_name',  '$last_name',  '$password',  '$phone',  '$mail',  '$role'
)";
    // mysql inserting a new row
$result = mysql_query($query);
    // check if row inserted or not
    if ($result) 
    {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Electrician successfully added.";
 
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