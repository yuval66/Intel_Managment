<?php
 
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");

// check for post data
if (isset($_GET["workerID"])) 
{
    $workerid= $_GET["workerID"];
    $Password= $_GET["password"];
 
   $query = "SELECT * FROM `LOGIN` WHERE `WORKERID` = '".$workerid."' ";
   $result = mysql_query($query);
 
    if (mysql_num_rows($result) < 1) 
    {
         // no product found
            $response["success"] = 0;
            $response["message"] = "Worker ID doesn't exist.";
 
            // echo no users JSON
            echo json_encode($response);      
          
     } 
     
     else 
     {
            
           $result = mysql_fetch_array($result);
 
 	   if ( $result["PASSWORD"] == $Password )
 	   {
            
		    $elec= array();
		    $elec["WorkerID"] = $result["WORKERID"];
		    $elec["Fname"] = $result["FNAME"];
		    $elec["Lname"] = $result["LNANE"];
		    $elec["Role"] = $result["ROLE"];
		    $elec["Pass"] = $result["PASSWORD"];
	
		          
		      // success
		     $response["success"] = 1;
		 
		     // user node
		     $response["elec"] = array();
		 
		     array_push($response["elec"], $elec);
		 
		     // echoing JSON response
		     echo json_encode($response);
	     }
	     else
	     {
	     	 $response["success"] = 0;
            	 $response["message"] = "Wrong Password for this Worker ID.";
 
            // echo no users JSON
            	 echo json_encode($response); 
	     }
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