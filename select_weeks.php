<?php
 
$user="noya3105";
$password="yuval66z";
$database="intelManage";

mysql_connect(localhost,$user,$password) or die ("Unable to connect");

@mysql_select_db($database) or die("Unable to select database");
 
  
   
    $query = "SELECT * FROM `WEEKS` ORDER BY `ID` ";
   
  
   $result = mysql_query($query);
 
 
    if (mysql_num_rows($result) > 0) {
    
    $response["weeks"] = array();

    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $week = array();
        $week["num"] = $row["NUM"];
        $week["date"] = $row["DATE"];
        $week["id"] = $row["ID"];
        array_push($response["weeks"], $week);

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