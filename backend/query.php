<?php

require_once("JSON.php");
$json = new Services_JSON();

$db = mysql_connect('db569989960.db.1and1.com', 'dbo569989960', 'zgzappstore');
    if (!$db) {
        die('Could not connect to db: ' . mysql_error());
    }
 
    //Select the Database
    mysql_select_db("db569989960",$db);
    
    //Replace * in the query with the column names.
    $result = mysql_query("select * from encontrados", $db);  
    
    //Create an array
    $json_response = array();
    
    while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
        $row_array['id'] = $row['id'];
        $row_array['text'] = $row['text'];
        $row_array['latitude'] = $row['latitude'];
        $row_array['longitude'] = $row['longitude'];
        $row_array['type'] = $row['type'];
        
        //push the values in the array
        array_push($json_response,$row_array);
    }
	
echo $json->encode($json_response);
    
    //Close the database connection
    fclose($db);

?>