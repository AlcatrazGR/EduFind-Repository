<?php

	require_once('Account.php');
	require_once('Book.php');
	require_once("database.conf.php");

	header('Content-Type: application/json');

	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	
		$username = $_POST['username'];
		
		$accountObj = new Account($username, "", 
			"", "", "", "","", "", "", "", "");
		$response = array();

		if(!$accountObj->DeleteProviderAccount($connection)) {
			$response["status"] = 0;
			$response["message"] = "There was an error while trying to delete your account. Please try again later or contact the support team.";
			die(json_encode( $response )."@EOR@");
		}
		else {
			
			$bookObj = new Book($username, "", "", "", "", "");
			if(!$bookObj->DeleteProviderBookRegistries($connection)) {
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to delete your account. Please try again later or contact the support team.";
				die(json_encode( $response )."@EOR@");
			}
			
			$response["status"] = 1;
			$response["message"] = "Your account has been successfully deleted.";
			die(json_encode( $response )."@EOR@");
		}

	}
	
	
	

?>