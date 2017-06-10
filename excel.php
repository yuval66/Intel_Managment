<?php 



function xlsBOF() {
	echo pack("ssssss", 0x809, 0x8, 0x0, 0x10, 0x0, 0x0);
}
function xlsEOF() {
	echo pack("ss", 0x0A, 0x00);
}
function xlsWriteNumber($Row, $Col, $Value) {
	echo pack("sssss", 0x203, 14, $Row, $Col, 0x0);
	echo pack("d", $Value);
}
function xlsWriteLabel($Row, $Col, $Value) {
	$L = strlen($Value);
	echo pack("ssssss", 0x204, 8 + $L, $Row, $Col, 0x0, $L);
	echo $Value;
} 
// prepare headers information
header("Content-Type: application/force-download");
header("Content-Type: application/octet-stream");
header("Content-Type: application/download");
header("Content-Disposition: attachment; filename=\"BackUp".date("Y-m-d").".xls\"");
header("Content-Transfer-Encoding: binary");
header("Pragma: no-cache");
header("Expires: 0");
// start exporting
xlsBOF();
// first row 


$conn = new mysqli('localhost', 'noya3105', 'yuval66z');  
mysqli_select_db($conn, 'intelManage');  

$setSql = "SELECT `BOARD`, `NAME`,`WORKERID` ,`STARTDATE`, `ENDDATE`,`NOTES`  FROM `MISSIONS`";  
$setRec = mysqli_query($conn, $setSql);  
  
$columnHeader = '';  
$columnHeader = "BOARD" . "\t" . "NAME" . "\t" . "WORKER ID" . "\t" . "START DATE" . "\t" . "END DATE" . "\t" . "NOTES" . "\t";  
  
$setData = '';  
xlsWriteLabel(0, 0, "BOARD");
xlsWriteLabel(0, 1, "NAME");
xlsWriteLabel(0, 2, "WORKER ID");
xlsWriteLabel(0, 3, "START DATE");
xlsWriteLabel(0, 4, "END DATE");
xlsWriteLabel(0, 5, "NOTES");
$i = 1; 
while ($rec = mysqli_fetch_row($setRec)) {  
	
	$j = 0;
    $rowData = '';  
    foreach ($rec as $value) {  
        xlsWriteLabel($i, $j, $value);
        $j = $j + 1;

    }  
    $i = $i + 1; 
}  
  
xlsEOF();
  
?>  