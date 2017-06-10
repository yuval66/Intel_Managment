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
    $newPasswordHash= $_POST["newPassMd"];

    
 
   $query = "UPDATE `LOGIN` SET `PASSWORD` = '".$newPasswordHash."' WHERE `WORKERID` = '".$workerid."' ";
   
   $result = mysql_query($query);
 
    if ($result) 
    {
	$query2 = "SELECT * FROM `LOGIN` WHERE `WORKERID` = '".$workerid."' "; 
        $resultOfMail = mysql_query($query2);
        
        if($resultOfMail)
        {
               $resultOfMail2= mysql_fetch_array($resultOfMail);
		         
		$from= 'gilad.tsidkiyahu@intel.com';
		$email = $resultOfMail2["MAIL"];
		
		$subject= 'Reset Password - Intel missions managment';
		$body= 'Hi, \nYour new paaword is : $newPassword.\nPlease get in to the app and change to a new password you will remember easily in the change password page';

		$msg= "Hi,
Your new password is : ".$newPassword."
Please get in to the app and change to a new password you will remember easily in the change password page.";
		
		mail($email, $subject, $msg, 'From:' . $from);
		
		$response["success"] = 1;
           	$response["message"] = "success";
 
            	// echo no users JSON
            	echo json_encode($response);
		
        }
        
        else
        {
            $response["success"] = 0;
            $response["message"] = "Mail doesn't exist for this user.";
 
            // echo no users JSON
            echo json_encode($response); 
        }
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