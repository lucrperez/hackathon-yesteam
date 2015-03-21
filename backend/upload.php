<?php

$db = mysql_connect('db569989960.db.1and1.com', 'dbo569989960', 'zgzappstore');
    if (!$db) {
        die('Could not connect to db: ' . mysql_error());
    }
 
    //Select the Database
    mysql_select_db("db569989960",$db);

	
	$text = $_REQUEST["text"];
	$latitude = $_REQUEST["latitude"];
	$longitude = $_REQUEST["longitude"];
	$type = $_REQUEST["type"];

	mysql_query("insert into encontrados (text,latitude,longitude,type) values ('".$text."','".$latitude."','".$longitude."',".$type.")", $db);  
	
	    fclose($db);
	
?>