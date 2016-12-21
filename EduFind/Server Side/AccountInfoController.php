<?php 
	
	require_once('Account.php');
	require_once("database.conf.php");
	
	header('Content-Type: application/json');
	
	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	
		$accountObj = new Account("", "", "", "", "", "", "", "", "", "", "");
		$response = $accountObj->GetUserInfoForAdminPanel($connection);
		
		die(json_encode( $response )."@EOR@");
	}


?>