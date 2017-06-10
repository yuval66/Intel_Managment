<?php
 
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

// check for post data
if (isset($_POST['ID'])) 
{
    $workerid= $_POST['ID'];
    

   $query = "DELETE FROM `LOGIN` WHERE `WORKERID` = '".$workerid."' ";
   
   $result = mysql_query($query);
 
    if (mysql_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "elec successfully deleted";
 
        // echoing JSON response
        echo json_encode($response);                   
    } 
     
     else 
     {
         $response["success"] = 0;
         $response["message"] = "Something wrong. Try again";
 
            // echo no users JSON
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